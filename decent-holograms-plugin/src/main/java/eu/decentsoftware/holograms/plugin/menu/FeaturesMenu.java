package eu.decentsoftware.holograms.plugin.menu;

import eu.decentsoftware.holograms.api.features.AbstractFeature;
import eu.decentsoftware.holograms.api.menu.*;
import eu.decentsoftware.holograms.api.player.DecentPlayer;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class FeaturesMenu extends AbstractPaginatedMenu<AbstractFeature> {

    public FeaturesMenu(DecentPlayer parent, String searchTerm) {
        super(parent, searchTerm);
    }

    public FeaturesMenu(DecentPlayer parent, String searchTerm, AbstractMenu previousMenu) {
        super(parent, searchTerm, previousMenu);
    }

    @Override
    public void construct(MenuModel menuModel) {
        menuModel.setTitle("Features");
        menuModel.setSize(45);
        menuModel.setBorderSlots(MenuUtils.getBorderSlots(menuModel.getSize()));

        if (canOpenPrevPage()) menuModel.withButton(18, this::PrevPageButton);
        if (canOpenNextPage()) menuModel.withButton(26, this::NextPageButton);
        menuModel.withButton(4, this::InfoButton);
        menuModel.withButton(40, this::BackButton);
    }

    @Override
    protected int getItemsPerPage() {
        return 21;
    }

    @Override
    protected List<AbstractFeature> getObjects() {
        return new ArrayList<>(DECENT_HOLOGRAMS.getFeatureManager().getFeatures());
    }

    @Override
    protected MenuSearchMatcher getMenuSearchMatcher() {
        return (searchTerm1, button) -> button.getItemBuilder().getName().contains(searchTerm1);
    }

    @Override
    protected void constructButton(MenuButton button, AbstractFeature feature) {
        button.getItemBuilder()
                .withMaterial(feature.isEnabled() ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE)
                .withName("&3&l" + feature.getName())
                .withLore(
                        "",
                        "&bInfo:",
                        String.format(" &8• &7Status: &b%s", feature.isEnabled() ? "&aON" : "&cOFF"),
                        String.format(" &8• &7Description: &b%s", feature.getDescription()), // TODO split into more lines
                        "",
                        "&3Click to toggle"
                )
                .toItemStack();

        button.withAction((e) -> {
            if (feature.isEnabled()) {
                feature.disable();
            } else {
                feature.enable();
            }
            this.open(this.currentPage);
        });
    }

    private void InfoButton(MenuButton button) {
        button.getItemBuilder()
                .withMaterial(Material.DIAMOND)
                .withName("&b&l&nFEATURES")
                .withLore(
                        "&fManage features."
                );
    }

}
