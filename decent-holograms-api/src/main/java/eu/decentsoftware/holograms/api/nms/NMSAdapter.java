package eu.decentsoftware.holograms.api.nms;

import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSAdapter {

	/*
	 *	General Methods
	 */

	/**
	 * Send a Packet to Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param packet The Packet to be sent.
	 */
	void sendPacket(@NonNull final Player player, @NonNull final Object packet);

	/**
	 * Get Type ID of given EntityType.
	 *
	 * @param type The EntityType.
	 * @return Type ID of given EntityType if it's not allowed-
	 */
	int getEntityTypeId(@NonNull final EntityType type);

	/**
	 * Get default heigth of an entity with given type.
	 *
	 * @param type Type of the entity.
	 * @return Default heigth of the entity with given type.
	 */
	float getEntityHeigth(@NonNull final EntityType type);

	/*
	 *	 Fake Entity Methods
	 */

	/**
	 * Send a packet showing fake entity to Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param location Location of the entity.
	 * @param entityId The fake entity's id.
	 * @param entityTypeId Type of the entity.
	 */
	void showFakeEntity(@NonNull final Player player, @NonNull final Location location, final int entityId, final int entityTypeId);

	/**
	 * Send a packet showing fake entity to Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param location Location of the entity.
	 * @param entityId The fake entity's id.
	 * @param entityTypeId Type of the entity.
	 */
	void showFakeEntityLiving(@NonNull final Player player, @NonNull final Location location, final int entityId, final int entityTypeId);

	/**
	 * Send a packet showing fake ArmorStand to a Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param location Location of the ArmorStand.
	 * @param entityId The fake ArmorStand's id.
	 * @param small Boolean whether the ArmorStand should be small.
	 */
	void showFakeArmorStand(@NonNull final Player player, @NonNull final Location location, final int entityId, final boolean small);

	/**
	 * Send a packet showing fake ItemEntity to a Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param location Location of the ItemEntity.
	 * @param entityId The fake ItemEntity's id.
	 * @param itemStack Displayed ItemStack.
	 */
	void showFakeItem(@NonNull final Player player, @NonNull final Location location, final int entityId, @NonNull final ItemStack itemStack);

	/**
	 * Send a packet updating the metadata of a fake entity to Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param entityId The fake entity's id.
	 * @param name Entity's new name.
	 */
	void updateFakeEntityName(@NonNull final Player player, final int entityId, @NonNull final String name);

	/**
	 * Send a packet displaying item as helmet of fake entity.
	 *
	 * @param player The Player receiving the packet.
	 * @param entityId The fake entity's id.
	 * @param itemStack The helmet ItemStack.
	 */
	void helmetFakeEntity(@NonNull final Player player, final int entityId, @NonNull final ItemStack itemStack);

	/**
	 * Send a teleport packet updating the location of a fake entity to Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param entityId The fake entity's id.
	 * @param location Entity's new location.
	 */
	void teleportFakeEntity(@NonNull final Player player, @NonNull final Location location, final int entityId);

	/**
	 * Make a fake entity mount a fake vehicle entity and
	 * send a packet updating the passengers of the fake vehicle entity to Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param vehicleId The fake vehicle entity's id.
	 * @param entityId The fake entity's id.
	 */
	void attachFakeEnity(@NonNull final Player player, final int vehicleId, final int entityId);

	/**
	 * Send a packet hiding a fake entity to Player.
	 *
	 * @param player The Player recieving the packet.
	 * @param entityId The fake entity's id.
	 */
	void hideFakeEntity(@NonNull final Player player, final int entityId);

}
