package eu.decentsoftware.holograms.api;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;

import javax.annotation.Nullable;
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
     * @param name The name.
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
     * @param name The name.
     * @param location The location.
     * @param saveToFile Boolean: Should the hologram be saved into file?
     * @return The new hologram.
     * @throws IllegalArgumentException If name or location is null or hologram with the given name already exists.
     */
    public static Hologram createHologram(String name, Location location, boolean saveToFile) throws IllegalArgumentException {
        return createHologram(name, location, saveToFile, Lists.newArrayList("Decent", "Holograms"));
    }

    /**
     * Create a new hologram with the given name on the specified location with the given lines.
     *
     * @param name The name.
     * @param location The location.
     * @param lines The lines of this hologram.
     * @return The new hologram.
     * @throws IllegalArgumentException If name or location is null or hologram with the given name already exists.
     */
    public static Hologram createHologram(String name, Location location, List<String> lines) throws IllegalArgumentException {
        return createHologram(name, location, false, lines);
    }

    /**
     * Create a new hologram with the given name on the specified location with the given lines.
     *
     * @param name The name.
     * @param location The location.
     * @param saveToFile Boolean: Should the hologram be saved into file?
     * @param lines The lines of this hologram.
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
                HologramLine hologramLine = createHologramLine(page, page.getNextLineLocation(), line);
                page.addLine(hologramLine);
            }
        }
        hologram.showAll();
        return hologram;
    }

    /**
     * Create a new hologram line with the given parent page on the specified location with the given content.
     *
     * @param parent The parent page.
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
     * @param parent The parent page.
     * @param location The location.
     * @param content The content.
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
     * @param index The index.
     * @return The hologram page.
     * @throws IllegalArgumentException If the hologram is null.
     */
    @Nullable
    public static HologramPage getHologramPage(Hologram hologram, int index) throws IllegalArgumentException {
        Validate.notNull(hologram);
        return hologram.getPage(index);
    }

    /**
     * Get hologram line by index.
     *
     * @param page The parent page.
     * @param index The index.
     * @return The hologram line.
     * @throws IllegalArgumentException If the page is null.
     */
    @Nullable
    public static HologramLine getHologramLine(HologramPage page, int index) throws IllegalArgumentException {
        Validate.notNull(page);
        return page.getLine(index);
    }

    /**
     * Set a new content to hologram line and update it.
     *
     * @param line The line.
     * @param content The new content.
     * @throws IllegalArgumentException If any of the arguments is null.
     */
    public static void setHologramLine(HologramLine line, String content) throws IllegalArgumentException {
        Validate.notNull(line);
        Validate.notNull(content);
        if (!line.getContent().equals(content)) {
            line.setContent(content);
            line.update();
        }
    }

    /**
     * Set a new content to hologram line and update it.
     *
     * @param page The parent page.
     * @param lineIndex The index of the line.
     * @param content The new content.
     * @throws IllegalArgumentException If any of the arguments is null.
     */
    public static void setHologramLine(HologramPage page, int lineIndex, String content) throws IllegalArgumentException {
        Validate.notNull(page);
        Validate.notNull(content);
        HologramLine line = getHologramLine(page, lineIndex);
        setHologramLine(line, content);
    }

    /**
     * Set a new content to hologram line and update it.
     *
     * @param hologram The parent hologram.
     * @param lineIndex The index of the line.
     * @param content The new content.
     * @throws IllegalArgumentException If any of the arguments is null.
     */
    public static void setHologramLine(Hologram hologram, int lineIndex, String content) throws IllegalArgumentException {
        setHologramLine(hologram, 0, lineIndex, content);
    }

    /**
     * Set a new content to hologram line and update it.
     *
     * @param hologram The parent hologram.
     * @param pageIndex The index of the parent page.
     * @param lineIndex The index of the line.
     * @param content The new content.
     * @throws IllegalArgumentException If any of the arguments is null.
     */
    public static void setHologramLine(Hologram hologram, int pageIndex, int lineIndex, String content) throws IllegalArgumentException {
        Validate.notNull(hologram);
        Validate.notNull(content);
        HologramPage page = getHologramPage(hologram, pageIndex);
        HologramLine line = getHologramLine(page, lineIndex);
        setHologramLine(line, content);
    }

    /**
     * Set the lines of this hologram on the first page.
     *
     * @param hologram The hologram.
     * @param lines The new lines.
     * @throws IllegalArgumentException If hologram is null.
     */
    public static void setHologramLines(Hologram hologram, List<String> lines) throws IllegalArgumentException {
        setHologramLines(hologram, 0, lines);
    }

    /**
     * Set the lines of this hologram on the specified page.
     *
     * @param hologram The hologram.
     * @param pageIndex The page.
     * @param lines The new lines.
     * @throws IllegalArgumentException If hologram is null.
     */
    public static void setHologramLines(Hologram hologram, int pageIndex, List<String> lines) throws IllegalArgumentException {
        Validate.notNull(hologram);

        HologramPage page = hologram.getPage(pageIndex);
        if (lines == null || lines.isEmpty() || page == null) {
            return;
        }

        while (page.size() > lines.size()) {
            page.removeLine(page.size() - 1);
        }

        for (int i = 0; i < lines.size(); i++) {
            String content = lines.get(i);
            if (page.size() > i) {
                HologramLine line = page.getLine(i);
                if (!line.getContent().equals(content)) {
                    line.setContent(content);
                }
            } else {
                HologramLine line = createHologramLine(page, page.getNextLineLocation(), content);
                page.addLine(line);
            }
        }
        hologram.realignLines();
        hologram.updateAll();
    }


}
