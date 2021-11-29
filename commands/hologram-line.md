# Hologram Line

## Commands

You can simply view all commands for hologram lines using the following command.

> /dh lines help



### <mark style="color:blue;">/dh l add \<hologram> \<page> \<content></mark>

Add a new line into hologram.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<content> - [Content](../get-started/lines.md) of the new line.

{% hint style="info" %}
Aliases: 'add', 'append'
{% endhint %}

### <mark style="color:blue;">/dh l set \<hologram> \<page> \<line> \<content></mark>

Set a new content to hologram line.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.
* \<content> - New [content](../get-started/lines.md) of the line.

### <mark style="color:blue;">/dh l insert \<hologram> \<page> \<line> \<content></mark>

Insert a new line into hologram at the position of the given line.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.
* \<content> - [Content](../get-started/lines.md) of the new line.

### <mark style="color:blue;">/dh l remove \<hologram> \<page> \<line></mark>

Remove a line from hologram.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.

{% hint style="info" %}
Aliases: 'remove', 'rem', 'delete', 'del'
{% endhint %}

### <mark style="color:blue;">/dh l swap \<hologram> \<page> \<line1> \<line2></mark>

Swap two lines in a hologram.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the lines are.
* \<line1> - Index of the first line.
* \<line2> - Index of the second line.

### <mark style="color:blue;">/dh l info \<hologram> \<page> \<line></mark>

Display some general info about a hologram line.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.

### <mark style="color:blue;">/dh l edit \<hologram> \<page> \<line></mark>

Edit line in a hologram. Sends you a clickable message that puts a command to edit the line into your chat field.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.

### <mark style="color:blue;">/dh l height \<hologram> \<page> \<line> \<newHeight></mark>

Set a new height to hologram line.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.
* \<height> - New height. (0.0 - 2.5)

### <mark style="color:blue;">/dh l offsetX \<hologram> \<page> \<line> \<offset></mark>

Set a new X offset to hologram line.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.
* \<offset> - New X offset. (0.0 - 2.5)

### <mark style="color:blue;">/dh l offsetY \<hologram> \<page> \<line> \<offset></mark>

Set a new Y offset to hologram line.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.
* \<offset> - New Y offset. (0.0 - 2.5)

### <mark style="color:blue;">/dh l setfacing \<hologram> \<page> \<line> \<facing></mark>

Set the rotation of hologram line facing (yaw).

* \<hologram> -  Name of the Hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.
* \<facing> - Angle of rotation in degrees.

{% hint style="info" %}
Aliases: 'facing', 'face'
{% endhint %}

### <mark style="color:blue;">/dh l setpermission \<hologram> \<page> \<line></mark>

Set the current permission required to view the hologram line.

* \<hologram> -  Name of the Hologram.
* \<page> - Index of the page, on which the line is.
* \<line> - Index of the line.
* \[permission] - New permission. (Leave empty to remove)

{% hint style="info" %}
Aliases: 'setpermission', 'permission', 'setperm', 'perm'
{% endhint %}

### <mark style="color:blue;">/dh l align \<hologram> \<page> \<line1> \<line2> \<X|Y|XZ></mark>

Align two lines in a hologram on the specified axis.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page, on which the lines are.
* \<line1> - Index of the first line.
* \<line2> - Index of the second line.
