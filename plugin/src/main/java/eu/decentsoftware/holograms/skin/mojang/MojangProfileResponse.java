package eu.decentsoftware.holograms.skin.mojang;

import java.util.List;

/**
 * Response from the Mojang API when looking up a player's profile.
 *
 * @author d0by
 * @see <a href="https://minecraft.wiki/w/Mojang_API#Query_player%27s_skin_and_cape">Mojang API Documentation</a>
 * @since 1.0.0
 */
public class MojangProfileResponse {

    /**
     * The UUID of the player.
     */
    private String id;
    /**
     * The name of the player.
     */
    private String name;
    /**
     * List of properties associated with the player's profile, such as skin textures.
     */
    private List<MojangProfileProperty> properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MojangProfileProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<MojangProfileProperty> properties) {
        this.properties = properties;
    }
}
