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

package eu.decentsoftware.holograms.profiler.command;

import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.commands.CommandHandler;
import eu.decentsoftware.holograms.api.commands.CommandInfo;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.commands.TabCompleteHandler;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.ProfilerMetric;

import java.util.Collections;

@CommandInfo(
        usage = "/dh profiler stats [metric]",
        description = "View stats for a specific profiler metric",
        permissions = "dh.command.profiler.stats"
)
class ProfilerStatsCommand extends DecentCommand {

    private final DecentProfiler profiler;

    ProfilerStatsCommand(DecentProfiler profiler) {
        super("stats");
        this.profiler = profiler;
    }

    @Override
    public CommandHandler getCommandHandler() {
        return (sender, args) -> {
            if (args.length == 0) {
                Lang.PROFILER_STATS.send(sender, profiler.getStats());
                return true;
            }

            String metricId = args[0];
            ProfilerMetric metric = profiler.getTimer(metricId);
            if (metric == null) {
                Lang.PROFILER_METRIC_NOT_FOUND.send(sender, metricId);
                return true;
            }

            Lang.PROFILER_STATS.send(sender, metric.getFormattedStats());
            return true;
        };
    }

    @Override
    public TabCompleteHandler getTabCompleteHandler() {
        return (sender, args) -> {
            if (args.length == 1) {
                return TabCompleteHandler.getPartialMatches(args[0], profiler.getTimerNames());
            }
            return Collections.emptyList();
        };
    }
}
