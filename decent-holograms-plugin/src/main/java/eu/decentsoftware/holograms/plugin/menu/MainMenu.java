package eu.decentsoftware.holograms.plugin.menu;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.menu.*;
import eu.decentsoftware.holograms.api.player.DecentPlayer;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainMenu extends AbstractPaginatedMenu<Hologram> {

    public MainMenu(DecentPlayer parent, String searchTerm) {
        super(parent, searchTerm);
    }

    public MainMenu(DecentPlayer parent, String searchTerm, AbstractMenu previousMenu) {
        super(parent, searchTerm, previousMenu);
    }

    @Override
    public void construct(MenuModel menuModel) {
        menuModel.setTitle("Decent Holograms");
        menuModel.setSize(54);

        Set<Integer> borderSlots = MenuUtils.getBorderSlots(menuModel.getSize() - 9);
        borderSlots.add(45);
        borderSlots.add(46);
        borderSlots.add(52);
        borderSlots.add(53);
        menuModel.setBorderSlots(borderSlots);

        if (canOpenPrevPage()) menuModel.withButton(18, this::PrevPageButton);
        if (canOpenNextPage()) menuModel.withButton(26, this::NextPageButton);
        menuModel.withButton(4, this::InfoButton);
        menuModel.withButton(47, this::CreateButton);
        menuModel.withButton(48, this::SortButton);
        menuModel.withButton(49, this::SearchButton);
        menuModel.withButton(50, this::FeaturesButton);
        menuModel.withButton(51, this::SettingsButton);
    }

    @Override
    protected int getItemsPerPage() {
        return 21;
    }

    @Override
    protected List<Hologram> getObjects() {
        return new ArrayList<>(DecentHologramsAPI.get().getHologramManager().getHolograms());
    }

    @Override
    protected MenuSearchMatcher getMenuSearchMatcher() {
        return (searchTerm1, button) -> button.getItemBuilder().getName().contains(searchTerm1);
    }

    @Override
    protected void constructButton(MenuButton button, Hologram hologram) {
        List<String> lore = Lang.getHologramInfo(hologram);
        lore.add(0, "");
        lore.add(1, "&bInfo:");
        lore.add("");
        lore.add("&3Press Q to delete");
        lore.add("&3Click to edit");

        button.getItemBuilder()
                .withMaterial(Material.PAINTING)
                .withName("&3&l" + hologram.getName())
                .withLore(lore);
        button.withAction((e) -> new HologramMenu(parent, hologram, this).open());
    }

    private void InfoButton(MenuButton button) {
        button.getItemBuilder()
                .withMaterial(Material.SIGN)
                .withName("&b&l&nINFO")
                .withLore(
                        "&fThe main DecentHolograms menu.",
                        "&fFrom here, you can edit holograms."
                );
    }

    private void CreateButton(MenuButton button) {
        button.getItemBuilder()
                .withMaterial(Material.LIME_DYE)
                .withName("&b&lCreate")
                .withLore(
                        "&fCreate a new hologram."
                );
        button.withAction(e -> {

        });
    }

    private void SortButton(MenuButton button) {
        button.getItemBuilder()
                .withMaterial(Material.HOPPER)
                .withName("&b&lSort")
                .withLore(
                        "&fSort the holograms."
                );
    }

    private void SearchButton(MenuButton button) {
        button.getItemBuilder()
                .withMaterial(Material.WRITABLE_BOOK)
                .withName("&b&lSearch")
                .withLore(
                        "&fSearch the holograms."
                );
    }

    private void FeaturesButton(MenuButton button) {
        button.getItemBuilder()
                .withMaterial(Material.DIAMOND)
                .withName("&b&lFeatures")
                .withLore(
                        "&fManage features."
                );
        button.withAction(e -> new FeaturesMenu(parent, null, this).open());
    }

    private void SettingsButton(MenuButton button) {
        button.getItemBuilder()
                .withMaterial(Material.COMPARATOR)
                .withName("&b&lSettings")
                .withLore(
                        "&fEdit settings."
                );
    }

}
