---
description: General usage and editting of Holograms.
---

# Hologram

## Commands

There is a lot of commands for editting holograms so we put them in its own category. You can access all of them using the command below.

> /dh holograms \<subcommand>

{% hint style="info" %}
Aliases: 'holograms', 'hologram', 'holo', 'h'
{% endhint %}

<mark style="color:blue;"></mark>

### <mark style="color:blue;">/dh h help</mark>

Displays a list of all hologram-related commands.

### <mark style="color:blue;">/dh h create \<name> \[content]</mark>

Create a new hologram.

* \<name> -  Name of the created Hologram.
* \[content] - Content of the first line. (Optional)

{% hint style="info" %}
Aliases: 'create', 'c'
{% endhint %}

### <mark style="color:blue;">/dh h clone \<hologram> \<name> \[temp]</mark>

Clone an existing hologram.

* \<hologram> -  Name of the Hologram to clone.
* \<name> - Name of the new, cloned hologram.
* \[temp] - 'True', if you DON'T want the hologram to save, otherwise 'False'. \[Default: False] (Optional)

{% hint style="info" %}
Aliases: 'copy'
{% endhint %}

### <mark style="color:blue;">/dh h delete \<hologram></mark>

Delete existing hologram.

* \<hologram> -  Name of the Hologram.

{% hint style="info" %}
Aliases: 'delete', 'del', 'remove', 'rem'
{% endhint %}

### <mark style="color:blue;">/dh h setpermission \<hologram> \[permission]</mark>

Set the current permission required to view the hologram.

* \<hologram> -  Name of the Hologram.
* \[permission] - New permission. (Leave empty to remove)

{% hint style="info" %}
Aliases: 'setpermission', 'permission', 'setperm', 'perm'
{% endhint %}

### <mark style="color:blue;">/dh h info \<hologram></mark>

Prints some general info about a hologram.

* \<hologram> -  Name of the Hologram.

### <mark style="color:blue;">/dh h lines \<hologram> \[page]</mark>

Lists all the holograms lines.

* \<hologram> -  Name of the Hologram to clone.
* \[page] - Page of the list. (Optional)

### <mark style="color:blue;">/dh h teleport \<hologram></mark>

Teleports you to the given hologram.

* \<hologram> -  Name of the Hologram.

{% hint style="info" %}
Aliases: 'teleport', 'tele', 'tp'
{% endhint %}

### <mark style="color:blue;">/dh h move \<hologram> \<x> \<y> \<z></mark>

Teleports you to the given hologram.

* \<hologram> -  Name of the Hologram.
* \<x> - New X location of the hologram.
* \<y> - New Y location of the hologram.
* \<z> - New Z location of the hologram.

{% hint style="info" %}
Aliases: 'mv'
{% endhint %}

### <mark style="color:blue;">/dh h movehere \<hologram></mark>

Teleports you to the given hologram.

* \<hologram> -  Name of the Hologram.

{% hint style="info" %}
Aliases: 'mvhr'
{% endhint %}

### <mark style="color:blue;">/dh h update \<hologram></mark>

Hide the hologram and then Show it again.

* \<hologram> -  Name of the Hologram.

### <mark style="color:blue;">/dh h disable \<hologram></mark>

Disable a hologram. While disabled, it won't be displayed to anyone.

* \<hologram> -  Name of the Hologram.

{% hint style="info" %}
Aliases: 'disable', 'off'
{% endhint %}

### <mark style="color:blue;">/dh h enable \<hologram></mark>

Enable a hologram.

* \<hologram> -  Name of the Hologram.

{% hint style="info" %}
Aliases: 'enable', 'on'
{% endhint %}

### <mark style="color:blue;">/dh h center \<hologram></mark>

Moves a hologram into the center of the block on its current X, Z location.

* \<hologram> -  Name of the Hologram.

### <mark style="color:blue;">/dh h align \<hologram> \<X|Y|Z|XZ> \<otherHologram></mark>

Moves a hologram to the location of the other hologram on a specified axis.

* \<hologram> -  Name of the Hologram.
* \<otherHologram> - Name of the other Hologram.

### <mark style="color:blue;">/dh h setfacing \<hologram> \<facing></mark>

Set the rotation of hologram facing (yaw).

* \<hologram> -  Name of the Hologram.
* \<facing> - Angle of rotation in degrees.

{% hint style="info" %}
Aliases: 'facing', 'face'
{% endhint %}

### <mark style="color:blue;">/dh h downorigin \<hologram> \<true|false></mark>

Set the value of down origin. If true, hologram's location will be relative to it's bottom.

* \<hologram> -  Name of the Hologram.

### <mark style="color:blue;">/dh h near \<distance></mark>

List of holograms in the specified distance from you.

* \<distance> -  Distance to check in blocks.

### <mark style="color:blue;">/dh h setdisplayrange \<hologram> \<range></mark>

Set maximum distance a player can be from a hologram to see it.

* \<hologram> -  Name of the Hologram.
* \<range> -  Range in blocks.

### <mark style="color:blue;">/dh h setupdaterange \<hologram> \<range></mark>

Set maximum distance a player can be from a hologram to see it's updates.

* \<hologram> -  Name of the Hologram.
* \<range> -  Range in blocks.

### <mark style="color:blue;">/dh h setupdateinterval \<hologram> \<interval></mark>

Set update interval.

* \<hologram> -  Name of the Hologram.
* \<interval> -  Interval in ticks (20 = 1sec).

