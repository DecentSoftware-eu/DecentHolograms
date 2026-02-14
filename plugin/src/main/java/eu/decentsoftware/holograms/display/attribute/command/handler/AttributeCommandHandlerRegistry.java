/*
 * This file is part of DecentHolograms, licensed under the GNU GPL v3.0 License.
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.display.attribute.command.handler;

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AttributeCommandHandlerRegistry {

    private static class HandlerSet<T> {
        private AttributeCommandHandler<T> defaultHandler;
        private final Map<String, AttributeCommandHandler<T>> keywordHandlers = new HashMap<>();
    }

    private final Map<AttributeKey<?>, HandlerSet<?>> handlerSets = new ConcurrentHashMap<>();

    public <T> void register(AttributeKey<T> key, AttributeCommandHandler<T> defaultHandler, AttributeCommandHandler<T>... keywordHandlers) {
        register(key, defaultHandler);
        for (AttributeCommandHandler<T> keywordHandler : keywordHandlers) {
            register(key, keywordHandler);
        }
    }

    public <T> void register(AttributeKey<T> key, AttributeCommandHandler<T> handler) {
        HandlerSet<T> set = getOrCreateHandlerSet(key);

        String keyword = handler.getKeyword();
        if (keyword == null) {
            if (set.defaultHandler != null) {
                throw new IllegalStateException("Default handler already registered for attribute: " + key.getName());
            }
            set.defaultHandler = handler;
        } else {
            if (set.keywordHandlers.containsKey(keyword)) {
                throw new IllegalStateException("Keyword '" + keyword + "' already registered for attribute: " + key.getName());
            }
            set.keywordHandlers.put(keyword, handler);
        }
    }

    @Nullable
    public <T> AttributeCommandHandler<T> getHandler(AttributeKey<T> key, String[] args) {
        HandlerSet<T> set = getHandlerSet(key);
        if (set == null) {
            return null;
        }

        if (args.length > 0) {
            AttributeCommandHandler<T> keywordHandler = set.keywordHandlers.get(args[0]);
            if (keywordHandler != null) {
                return keywordHandler;
            }
        }

        return set.defaultHandler;
    }

    public <T> Set<String> getKeywords(AttributeKey<T> key) {
        HandlerSet<T> set = getHandlerSet(key);
        return set != null ? set.keywordHandlers.keySet() : Collections.emptySet();
    }

    @Nullable
    public <T> AttributeCommandHandler<T> getDefaultHandler(AttributeKey<T> key) {
        HandlerSet<T> set = getHandlerSet(key);
        return set != null ? set.defaultHandler : null;
    }

    @SuppressWarnings("unchecked")
    private <T> HandlerSet<T> getOrCreateHandlerSet(AttributeKey<T> key) {
        return (HandlerSet<T>) handlerSets.computeIfAbsent(key, k -> new HandlerSet<>());
    }

    @SuppressWarnings("unchecked")
    private <T> HandlerSet<T> getHandlerSet(AttributeKey<T> key) {
        return (HandlerSet<T>) handlerSets.get(key);
    }
}