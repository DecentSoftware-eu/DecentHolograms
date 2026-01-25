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

package eu.decentsoftware.holograms.display.config;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.config.dto.ConfigDisplay;

import java.util.ArrayList;
import java.util.List;

public class DisplayPersistenceService {

    private final DisplayConfigService configService;
    private final DisplayConfigMapper configMapper;

    public DisplayPersistenceService(DisplayConfigService configService, DisplayConfigMapper configMapper) {
        this.configService = configService;
        this.configMapper = configMapper;
    }

    public void saveDisplay(DisplayBase display) {
        ConfigDisplay configDisplay = configMapper.toDto(display);
        configService.save(display.getName(), configDisplay);
    }

    public void deleteDisplay(DisplayBase display) {
        configService.delete(display.getName());
    }

    public List<DisplayBase> loadAllDisplays() {
        List<ConfigDisplay> configDisplays = configService.loadAll();
        List<DisplayBase> displays = new ArrayList<>();
        for (ConfigDisplay configDisplay : configDisplays) {
            try {
                DisplayBase display = configMapper.toDomain(configDisplay);
                displays.add(display);
            } catch (DisplayConfigException e) {
                Log.error("Failed to load display '%s': %s", configDisplay.getName(), e.getMessage());
            }
        }
        return displays;
    }
}
