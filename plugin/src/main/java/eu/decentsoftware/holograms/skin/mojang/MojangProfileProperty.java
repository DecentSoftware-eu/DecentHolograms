package eu.decentsoftware.holograms.skin.mojang;

/**
 * Represents a property in a player's profile.
 *
 * @author d0by
 * @see <a href="https://minecraft.wiki/w/Mojang_API#Query_player%27s_skin_and_cape">Mojang API Documentation</a>
 * @since 1.0.0
 */
public class MojangProfileProperty {

    /**
     * Name of the property, always "textures" for skin properties.
     */
    private String name;
    /**
     * Value of the property, base64 encoded.
     */
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
