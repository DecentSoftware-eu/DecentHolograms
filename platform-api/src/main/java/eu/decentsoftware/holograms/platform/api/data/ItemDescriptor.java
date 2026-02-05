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

package eu.decentsoftware.holograms.platform.api.data;

import java.util.Objects;

public class ItemDescriptor {

    private String type;
    private boolean enchanted;
    private Integer damage;
    private Integer customModelData;
    private String skullTexture;
    private DecentColor leatherColor;

    public ItemDescriptor(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isEnchanted() {
        return enchanted;
    }

    public void setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    public String getSkullTexture() {
        return skullTexture;
    }

    public void setSkullTexture(String skullTexture) {
        this.skullTexture = skullTexture;
    }

    public DecentColor getLeatherColor() {
        return leatherColor;
    }

    public void setLeatherColor(DecentColor leatherColor) {
        this.leatherColor = leatherColor;
    }

    @Override
    public String toString() {
        return "ItemDescriptor{" +
                "type='" + type + '\'' +
                ", enchanted=" + enchanted +
                ", damage=" + damage +
                ", customModelData=" + customModelData +
                ", skullTexture='" + skullTexture + '\'' +
                ", leatherColor=" + leatherColor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemDescriptor)) {
            return false;
        }
        ItemDescriptor that = (ItemDescriptor) o;
        return isEnchanted() == that.isEnchanted()
                && Objects.equals(getDamage(), that.getDamage())
                && Objects.equals(getCustomModelData(), that.getCustomModelData())
                && Objects.equals(getType(), that.getType())
                && Objects.equals(getSkullTexture(), that.getSkullTexture())
                && Objects.equals(getLeatherColor(), that.getLeatherColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), isEnchanted(), getDamage(), getCustomModelData(), getSkullTexture(), getLeatherColor());
    }
}
