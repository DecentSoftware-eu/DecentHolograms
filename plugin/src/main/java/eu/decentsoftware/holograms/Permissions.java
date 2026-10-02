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

package eu.decentsoftware.holograms;

/**
 * Permissions of DecentHolograms.
 */
public final class Permissions {

    private Permissions() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String ADMIN = "dh.admin";
    public static final String DEFAULT = "dh.default";

    // -- Commands

    private static final String COMMANDS = "dh.command";

    public static final String COMMAND_HELP = COMMANDS + ".help";
    public static final String COMMAND_RELOAD = COMMANDS + ".reload";
    public static final String COMMAND_DECENT_HOLOGRAMS = COMMANDS + ".decentholograms";
    public static final String COMMAND_VERSION = COMMANDS + ".version";

    // -- Hologram Commands

    public static final String COMMAND_HOLOGRAMS_LIST = COMMANDS + ".list";
    public static final String COMMAND_HOLOGRAMS_CONVERT = COMMANDS + ".convert";
    public static final String COMMAND_HOLOGRAMS = COMMANDS + ".holograms";
    public static final String COMMAND_HOLOGRAMS_UPDATE = COMMAND_HOLOGRAMS + ".update";
    public static final String COMMAND_HOLOGRAMS_ALIGN = COMMAND_HOLOGRAMS + ".align";
    public static final String COMMAND_HOLOGRAMS_CENTER = COMMAND_HOLOGRAMS + ".center";
    public static final String COMMAND_HOLOGRAMS_CLONE = COMMAND_HOLOGRAMS + ".clone";
    public static final String COMMAND_HOLOGRAMS_CREATE = COMMAND_HOLOGRAMS + ".create";
    public static final String COMMAND_HOLOGRAMS_DELETE = COMMAND_HOLOGRAMS + ".delete";
    public static final String COMMAND_HOLOGRAMS_DISABLE = COMMAND_HOLOGRAMS + ".disable";
    public static final String COMMAND_HOLOGRAMS_DISPLAY_RANGE = COMMAND_HOLOGRAMS + ".setdisplayrange";
    public static final String COMMAND_HOLOGRAMS_DOWN_ORIGIN = COMMAND_HOLOGRAMS + ".downorigin";
    public static final String COMMAND_HOLOGRAMS_ENABLE = COMMAND_HOLOGRAMS + ".enable";
    public static final String COMMAND_HOLOGRAMS_FACING = COMMAND_HOLOGRAMS + ".setfacing";
    public static final String COMMAND_HOLOGRAMS_ADD_FLAG = COMMAND_HOLOGRAMS + ".addflag";
    public static final String COMMAND_HOLOGRAMS_REMOVE_FLAG = COMMAND_HOLOGRAMS + ".removeflag";
    public static final String COMMAND_HOLOGRAMS_HELP = COMMAND_HOLOGRAMS + ".help";
    public static final String COMMAND_HOLOGRAMS_INFO = COMMAND_HOLOGRAMS + ".info";
    public static final String COMMAND_HOLOGRAMS_LINES = COMMAND_HOLOGRAMS + ".lines";
    public static final String COMMAND_HOLOGRAMS_MOVE_HERE = COMMAND_HOLOGRAMS + ".movehere";
    public static final String COMMAND_HOLOGRAMS_MOVE = COMMAND_HOLOGRAMS + ".move";
    public static final String COMMAND_HOLOGRAMS_NEARBY = COMMAND_HOLOGRAMS + ".near";
    public static final String COMMAND_HOLOGRAMS_SET_PERMISSION = COMMAND_HOLOGRAMS + ".setpermission";
    public static final String COMMAND_HOLOGRAMS_TELEPORT = COMMAND_HOLOGRAMS + ".teleport";
    public static final String COMMAND_HOLOGRAMS_UPDATE_INTERVAL = COMMAND_HOLOGRAMS + ".setupdateinterval";
    public static final String COMMAND_HOLOGRAMS_UPDATE_RANGE = COMMAND_HOLOGRAMS + ".setupdaterange";
    public static final String COMMAND_HOLOGRAMS_RENAME = COMMAND_HOLOGRAMS + ".rename";

    // -- Page Commands

    public static final String COMMAND_PAGES = COMMANDS + ".pages";
    public static final String COMMAND_PAGES_HELP = COMMAND_PAGES + ".help";
    public static final String COMMAND_PAGES_ADD = COMMAND_PAGES + ".add";
    public static final String COMMAND_PAGES_INSERT = COMMAND_PAGES + ".insert";
    public static final String COMMAND_PAGES_REMOVE = COMMAND_PAGES + ".remove";
    public static final String COMMAND_PAGES_SWAP = COMMAND_PAGES + ".swap";
    public static final String COMMAND_PAGES_SWITCH = COMMAND_PAGES + ".switch";
    public static final String COMMAND_PAGES_ACTIONS = COMMAND_PAGES + ".actions";
    public static final String COMMAND_PAGES_CLEAR_ACTIONS = COMMAND_PAGES + ".clearactions";
    public static final String COMMAND_PAGES_ADD_ACTION = COMMAND_PAGES + ".addactions";
    public static final String COMMAND_PAGES_REMOVE_ACTION = COMMAND_PAGES + ".removeaction";

    // -- Line Commands

    public static final String COMMAND_LINES = COMMANDS + ".lines";
    public static final String COMMAND_LINES_ADD = COMMAND_LINES + ".add";
    public static final String COMMAND_LINES_ALIGN = COMMAND_LINES + ".align";
    public static final String COMMAND_LINES_EDIT = COMMAND_LINES + ".edit";
    public static final String COMMAND_LINES_ADD_FLAG = COMMAND_LINES + ".addflag";
    public static final String COMMAND_LINES_REMOVE_FLAG = COMMAND_LINES + ".removeflag";
    public static final String COMMAND_LINES_HEIGHT = COMMAND_LINES + ".height";
    public static final String COMMAND_LINES_HELP = COMMAND_LINES + ".help";
    public static final String COMMAND_LINES_INFO = COMMAND_LINES + ".info";
    public static final String COMMAND_LINES_INSERT = COMMAND_LINES + ".insert";
    public static final String COMMAND_LINES_OFFSET_X = COMMAND_LINES + ".offsetx";
    public static final String COMMAND_LINES_OFFSET_Z = COMMAND_LINES + ".offsetz";
    public static final String COMMAND_LINES_SET_PERMISSION = COMMAND_LINES + ".setpermission";
    public static final String COMMAND_LINES_REMOVE = COMMAND_LINES + ".remove";
    public static final String COMMAND_LINES_SET = COMMAND_LINES + ".set";
    public static final String COMMAND_LINES_SWAP = COMMAND_LINES + ".swap";
    public static final String COMMAND_LINES_FACING = COMMAND_LINES + ".setfacing";

    // -- Feature Commands

    public static final String COMMAND_FEATURES = COMMANDS + ".features";
    public static final String COMMAND_FEATURES_DISABLE = COMMAND_FEATURES + ".disable";
    public static final String COMMAND_FEATURES_ENABLE = COMMAND_FEATURES + ".enable";
    public static final String COMMAND_FEATURES_HELP = COMMAND_FEATURES + ".help";
    public static final String COMMAND_FEATURES_INFO = COMMAND_FEATURES + ".info";
    public static final String COMMAND_FEATURES_LIST = COMMAND_FEATURES + ".list";
    public static final String COMMAND_FEATURES_RELOAD = COMMAND_FEATURES + ".reload";

    // -- Display Commands

    public static final String COMMAND_DISPLAYS = COMMANDS + ".displays";
    public static final String COMMAND_DISPLAYS_ATTRIBUTE = COMMAND_DISPLAYS + ".attribute";
    public static final String COMMAND_DISPLAYS_LIST_ATTRIBUTES = COMMAND_DISPLAYS + ".listattributes";
    public static final String COMMAND_DISPLAYS_RESET_ATTRIBUTES = COMMAND_DISPLAYS + ".resetattribute";
    public static final String COMMAND_DISPLAYS_SET_BLOCK = COMMAND_DISPLAYS + ".setblock";
    public static final String COMMAND_DISPLAYS_CENTER = COMMAND_DISPLAYS + ".center";
    public static final String COMMAND_DISPLAYS_CLONE = COMMAND_DISPLAYS + ".clone";
    public static final String COMMAND_DISPLAYS_CREATE = COMMAND_DISPLAYS + ".create";
    public static final String COMMAND_DISPLAYS_DELETE = COMMAND_DISPLAYS + ".delete";
    public static final String COMMAND_DISPLAYS_DISABLE = COMMAND_DISPLAYS + ".disable";
    public static final String COMMAND_DISPLAYS_DISPLAY_RANGE = COMMAND_DISPLAYS + ".displayrange";
    public static final String COMMAND_DISPLAYS_HELP = COMMAND_DISPLAYS + ".help";
    public static final String COMMAND_DISPLAYS_ENABLE = COMMAND_DISPLAYS + ".enable";
    public static final String COMMAND_DISPLAYS_FACING = COMMAND_DISPLAYS + ".facing";
    public static final String COMMAND_DISPLAYS_SET_ITEM = COMMAND_DISPLAYS + ".setitem";
    public static final String COMMAND_DISPLAYS_LIST = COMMAND_DISPLAYS + ".list";
    public static final String COMMAND_DISPLAYS_MOVE = COMMAND_DISPLAYS + ".move";
    public static final String COMMAND_DISPLAYS_MOVE_HERE = COMMAND_DISPLAYS + ".movehere";
    public static final String COMMAND_DISPLAYS_NEARBY = COMMAND_DISPLAYS + ".nearby";
    public static final String COMMAND_DISPLAYS_RENAME = COMMAND_DISPLAYS + ".rename";
    public static final String COMMAND_DISPLAYS_TELEPORT = COMMAND_DISPLAYS + ".teleport";
    private static final String COMMAND_DISPLAYS_TEXT = COMMAND_DISPLAYS + ".text";
    public static final String COMMAND_DISPLAYS_TEXT_ADD_LINE = COMMAND_DISPLAYS_TEXT + ".addline";
    public static final String COMMAND_DISPLAYS_TEXT_INSERT_LINE = COMMAND_DISPLAYS_TEXT + ".insertline";
    public static final String COMMAND_DISPLAYS_TEXT_REMOVE_LINE = COMMAND_DISPLAYS_TEXT + ".removeline";
    public static final String COMMAND_DISPLAYS_TEXT_SET_LINE = COMMAND_DISPLAYS_TEXT + ".setline";
    public static final String COMMAND_DISPLAYS_TEXT_SWAP_LINES = COMMAND_DISPLAYS_TEXT + ".swaplines";
    public static final String COMMAND_DISPLAYS_UPDATE_INTERVAL = COMMAND_DISPLAYS + ".updateinterval";

    // -- Profiler Commands

    public static final String COMMAND_PROFILER = COMMANDS + ".profiler";
    public static final String COMMAND_PROFILER_HELP = COMMAND_PROFILER + ".help";
    public static final String COMMAND_PROFILER_RESET = COMMAND_PROFILER + ".reset";
    public static final String COMMAND_PROFILER_START = COMMAND_PROFILER + ".start";
    public static final String COMMAND_PROFILER_STATS = COMMAND_PROFILER + ".stats";
    public static final String COMMAND_PROFILER_STOP = COMMAND_PROFILER + ".stop";

}
