package eu.decentsoftware.holograms.api.holograms;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.holograms.objects.FlagHolder;
import eu.decentsoftware.holograms.api.nms.NMS;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class HologramPage extends FlagHolder {

    /*
     *	Fields
     */

    private int index;
    private final @NonNull Hologram parent;
    private final @NonNull List<Integer> clickableEntityIds;
    private final @NonNull List<HologramLine> lines;
    private final @NonNull Map<ClickType, List<Action>> actions;
    protected boolean alwaysFacePlayer;

    /*
     *	Constructors
     */

    public HologramPage(@NonNull Hologram parent, int index) {
        this.parent = parent;
        this.index = index;
        this.clickableEntityIds = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.actions = new EnumMap<>(ClickType.class);
        this.alwaysFacePlayer = parent.isAlwaysFacePlayer();
    }

    /*
     *	General Methods
     */

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasOffsets() {
        return true; // Not used, just ignore.
    }

    /**
     * Get the current parent hologram of this page.
     *
     * @return the current parent hologram of this page.
     */
    @NonNull
    public Hologram getParent() {
        return parent;
    }

    /**
     * Get height of this hologram in blocks.
     *
     * @return height of this hologram in blocks.
     */
    public double getHeight() {
        double height = 0.0D;
        for (HologramLine hologramLine : lines) {
            height += hologramLine.getHeight();
        }
        return height;
    }

    public int getClickableEntityId(int index) {
        if (index >= clickableEntityIds.size()) {
            clickableEntityIds.add(NMS.getInstance().getFreeEntityId());
        }
        return clickableEntityIds.get(index);
    }

    @NonNull
    public Location getCenter() {
        Location center = parent.getLocation().clone();
        if (parent.isDownOrigin()) {
            center.add(0, getHeight() / 2, 0);
        } else {
            center.subtract(0, getHeight() / 2, 0);
        }
        return center;
    }

    /**
     * Get hologram size. (Number of lines)
     *
     * @return Number of lines in this hologram.
     */
    public int size() {
        return this.lines.size();
    }

    @NonNull
    public Map<String, Object> serializeToMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        List<Map<String, Object>> linesMap = new ArrayList<>();
        for (int i = 1; i <= this.lines.size(); i++) {
            HologramLine line = this.lines.get(i - 1);
            linesMap.add(line.serializeToMap());
        }
        map.put("lines", linesMap);
        Map<String, List<String>> actionsMap = new LinkedHashMap<>();
        for (Map.Entry<ClickType, List<Action>> entry : this.getActions().entrySet()) {
            actionsMap.put(entry.getKey().name(), entry.getValue().stream().map(Action::toString).collect(Collectors.toList()));
        }
        map.put("actions", actionsMap);
//        map.put("always-face-player", isAlwaysFacePlayer());
        return map;
    }

    @NonNull
    public HologramPage clone(@NonNull Hologram parent, int index) {
        HologramPage page = new HologramPage(parent, index);
        for (HologramLine line : getLines()) {
            page.addLine(line.clone(page, page.getNextLineLocation()));
        }
        for (Map.Entry<ClickType, List<Action>> entry : getActions().entrySet()) {
            for (Action action : entry.getValue()) {
                page.addAction(entry.getKey(), action);
            }
        }
        page.addFlags(this.getFlags().toArray(new EnumFlag[0]));
//        page.setAlwaysFacePlayer(isAlwaysFacePlayer());
        return page;
    }

    /*
     *	Lines Methods
     */

    /**
     * Re-Align the lines in this hologram page putting them to the right place.
     * <p>
     * This method is good to use after teleporting the hologram page.
     */
    public void realignLines() {
        Location currentLocation = parent.getLocation().clone();
        if (parent.isDownOrigin()) {
            currentLocation.add(0, getHeight(), 0);
        }

        for (HologramLine line : lines) {
            Location lineLocation = line.getLocation();
            lineLocation.setX(currentLocation.getX() + line.getOffsetX());
            lineLocation.setY(currentLocation.getY() + line.getOffsetY());
            lineLocation.setZ(currentLocation.getZ() + line.getOffsetZ());

            line.setLocation(lineLocation);
            line.updateLocation(true);
            currentLocation.subtract(0, line.getHeight(), 0);
        }
    }

    /**
     * Add a new line to the bottom of this hologram page.
     *
     * @param line New line.
     * @return Boolean whether the operation was successful.
     */
    public boolean addLine(@NonNull HologramLine line) {
        lines.add(line);
        parent.getViewerPlayers(index).forEach(line::show);
        realignLines();
        return true;
    }

    /**
     * Insert a new line into this hologram page.
     *
     * @param index Index of the new line.
     * @param line  New line.
     * @return Boolean whether the operation was successful.
     */
    public boolean insertLine(int index, @NonNull HologramLine line) {
        if (index < 0 || index >= size()) {
            return false;
        }
        lines.add(index, line);
        parent.getViewerPlayers(this.index).forEach(line::show);
        realignLines();
        return true;
    }

    /**
     * Set new content of a line in this hologram page.
     *
     * @param index   Index of the line.
     * @param content Line's new content.
     * @return Boolean whether the operation was successful.
     */
    public boolean setLine(int index, @NonNull String content) {
        if (index < 0 || index >= size()) {
            return false;
        }
        HologramLine line = getLine(index);
        line.hide();
        line.setContent(content);
        line.show();
        realignLines();
        return true;
    }

    /**
     * Get line on a specified index in this hologram page.
     *
     * @param index Index of the line.
     * @return The HologramLine or null if it wasn't found.
     */
    public HologramLine getLine(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        return lines.get(index);
    }

    /**
     * Remove a line from this hologram page.
     *
     * @param index Index of the line.
     * @return The removed line or null if it wasn't found.
     */
    public HologramLine removeLine(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }
        HologramLine line = lines.remove(index);
        if (line != null) {
            line.destroy();
            realignLines();
        }
        return line;
    }

    /**
     * Swap two lines in this hologram page.
     *
     * @param index1 First line.
     * @param index2 Second line.
     * @return Boolean whether the operation was successful.
     */
    public boolean swapLines(int index1, int index2) {
        if (index1 < 0 || index1 >= size() || index2 < 0 || index2 >= size()) {
            return false;
        }
        Collections.swap(this.lines, index1, index2);
        realignLines();
        return true;
    }

    /**
     * Get the Location at the bottom of this hologram page that's available for a new line.
     *
     * @return the Location at the bottom of this hologram page that's available for a new line.
     */
    public Location getNextLineLocation() {
        if (size() == 0) {
            return parent.getLocation().clone();
        }
        HologramLine line = lines.get(lines.size() - 1);
        return line.getLocation().clone().subtract(0, line.getHeight(), 0);
    }

    /**
     * Get the List of all lines in this hologram page.
     *
     * @return List of all lines in this hologram page.
     */
    public List<HologramLine> getLines() {
        return ImmutableList.copyOf(lines);
    }

    /*
     *  Action Methods
     */

    public boolean isClickable() {
        if (parent.hasFlag(EnumFlag.DISABLE_ACTIONS)) return false;
        for (ClickType value : ClickType.values()) {
            List<Action> list = actions.get(value);
            if (list != null && !list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEntity(int eid) {
        return clickableEntityIds.contains(eid) || lines.stream().anyMatch(line -> line.getEntityIds()[1] == eid);
    }

    public void addAction(@NonNull ClickType clickType, @NonNull Action action) {
        if (!actions.containsKey(clickType)) {
            actions.put(clickType, new ArrayList<>());
        }
        actions.get(clickType).add(action);
    }

    public void executeActions(@NonNull Player player, @NonNull ClickType clickType) {
        if (!actions.containsKey(clickType)) return;
        for (Action action : actions.get(clickType)) {
            String actionName = action.getType().getName();
            String actionData = action.getData();
            if (actionName.contains("_PAGE") && actionData == null) {
                action.setData(getParent().getName());
            } else if (actionName.equals("PAGE") && actionData.matches("[0-9]+")) {
                action.setData(getParent().getName() + ":" + actionData);
            }

            if (!action.execute(player)) {
                action.setData(actionData);
                return;
            }
            action.setData(actionData);
        }
    }

    public void clearActions() {
        actions.clear();
    }

    public void clearActions(@NonNull ClickType clickType) {
        actions.remove(clickType);
    }

    public Action removeAction(@NonNull ClickType clickType, int index) {
        return actions.get(clickType).remove(index);
    }

    public List<Action> getActions(@NonNull ClickType clickType) {
        if (!actions.containsKey(clickType)) {
            return new ArrayList<>();
        }
        return actions.get(clickType);
    }

}
