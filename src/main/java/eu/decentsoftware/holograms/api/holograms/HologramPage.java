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
import lombok.experimental.FieldNameConstants;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Getter
@Setter
public class HologramPage extends FlagHolder {

    /*
     *	Fields
     */

    private int index;
    private final Hologram parent;
    private final List<Integer> clickableEntityIds;
    private final List<HologramLine> lines;
    private final Map<ClickType, List<Action>> actions;
    private Location location;
    protected boolean alwaysFacePlayer;

    @FieldNameConstants.Exclude
    protected final AtomicBoolean hasOffsets;

    /*
     *	Constructors
     */

    public HologramPage(@Nonnull Hologram parent, int index) {
        this.location = parent.getLocation();
        this.parent = parent;
        this.index = index;
        this.clickableEntityIds = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.actions = new EnumMap<>(ClickType.class);
        this.hasOffsets = new AtomicBoolean(false);
        this.alwaysFacePlayer = parent.isAlwaysFacePlayer();
    }

    /*
     *	General Methods
     */

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasOffsets() {
        return hasOffsets.get();
    }

    public void updateHasOffsets() {
        for (HologramLine hLine : getLines()) {
            if (hLine.hasOffsets()) {
                hasOffsets.set(true);
                return;
            }
        }
        hasOffsets.set(false);
    }

    /**
     * Get the current parent hologram of this page.
     *
     * @return the current parent hologram of this page.
     */
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

    public Location getCenter() {
        Location center = getLocation().clone();
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

    public HologramPage clone(Hologram parent, int index) {
        HologramPage page = new HologramPage(parent, index);
        for (HologramLine line : getLines()) {
            page.addLine(line.clone(page.getNextLineLocation()));
        }
        for (Map.Entry<ClickType, List<Action>> entry : getActions().entrySet()) {
            for (Action action : entry.getValue()) {
                page.addAction(entry.getKey(), action);
            }
        }
        page.setAlwaysFacePlayer(isAlwaysFacePlayer());
        return page;
    }

    /*
     *	Lines Methods
     */

    /**
     * Re-Align the lines in this hologram page putting them to the right place.
     * <p>
     *     This method is good to use after teleporting the hologram page.
     * </p>
     */
    public void realignLines() {
        Location currentLocation = getLocation().clone();
        if (parent.isDownOrigin()) {
            currentLocation.add(0, getHeight(), 0);
        }

        for (HologramLine line : lines) {
            line.setLocation(currentLocation.clone().add(line.getOffsetX(), line.getOffsetY(), line.getOffsetZ()));
            line.updateLocation(true);
            if (line.hasOffsets() && !hasOffsets()) {
                hasOffsets.set(true);
            }
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
     * @param line New line.
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
     * @param index Index of the line.
     * @param content Line's new content.
     * @return Boolean whether the operation was successful.
     */
    public boolean setLine(int index, String content) {
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
     * This will replace the content of all lines and add
     * new lines if there's more new lines than actual lines
     * or if there is a difference between line contents.
     *
     * @param lines {@link List} of {@link String}
     * @return If it was successful or not
     */
    public boolean replaceLines(List<String> lines) {
        // Validating if there is a difference between the line count
        boolean changed = false;
        LinkedList<HologramLine> toAdd = new LinkedList<>();
        for (int i = 0; i < lines.size(); i++) {
            // Validator to validate if the line is new or not
            // If the line is new add it to the toAdd list.
            boolean isLineNew = false;
            HologramLine line;
            try {
                // Attempting to get the old line from the i index
                line = this.lines.get(i);
            } catch (IndexOutOfBoundsException exception) {
                // Line does not exist, make a new line
                isLineNew = true;
                line = new HologramLine(this, location, lines.get(i));
            }

            // The line is validated to be new, so add it to the toAdd list
            if (isLineNew) {
                changed = true;
                toAdd.add(line);
                continue;
            }

            // We do not need to try-catch because we already know the index size
            // of the new lines, and we know it will not be greater than that.
            String newLine = lines.get(i);
            String lineContent = line.getContent();

            // Comparing both of the lines to see if we need to change the content
            // This way of comparing strings could possibly be improved, I don't know.
            boolean isDifferent = lineContent.equalsIgnoreCase(newLine);

            // If the content is different, update the content
            // If the content is not different, do nothing.
            if (isDifferent) {
                line.setContent(newLine);

                // Validating that the content has been changed at least once
                // which will be used in the return value
                if (!changed)
                    changed = true;
            }
        }

        toAdd.forEach(this::addLine);
        return changed;
    }

    /**
     * This will replace the content of all lines and add
     * new lines if there's more new lines than actual lines
     * or if there is a difference between line contents.
     *
     * @param lines {@link String} array
     * @return If it was successful or not
     */
    public boolean replaceLines(String... lines) {
        return replaceLines(Arrays.asList(lines));
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
     * Clears all the lines
     */
    public void clearLines() {
        getLines().forEach(line -> {
            line.destroy();
            line.delete();
        });
        lines.clear();
        realignLines();
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
            return getLocation().clone();
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

    public void addAction(ClickType clickType, Action action) {
        if (!actions.containsKey(clickType)) {
            actions.put(clickType, new ArrayList<>());
        }
        actions.get(clickType).add(action);
    }

    public void executeActions(Player player, ClickType clickType) {
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

    public void clearActions(ClickType clickType) {
        actions.remove(clickType);
    }

    public Action removeAction(ClickType clickType, int index) {
        return actions.get(clickType).remove(index);
    }

    public List<Action> getActions(ClickType clickType) {
        if (!actions.containsKey(clickType)) {
            return new ArrayList<>();
        }
        return actions.get(clickType);
    }

}
