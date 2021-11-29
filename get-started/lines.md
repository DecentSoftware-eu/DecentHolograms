---
description: General usage and editting of Hologram Lines.
---

# Lines

## Content

There are multiple different types of Hologram Line content. Type of a certain line must be in it's content.

| Type      | Declaration                                           |
| --------- | ----------------------------------------------------- |
| TEXT      | Any content, that doesn't identify as any other type. |
| ICON      | #ICON: ITEM\_FORMAT                                   |
| HEAD      | #HEAD: ITEM\_FORMAT                                   |
| SMALLHEAD | #SMALLHEAD: ITEM\_FORMAT                              |
| ENTITY    | #ENTITY: \<entityType>                                |

{% hint style="info" %}
In the game, the HAND declaration can be used to set the ITEM\_FORMAT in a given type.&#x20;

For example: `/dh line add <hologram> <page> #ICON: <HAND>`
{% endhint %}

### Entity Type List

**Link:** [https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/entity/EntityType.html)

### Item Format

`<material>:[data_value] [NBT] (!ENCHANTED)`

* [ ] \<material> - Material of the item.
* [ ] :\[data\_value] - Data Value (durability) of the item. (Optional)
* [ ] \[NBT] - NBT data of the item. (Optional)
* [ ] (!ENCHANTED) - '!ENCHANTED' makes the item enchanted. (Optional)

#### Example Items:

`STONE !ENCHANTED`

`WOOL:14`

`LEATHER_CHESTPLATE {display:{color:3847130}}`

### `Player/Custom Heads:`

#### **You can use the player's name**

* `#ICON PLAYER_HEAD (LixCisCZ)`

**You can use a placeholder (if you have a placeholderAPI on your server)**

* `#ICON PLAYER_HEAD (%player_name%)`

**You can use head value**

* `#ICON PLAYER_HEAD (eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE2ZjAwNzNjNTg3MDNkOGQ0MWU1NWUwYTNhYmIwNDJiNzNmOGMxMDViYzQxYzJmMDJmZmUzM2YwMzgzY2YwYSJ9fX0=)`

{% hint style="warning" %}
If you have a lower version than 1.13, you must use`SKULL_ITEM`instead of`PLAYER_HEAD`
{% endhint %}
