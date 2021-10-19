package eu.decentsoftware.holograms.utils.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.items.ItemBuilder;
import eu.decentsoftware.holograms.utils.items.ItemWrapper;
import eu.decentsoftware.holograms.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration extends YamlConfiguration {

    private final String fileName;
    private final JavaPlugin plugin;
    private File file;

    public Configuration(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.fileName = name.endsWith(".yml") ? name : name + ".yml";

        loadFile();
        createData();

        try {
            loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        this.remapWrongNaming();
        this.saveData();
    }

    public Configuration(JavaPlugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
        this.fileName = file.getName();

        createData();

        try {
            loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() throws IOException, InvalidConfigurationException {
        this.load(file);
    }

    public File loadFile() {
        this.file = new File(this.plugin.getDataFolder(), this.fileName);
        return this.file;
    }

    public void saveData() {
        if (this.file == null) {
            loadFile();
        }

        try {
            this.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Attempting to fix error...");
            createData();
            saveData();
        }
    }

    @Override
    public void save(File file) throws IOException {
        super.save(file);
    }

    public void reload() {
        try {
            this.loadConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void createData() {
        if (!file.exists()) {
            if (!this.plugin.getDataFolder().exists()) {
                this.plugin.getDataFolder().mkdirs();
            }

            // If file isn't a resource, create from scratch
            if (this.plugin.getResource(this.fileName) == null) {
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                this.plugin.saveResource(this.fileName, false);
            }
        }
    }

    public void delete() {
        if (this.file.exists()) {
            this.file.delete();
        }
    }

    public void setLocation(String path, Location location, boolean includeYawPitch) {
        set(path, LocationUtils.asString(location, includeYawPitch));
    }

    public void setLocation(String path, Location location) {
        setLocation(path, location, false);
    }

    public Location getLocation(String path) {
        return LocationUtils.asLocation(getString(path));
    }

    public Set<String> getSectionKeys(String path) {
        if (!contains(path)) {
            return Sets.newHashSet();
        }
        return getConfigurationSection(path).getKeys(false);
    }

    public Object getOrDefault(String path, Object defaultValue) {
        if (!contains(path)) {
            set(path, defaultValue);
            return defaultValue;
        }
        return get(path);
    }

    public void setItem(String path, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        Material material = item.getType();

        if (meta.getDisplayName() != null) {
            set(path + ".name", meta.getDisplayName());
        }

        set(path + ".material", material.name());
        set(path + ".durability", item.getDurability());
        set(path + ".amount", item.getAmount());

        if (meta.getLore() != null && !meta.getLore().isEmpty()) {
            set(path + ".lore", meta.getLore());
        }

        if (meta.getEnchants() != null && !meta.getEnchants().isEmpty()) {
            Map<Enchantment, Integer> enchants = meta.getEnchants();
            List<String> list = Lists.newArrayList();
            enchants.forEach((enchantment, level) ->
                    list.add(String.format("%s:%d", enchantment.getName(), level))
            );
            set(path + ".enchantments", list);
        }

        if (material.name().contains("SKULL") || material.name().contains("HEAD")) {
            String skullOwner = new ItemBuilder(item).getSkullOwner();
            if (skullOwner != null) {
                set(path + ".skull.owner", skullOwner);
            }
        }
    }

    public ItemStack getItem(String path) {
        String materialName = getString(path + ".material", "STONE");

        Material material = Material.getMaterial(materialName);
        if (material == null) {
            material = Material.STONE;
        }

        ItemBuilder builder = new ItemBuilder(material);
        if (contains(path + ".name")) {
            builder.withName(Common.colorize(getString(path + ".name")));
        }

        if (contains(path + ".durability")) {
            builder.withDurability((short) getInt(path + ".durability", 0));
        }

        if (contains(path + ".lore")) {
            builder.withLore(getStringList(path + ".lore"));
        }

        if (contains(path + ".enchantments")) {
            List<String> list = getStringList(path + ".enchantments");
            list.forEach((s) -> {
                String[] spl = s.split(":");
                Enchantment enchantment = Enchantment.getByName(spl[0]);
                int level = Integer.parseInt(spl[1]);
                builder.withEnchantment(enchantment, level);
            });
        }

        if (material.name().contains("SKULL") || material.name().contains("HEAD")) {
            String ownerName = getString(path + ".skull.owner");
            if (ownerName != null) {
                builder.withSkullOwner(ownerName);
            }
        }

        return builder.toItemStack();
    }

    public ItemWrapper getItemWrapper(String path) {
        String materialName = getString(path + ".material");
        Material material = materialName == null ? null : Material.getMaterial(materialName);
        String name = getString(path + ".name", null);
        int amount = getInt(path + ".amount", 1);
        short durability = (short) getInt(path + ".durability", 0);
        List<String> lore = getStringList(path + ".lore");
        Map<Enchantment, Integer> enchantments = Maps.newHashMap();
        getStringList(path + ".enchantments").forEach(s -> {
            String[] spl = s.split(":");
            Enchantment enchantment = Enchantment.getByName(spl[0]);
            int level = Integer.parseInt(spl[1]);
            enchantments.put(enchantment, level);
        });
        String skullOwnerName = getString(path + ".skull.owner");
        String skullTexture = getString(path + ".skull.texture");
        ItemFlag[] flags = getStringList(path + ".flags").stream().map(ItemFlag::valueOf).toArray(ItemFlag[]::new);
        return new ItemWrapper(material, name, skullOwnerName, skullTexture, amount, durability, lore, enchantments, flags);
    }

    public void remapWrongNaming() {
        for (String key : getKeys(true)) {
            if (!key.contains("heigth")) continue;
            Object object = get(key);
            Common.log("%s | %s", key, object);
            set(key, null);
            set(key.replace("heigth", "height"), object);
        }
    }

}