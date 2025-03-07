package eu.decentsoftware.holograms.api.holograms;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.holograms.enums.HologramLineType;
import eu.decentsoftware.holograms.api.holograms.objects.FlagHolder;
import eu.decentsoftware.holograms.nms.api.renderer.NmsClickableHologramRenderer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class HologramPage extends FlagHolder {

    /*
     *	Fields
     */

    private int index;
    private final Hologram parent;
    private final List<NmsClickableHologramRenderer> clickableEntityRenderers = new ArrayList<>();
    private final List<HologramLine> lines = new ArrayList<>();
    private final Map<ClickType, List<Action>> actions = new EnumMap<>(ClickType.class);

    /*
     *	Constructors
     */

    public HologramPage(@NonNull Hologram parent, int index) {
        this.parent = parent;
        this.index = index;
    }

    /*
     *	General Methods
     */

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
     * @see DHAPI#addHologramLine(HologramPage, String)
     */
    public boolean addLine(@NonNull HologramLine line) {
        return insertLine(size(), line);
    }

    /**
     * Insert a new line into this hologram page.
     *
     * @param index Index of the new line.
     * @param line  New line.
     * @return Boolean whether the operation was successful.
     * @see DHAPI#insertHologramLine(Hologram, int, String)
     */
    public boolean insertLine(int index, @NonNull HologramLine line) {
        if (index < 0 || index > size()) {
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
     * @see DHAPI#setHologramLine(HologramPage, int, String)
     */
    public boolean setLine(int index, @NonNull String content) {
        HologramLine line = getLine(index);
        if (line == null) {
            return false;
        }

        HologramLineType previousType = line.getType();

        line.setContent(content);

        if (line.getType() != previousType || line.getType() == HologramLineType.ENTITY) {
            line.hide();
            line.show();
            realignLines();
        }
        return true;
    }

    /**
     * Get line on a specified index in this hologram page.
     *
     * @param index Index of the line.
     * @return The HologramLine or null if it wasn't found.
     * @see DHAPI#getHologramLine(HologramPage, int)
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
     * @see DHAPI#removeHologramLine(HologramPage, int)
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
    @NonNull
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
    @NonNull
    public List<HologramLine> getLines() {
        return ImmutableList.copyOf(lines);
    }

    /*
     *  Action Methods
     */

    public boolean isClickable() {
        return !parent.hasFlag(EnumFlag.DISABLE_ACTIONS) && hasActions();
    }

    /**
     * @deprecated For removal.
     */
    @Deprecated
    public int getClickableEntityId(int index) {
        return getClickableRenderer(index).getEntityId();
    }

    NmsClickableHologramRenderer getClickableRenderer(int index) {
        if (index >= clickableEntityRenderers.size()) {
            clickableEntityRenderers.add(DecentHologramsAPI.get()
                    .getNmsAdapter()
                    .getHologramComponentFactory()
                    .createClickableRenderer());
        }
        return clickableEntityRenderers.get(index);
    }

    public boolean hasEntity(final int eid) {
        for (NmsClickableHologramRenderer clickableEntityRenderer : clickableEntityRenderers) {
            if (clickableEntityRenderer.getEntityId() == eid) {
                return true;
            }
        }
        for (HologramLine line : lines) {
            for (int entityId : line.getEntityIds()) {
                if (entityId == eid) {
                    return true;
                }
            }
        }
        return false;
    }

    public void addAction(@NonNull ClickType clickType, @NonNull Action action) {
        actions.computeIfAbsent(clickType, k -> new ArrayList<>()).add(action);
    }

    public void executeActions(@NonNull Player player, @NonNull ClickType clickType) {
        if (!actions.containsKey(clickType)) return;
        for (Action action : actions.get(clickType)) {
            String actionName = action.getType().getName();
            String actionData = action.getData();
            if (actionName.contains("_PAGE") && actionData == null) {
                action.setData(getParent().getName());
            } else if (actionName.equals("PAGE") && actionData != null && actionData.matches("\\d+")) {
                action.setData(getParent().getName() + ":" + actionData);
            }

            if (!action.execute(player)) {
                action.setData(actionData);
                return;
            }
            action.setData(actionData);
        }
    }

    public void clearActions(@NonNull ClickType clickType) {
        actions.remove(clickType);
    }

    public void removeAction(@NonNull ClickType clickType, int index) {
        actions.get(clickType).remove(index);
    }

    public List<Action> getActions(@NonNull ClickType clickType) {
        if (!actions.containsKey(clickType)) {
            return new ArrayList<>();
        }
        return actions.get(clickType);
    }

    /**
     * Check if this page has any actions.
     *
     * @return True if this page has any actions, false otherwise.
     */
    public boolean hasActions() {
        for (ClickType value : ClickType.values()) {
            List<Action> list = actions.get(value);
            if (list != null && !list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
