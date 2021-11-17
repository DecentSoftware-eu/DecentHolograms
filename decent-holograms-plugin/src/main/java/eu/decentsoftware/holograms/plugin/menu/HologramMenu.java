package eu.decentsoftware.holograms.plugin.menu;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.menu.AbstractMenu;
import eu.decentsoftware.holograms.api.menu.MenuButton;
import eu.decentsoftware.holograms.api.menu.MenuModel;
import eu.decentsoftware.holograms.api.menu.MenuUtils;
import eu.decentsoftware.holograms.api.player.DecentPlayer;
import org.bukkit.Material;

public class HologramMenu extends AbstractMenu {

    private final Hologram hologram;

    public HologramMenu(DecentPlayer parent, Hologram hologram) {
        super(parent);
        this.hologram = hologram;
    }

    public HologramMenu(DecentPlayer parent, Hologram hologram, AbstractMenu previousMenu) {
        super(parent, previousMenu);
        this.hologram = hologram;
    }

    @Override
    public void construct(MenuModel menuModel) {
        menuModel.setTitle("Hologram - " + hologram.getName());
        menuModel.setSize(54);
        menuModel.setBorderSlots(MenuUtils.getBorderSlotsNoSides(menuModel.getSize()));

        menuModel.withButton(4, this::InfoButton);
        menuModel.withButton(49, this::BackButton);
    }

    private void InfoButton(MenuButton button) {
        button.getItemBuilder()
                .withMaterial(Material.SIGN)
                .withName("&b&l&nINFO")
                .withLore(
                        ""
                );
    }

}
