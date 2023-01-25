package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.api.holograms.enums.HologramLineType;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple access point to the DecentHolograms API. Using this class
 * you can easily manipulate with holograms and their contents.
 *
 * @author d0by
 * @since 2.0.12
 */
public final class DHAPI {

    /**
     * Create a new hologram with the given name on the specified location.
     *
     * @param name     The name.
     * @param location The location.
     * @return The new hologram.
     * @throws IllegalArgumentException If name or location is null or hologram with the given name already exists.
     */
    public static Hologram createHologram(String name, Location location) throws IllegalArgumentException {
        return createHologram(name, location, false);
    }

    /**
     * Create a new hologram with the given name on the specified location.
     *
     * @param name       The name.
     * @param location   The location.
     * @param saveToFile Boolean: Should the hologram be saved into file?
     * @return The new hologram.
     * @throws IllegalArgumentException If name or location is null or hologram with the given name already exists.
     */
    public static Hologram createHologram(String name, Location location, boolean saveToFile) throws IllegalArgumentException {
        return createHologram(name, location, saveToFile, new ArrayList<>());
    }

    /**
     * Create a new hologram with the given name on the specified location with the given lines.
     *
     * @param name     The name.
     * @param location The location.
     * @param lines    The lines of this hologram.
     * @return The new hologram.
     * @throws IllegalArgumentException If name or location is null or hologram with the given name already exists.
     */
    public static Hologram createHologram(String name, Location location, List<String> lines) throws IllegalArgumentException {
        return createHologram(name, location, false, lines);
    }

    /**
     * Create a new hologram with the given name on the specified location with the given lines.
     *
     * @param name       The name.
     * @param location   The location.
     * @param saveToFile Boolean: Should the hologram be saved into file?
     * @param lines      The lines of this hologram.
     * @return The new hologram.
     * @throws IllegalArgumentException If name or location is null or hologram with the given name already exists.
     */
    public static Hologram createHologram(String name, Location location, boolean saveToFile, List<String> lines) throws IllegalArgumentException {
        Validate.notNull(name);
        Validate.notNull(location);

        if (Hologram.getCachedHologramNames().contains(name)) {
            throw new IllegalArgumentException(String.format("Hologram with that name already exists! (%s)", name));
        }
        Hologram hologram = new Hologram(name, location, saveToFile);
        HologramPage page = hologram.getPage(0);
        if (lines != null) {
            for (String line : lines) {
                HologramLine hologramLine = new HologramLine(page, page.getNextLineLocation(), line);
                page.addLine(hologramLine);
            }
        }
        hologram.showAll();
        hologram.save();
        return hologram;
    }

    /**
     * Move a hologram to the given location.
     *
     * @param name     The holograms name.
     * @param location The location.
     * @throws IllegalArgumentException If hologram or location is null.
     */
    public static void moveHologram(String name, Location location) throws IllegalArgumentException {
        Validate.notNull(name);
        Validate.notNull(location);

        Hologram hologram = getHologram(name);
        moveHologram(hologram, location);
    }

    /**
     * Move a hologram to the given location.
     *
     * @param hologram The hologram.
     * @param location The location.
     * @throws IllegalArgumentException If hologram or location is null.
     */
    public static void moveHologram(Hologram hologram, Location location) throws IllegalArgumentException {
        Validate.notNull(hologram);
        Validate.notNull(location);

        Location hologramLocation = hologram.getLocation();
        hologramLocation.setWorld(location.getWorld());
        hologramLocation.setX(location.getX());
        hologramLocation.setY(location.getY());
        hologramLocation.setZ(location.getZ());
        hologram.setLocation(hologramLocation);
        hologram.realignLines();
        hologram.save();
    }

    /**
     * Update the given hologram for all viewers.
     *
     * @param name The holograms name.
     */
    public static void updateHologram(String name) {
        Validate.notNull(name);

        Hologram hologram = getHologram(name);
        hologram.updateAll();
    }

    /**
     * Remove a hologram by its name.
     * <p>
     * The removed hologram will also get its file deleted.
     * </p>
     *
     * @param name The name.
     */
    public static void removeHologram(String name) {
        Validate.notNull(name);

        Hologram hologram = getHologram(name);
        hologram.delete();
    }

    /**
     * Create a new hologram line with the given parent page on the specified location with the given content.
     *
     * @param parent  The parent page.
     * @param content The content.
     * @return The new hologram line.
     * @throws IllegalArgumentException If any of the arguments is null.
     */
    public static HologramLine createHologramLine(HologramPage parent, String content) throws IllegalArgumentException {
        Validate.notNull(parent);
        Validate.notNull(content);
        return new HologramLine(parent, parent.getNextLineLocation(), content);
    }

    /**
     * Create a new hologram line with the given parent page on the specified location with the given content.
     *
     * @param parent   The parent page.
     * @param location The location.
     * @param content  The content.
     * @return The new hologram line.
     * @throws IllegalArgumentException If any of the arguments is null.
     */
    public static HologramLine createHologramLine(HologramPage parent, Location location, String content) throws IllegalArgumentException {
        Validate.notNull(parent);
        Validate.notNull(location);
        Validate.notNull(content);
        return new HologramLine(parent, location, content);
    }

    /**
     * Add a new page into hologram.
     *
     * @param hologram The hologram.
     * @return The new page.
     * @throws IllegalArgumentException If hologram is null.
     */
    public static HologramPage addHologramPage(Hologram hologram) throws IllegalArgumentException {
        return addHologramPage(hologram, null);
    }

    /**
     * Add a new page into hologram.
     *
     * @param hologram The hologram.
     * @param lines    New pages lines.
     * @return The new page.
     * @throws IllegalArgumentException If hologram is null.
     */
    public static HologramPage addHologramPage(Hologram hologram, List<String> lines) throws IllegalArgumentException {
        Validate.notNull(hologram);
        HologramPage page = hologram.addPage();
        if (lines != null && !lines.isEmpty()) {
            for (String content : lines) {
                HologramLine line = new HologramLine(page, page.getNextLineLocation(), content);
                page.addLine(line);
            }
        }
        hologram.save();
        return page;
    }

    /**
     * Insert a new hologram page on the specified index into hologram.
     *
     * @param hologram The hologram.
     * @param index    The index.
     * @return The new page.
     * @throws IllegalArgumentException If hologram is null or the index is out of bounds.
     */
    public static HologramPage insertHologramPage(Hologram hologram, int index) throws IllegalArgumentException {
        return insertHologramPage(hologram, index, null);
    }

    /**
     * Insert a new hologram page on the specified index into hologram.
     *
     * @param hologram The hologram.
     * @param index    The index. Index starts at 1.
     * @param lines    New pages lines.
     * @return The new page.
     * @throws IllegalArgumentException If hologram is null or the index is out of bounds.
     */
    public static HologramPage insertHologramPage(Hologram hologram, int index, List<String> lines) throws IllegalArgumentException {
        Validate.notNull(hologram);
        HologramPage page = hologram.insertPage(index);
        if (page == null) {
            throw new IllegalArgumentException("Given page index is out of bounds for the hologram.");
        }
        if (lines != null && !lines.isEmpty()) {
            for (String content : lines) {
                HologramLine line = new HologramLine(page, page.getNextLineLocation(), content);
                page.addLine(line);
            }
        }
        hologram.save();
        return page;
    }

    /**
     * Remove a page from hologram.
     *
     * @param hologram The hologram.
     * @param index    Index of the page.
     * @return The removed page.
     * @throws IllegalArgumentException If hologram is null;
     */
    @Nullable
    public static HologramPage removeHologramPage(Hologram hologram, int index) throws IllegalArgumentException {
        Validate.notNull(hologram);
        HologramPage page = hologram.removePage(index);
        hologram.save();
        return page;
    }

    /**
     * Get hologram by name.
     *
     * @param name The name.
     * @return The hologram.
     * @throws IllegalArgumentException If the name is null.
     */
    @Nullable
    public static Hologram getHologram(String name) throws IllegalArgumentException {
        Validate.notNull(name);
        return Hologram.getCachedHologram(name);
    }

    /**
     * Get hologram page by index.
     *
     * @param hologram The hologram.
     * @param index    The index.
     * @return The hologram page.
     * @throws IllegalArgumentException If the hologram is null or the indexes are invalid.
     */
    @Nullable
    public static HologramPage getHologramPage(Hologram hologram, int index) throws IllegalArgumentException {
        Validate.notNull(hologram);
        return hologram.getPage(index);
    }

    /**
     * Get hologram line by index.
     *
     * @param page  The parent page.
     * @param index The index.
     * @return The hologram line.
     * @throws IllegalArgumentException If the page is null or the indexes are invalid.
     */
    @Nullable
    public static HologramLine getHologramLine(HologramPage page, int index) throws IllegalArgumentException {
        Validate.notNull(page);
        return page.getLine(index);
    }

    /**
     * Add a new line with an item into hologram.
     *
     * @param hologram The hologram.
     * @param material Material for new line content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or material is null
     */
    public static HologramLine addHologramLine(Hologram hologram, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        return addHologramLine(hologram, new ItemStack(material));
    }

    /**
     * Add an ItemStack as new line into hologram.
     *
     * @param hologram The hologram.
     * @param item     ItemStack for new line content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or item is null
     */
    public static HologramLine addHologramLine(Hologram hologram, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        return addHologramLine(hologram, "#ICON:" + HologramItem.fromItemStack(item).getContent());
    }

    /**
     * Add a new line into hologram.
     *
     * @param hologram The hologram.
     * @param content  New lines content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or content is null.
     */
    public static HologramLine addHologramLine(Hologram hologram, String content) throws IllegalArgumentException {
        return addHologramLine(hologram, 0, content);
    }

    /**
     * Add a new line into hologram page.
     *
     * @param hologram  The hologram.
     * @param pageIndex Index of the page.
     * @param content   New lines content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or content is null or the indexes are invalid.
     */
    public static HologramLine addHologramLine(Hologram hologram, int pageIndex, String content) throws IllegalArgumentException {
        Validate.notNull(hologram);
        Validate.notNull(content);
        HologramPage page = hologram.getPage(pageIndex);
        if (page == null) {
            throw new IllegalArgumentException("Given page index is out of bounds for the hologram.");
        }
        return addHologramLine(page, content);
    }

    /**
     * Add a Material as a new line into hologram page.
     *
     * @param page     The page.
     * @param material Material for new line content.
     * @return The new line.
     * @throws IllegalArgumentException If page or material is null
     */
    public static HologramLine addHologramLine(HologramPage page, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        return addHologramLine(page, new ItemStack(material));
    }

    /**
     * Add an ItemStack as a new line into hologram page.
     *
     * @param page The page.
     * @param item ItemStack for new line content.
     * @return The new line.
     * @throws IllegalArgumentException If page or item is null
     */
    public static HologramLine addHologramLine(HologramPage page, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        return addHologramLine(page, "#ICON:" + HologramItem.fromItemStack(item).getContent());
    }

    /**
     * Add a new line into hologram page.
     *
     * @param page    The page.
     * @param content New lines content.
     * @return The new line.
     * @throws IllegalArgumentException If page or content is null.
     */
    public static HologramLine addHologramLine(HologramPage page, String content) throws IllegalArgumentException {
        HologramLine line = new HologramLine(page, page.getNextLineLocation(), content);
        page.addLine(line);
        page.getParent().save();
        return line;
    }

    /**
     * Insert a Material as new line on the specified index into hologram page.
     *
     * @param hologram  The hologram.
     * @param lineIndex Index of the new line.
     * @param material  Material for new item line content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or material is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(Hologram hologram, int lineIndex, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        return insertHologramLine(hologram, lineIndex, new ItemStack(material));
    }

    /**
     * Insert an ItemStack as new line on the specified index into hologram page.
     *
     * @param hologram  The hologram.
     * @param lineIndex Index of the new line.
     * @param item      ItemStack for new item line content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or item is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(Hologram hologram, int lineIndex, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        return insertHologramLine(hologram, 0, lineIndex, item);
    }

    /**
     * Insert a new line on the specified index into hologram page.
     *
     * @param hologram  The hologram.
     * @param lineIndex Index of the new line.
     * @param content   New lines content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or content is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(Hologram hologram, int lineIndex, String content) throws IllegalArgumentException {
        return insertHologramLine(hologram, 0, lineIndex, content);
    }

    /**
     * Insert a Material as new line on the specified index into hologram page.
     *
     * @param hologram  The hologram.
     * @param pageIndex Index of the hologram page.
     * @param lineIndex Index of the new line.
     * @param material  Material for the new line content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or material is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(Hologram hologram, int pageIndex, int lineIndex, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        return insertHologramLine(hologram, pageIndex, lineIndex, new ItemStack(material));
    }

    /**
     * Insert an ItemStack as new line on the specified index into hologram page.
     *
     * @param hologram  The hologram.
     * @param pageIndex Index of the hologram page.
     * @param lineIndex Index of the new line.
     * @param item      ItemStack for the new line content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or item is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(Hologram hologram, int pageIndex, int lineIndex, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        return insertHologramLine(hologram, pageIndex, lineIndex, "#ICON:" + HologramItem.fromItemStack(item).getContent());
    }

    /**
     * Insert a new line on the specified index into hologram page.
     *
     * @param hologram  The hologram.
     * @param pageIndex Index of the hologram page.
     * @param lineIndex Index of the new line.
     * @param content   New lines content.
     * @return The new line.
     * @throws IllegalArgumentException If hologram or content is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(Hologram hologram, int pageIndex, int lineIndex, String content) throws IllegalArgumentException {
        Validate.notNull(hologram);
        HologramPage page = hologram.getPage(pageIndex);
        if (page == null) {
            throw new IllegalArgumentException("Given page index is out of bounds for the hologram.");
        }
        return insertHologramLine(page, lineIndex, content);
    }

    /**
     * Insert a Material as new line on the specified index into hologram page.
     *
     * @param page     The page.
     * @param index    Index of the new line.
     * @param material Material as new line content.
     * @return The new line.
     * @throws IllegalArgumentException If page or material is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(HologramPage page, int index, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        return insertHologramLine(page, index, new ItemStack(material));
    }

    /**
     * Insert an ItemStack as new line on the specified index into hologram page.
     *
     * @param page  The page.
     * @param index Index of the new line.
     * @param item  ItemStack as new line content.
     * @return The new line.
     * @throws IllegalArgumentException If page or item is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(HologramPage page, int index, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        return insertHologramLine(page, index, "#ICON:" + HologramItem.fromItemStack(item).getContent());
    }

    /**
     * Insert a new line on the specified index into hologram page.
     *
     * @param page    The page.
     * @param index   Index of the new line.
     * @param content New lines content.
     * @return The new line.
     * @throws IllegalArgumentException If page or content is null or the indexes are invalid.
     */
    public static HologramLine insertHologramLine(HologramPage page, int index, String content) throws IllegalArgumentException {
        HologramLine oldLine = page.getLine(index);
        if (oldLine == null) {
            throw new IllegalArgumentException("Given line index is out of bounds for the hologram page.");
        }
        HologramLine line = new HologramLine(page, oldLine.getLocation(), content);
        page.insertLine(index, line);
        page.getParent().save();
        return line;
    }

    /**
     * Set a material as new content to a hologram line and update it.
     *
     * @param line     The line.
     * @param material Material to set as new content.
     * @throws IllegalArgumentException If any of the arguments are null.
     */
    public static void setHologramLine(HologramLine line, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        setHologramLine(line, new ItemStack(material));
    }

    /**
     * Set an ItemStack as new content to a hologram line and update it.
     *
     * @param line The line.
     * @param item ItemStack to set as new content.
     * @throws IllegalArgumentException If any of the arguments are null
     */
    public static void setHologramLine(HologramLine line, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        setHologramLine(line, "#ICON:" + HologramItem.fromItemStack(item).getContent());
    }

    /**
     * Set a new content to hologram line and update it.
     *
     * @param line    The line.
     * @param content The new content.
     * @throws IllegalArgumentException If any of the arguments is null.
     */
    public static void setHologramLine(HologramLine line, String content) throws IllegalArgumentException {
        Validate.notNull(line);
        Validate.notNull(content);

        // If the new content is the same as current content, don't do anyting.
        if (line.getContent().equals(content)) {
            return;
        }

        HologramLineType prevType = line.getType();
        HologramPage parent = line.getParent();

        // Set the new content
        line.setContent(content);

        // If the new type is the same as the previous type, just update.
        if ((prevType == HologramLineType.TEXT || prevType == HologramLineType.HEAD || prevType == HologramLineType.SMALLHEAD) && prevType == line.getType()) {
            line.update();
        }

        // If the type changed, respawn the line.
        else {
            Player[] viewers = line.getViewerPlayers().toArray(new Player[0]);
            line.hide();
            line.show(viewers);

            // Realign lines in case the heights changed.
            if (parent != null) {
                parent.realignLines();
            }
        }

        // Finally, save the changes.
        if (parent != null) {
            parent.getParent().save();
        }
    }

    /**
     * Set a Material as new content to hologram line and update it.
     *
     * @param page      The parent page.
     * @param lineIndex The index of the line.
     * @param material  Material for new content.
     * @throws IllegalArgumentException If any of the arguments are null or the indexes are invalid.
     */
    public static void setHologramLine(HologramPage page, int lineIndex, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        setHologramLine(page, lineIndex, new ItemStack(material));
    }

    /**
     * Set an ItemStack as new content to hologram line and update it.
     *
     * @param page      The parent page.
     * @param lineIndex The index of the line.
     * @param item      ItemStack for new content.
     * @throws IllegalArgumentException If any of the arguments are null or the indexes are invalid.
     */
    public static void setHologramLine(HologramPage page, int lineIndex, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        setHologramLine(page, lineIndex, "#ICON:" + HologramItem.fromItemStack(item).getContent());
    }

    /**
     * Set a new content to hologram line and update it.
     *
     * @param page      The parent page.
     * @param lineIndex The index of the line.
     * @param content   The new content.
     * @throws IllegalArgumentException If any of the arguments is null or the indexes are invalid.
     */
    public static void setHologramLine(HologramPage page, int lineIndex, String content) throws IllegalArgumentException {
        Validate.notNull(page);
        Validate.notNull(content);
        HologramLine line = page.getLine(lineIndex);
        if (line == null) {
            throw new IllegalArgumentException("Given line index is out of bounds for the hologram page.");
        }
        setHologramLine(line, content);
    }

    /**
     * Set a Material as new content to hologram line and update it.
     *
     * @param hologram  The parent hologram.
     * @param lineIndex The index of the line.
     * @param material  Material for new content.
     * @throws IllegalArgumentException If any of the arguments is null or the indexes are invalid.
     */
    public static void setHologramLine(Hologram hologram, int lineIndex, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        setHologramLine(hologram, lineIndex, new ItemStack(material));
    }

    /**
     * Set a ItemStack as new content to hologram line and update it.
     *
     * @param hologram  The parent hologram.
     * @param lineIndex The index of the line.
     * @param item      ItemStack for new content.
     * @throws IllegalArgumentException If any of the arguments is null or the indexes are invalid.
     */
    public static void setHologramLine(Hologram hologram, int lineIndex, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        setHologramLine(hologram, lineIndex, "#ICON:" + HologramItem.fromItemStack(item).getContent());
    }

    /**
     * Set a new content to hologram line and update it.
     *
     * @param hologram  The parent hologram.
     * @param lineIndex The index of the line.
     * @param content   The new content.
     * @throws IllegalArgumentException If any of the arguments is null or the indexes are invalid.
     */
    public static void setHologramLine(Hologram hologram, int lineIndex, String content) throws IllegalArgumentException {
        setHologramLine(hologram, 0, lineIndex, content);
    }

    /**
     * Set a Material as new content to hologram line and update it.
     *
     * @param hologram  The parent hologram.
     * @param pageIndex The index of the parent page.
     * @param lineIndex The index of the line.
     * @param material  Material for new content.
     * @throws IllegalArgumentException If any of the arguments is null or the indexes are invalid.
     */
    public static void setHologramLine(Hologram hologram, int pageIndex, int lineIndex, Material material) throws IllegalArgumentException {
        Validate.notNull(material);
        setHologramLine(hologram, pageIndex, lineIndex, new ItemStack(material));
    }

    /**
     * Set a ItemStack as new content to hologram line and update it.
     *
     * @param hologram  The parent hologram.
     * @param pageIndex The index of the parent page.
     * @param lineIndex The index of the line.
     * @param item      ItemStack for new content.
     * @throws IllegalArgumentException If any of the arguments is null or the indexes are invalid.
     */
    public static void setHologramLine(Hologram hologram, int pageIndex, int lineIndex, ItemStack item) throws IllegalArgumentException {
        Validate.notNull(item);
        setHologramLine(hologram, pageIndex, lineIndex, "#ICON:" + HologramItem.fromItemStack(item).getContent());
    }

    /**
     * Set a new content to hologram line and update it.
     *
     * @param hologram  The parent hologram.
     * @param pageIndex The index of the parent page.
     * @param lineIndex The index of the line.
     * @param content   The new content.
     * @throws IllegalArgumentException If any of the arguments is null or the indexes are invalid.
     */
    public static void setHologramLine(Hologram hologram, int pageIndex, int lineIndex, String content) throws IllegalArgumentException {
        Validate.notNull(hologram);
        Validate.notNull(content);

        HologramPage page = hologram.getPage(pageIndex);
        if (page == null) {
            throw new IllegalArgumentException("Given page index is out of bounds for the hologram.");
        }
        HologramLine line = page.getLine(lineIndex);
        if (line == null) {
            throw new IllegalArgumentException("Given line index is out of bounds for the hologram page.");
        }
        setHologramLine(line, content);
    }

    /**
     * Remove a line from hologram page.
     *
     * @param hologram  The hologram.
     * @param lineIndex Index of the line.
     * @return The removed hologram line.
     * @throws IllegalArgumentException If hologram is null or the indexes are invalid.
     */
    @Nullable
    public static HologramLine removeHologramLine(Hologram hologram, int lineIndex) throws IllegalArgumentException {
        return removeHologramLine(hologram, 0, lineIndex);
    }

    /**
     * Remove a line from hologram page.
     *
     * @param hologram  The hologram.
     * @param pageIndex Index of the page.
     * @param lineIndex Index of the line.
     * @return The removed hologram line.
     * @throws IllegalArgumentException If hologram is null or the indexes are invalid.
     */
    @Nullable
    public static HologramLine removeHologramLine(Hologram hologram, int pageIndex, int lineIndex) throws IllegalArgumentException {
        Validate.notNull(hologram);
        HologramPage page = hologram.getPage(pageIndex);
        if (page == null) {
            throw new IllegalArgumentException("Given page index is out of bounds for the hologram.");
        }
        HologramLine line = page.removeLine(lineIndex);
        hologram.save();
        return line;
    }

    /**
     * Remove a line from hologram page.
     *
     * @param page      The hologram page.
     * @param lineIndex Index of the line.
     * @return The removed hologram line.
     * @throws IllegalArgumentException If hologram is null or the indexes are invalid.
     */
    @Nullable
    public static HologramLine removeHologramLine(HologramPage page, int lineIndex) throws IllegalArgumentException {
        Validate.notNull(page);
        HologramLine line = page.removeLine(lineIndex);
        page.getParent().save();
        return line;
    }

    /**
     * Set the lines of this hologram on the first page.
     *
     * @param hologram The hologram.
     * @param lines    The new lines.
     * @throws IllegalArgumentException If hologram is null.
     */
    public static void setHologramLines(Hologram hologram, List<String> lines) throws IllegalArgumentException {
        setHologramLines(hologram, 0, lines);
    }

    /**
     * Set the lines of this hologram on the specified page. If there are lines
     * that are out of the new bounds, they are removed. You can even use an empty
     * array list to remove all lines from the page.
     *
     * @param hologram  The hologram.
     * @param pageIndex The page.
     * @param lines     The new lines.
     * @throws IllegalArgumentException If hologram or lines is null.
     */
    public static void setHologramLines(Hologram hologram, int pageIndex, List<String> lines) throws IllegalArgumentException {
        Validate.notNull(hologram);
        Validate.notNull(lines);

        HologramPage page = hologram.getPage(pageIndex);
        if (page == null) {
            throw new IllegalArgumentException("Given page index is out of bounds for the hologram.");
        }

        while (page.size() > lines.size()) {
            page.removeLine(page.size() - 1);
        }

        for (int i = 0; i < lines.size(); i++) {
            String content = lines.get(i);
            if (page.size() > i) {
                setHologramLine(page, i, content);
            } else {
                HologramLine line = new HologramLine(page, page.getNextLineLocation(), content);
                page.addLine(line);
            }
        }
        hologram.realignLines();
        hologram.updateAll();
        hologram.save();
    }


}
