# Hologram Pages

## Commands

You can simply view all commands for hologram pages using the following command.

> /dh pages help

### <mark style="color:blue;">/dh p add \<hologram> \[content]</mark>

Add a new page into hologram.

* \<hologram> - Name of the hologram.
* \[content] - [Content](../get-started/lines.md) of the first line.

### <mark style="color:blue;">/dh p insert \<hologram> \<index></mark>

Insert a new page into hologram.

* \<hologram> - Name of the hologram.
* \<index> - Index of the new page.

### <mark style="color:blue;">/dh p remove \<hologram> \<page></mark>

Remove a page from hologram.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page to remove.

### <mark style="color:blue;">/dh p swap \<hologram> \<page1> \<page2></mark>

Swap two pages in a hologram.

* \<hologram> - Name of the hologram.
* \<page1> - Index of the first page.
* \<page2> - Index of the second page.

### <mark style="color:blue;">/dh p switch \<hologram> \<page></mark>

Switch to another page in a hologram.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page to view.

### <mark style="color:blue;">/dh p addaction \<hologram> \<page> \<clickType> \<action></mark>

Add action to page.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page.
* \<clickType> - ClickType that triggers this action.
* \<action> - The action.

### <mark style="color:blue;">/dh p removeaction \<hologram> \<page> \<clickType> \<index></mark>

Remove action from page.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page to view.
* \<clickType> - ClickType that triggers this action.
* \<index> - Index of the action.

### <mark style="color:blue;">/dh p clearactions \<hologram> \<page> \<clickType></mark>

Clear actions on specified click type in page.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page to view.
* \<clickType> - The ClickType.

### <mark style="color:blue;">/dh p actions \<hologram> \<page> \<clickType></mark>

View actions set on specified click type in page.

* \<hologram> - Name of the hologram.
* \<page> - Index of the page to view.
* \<clickType> - The ClickType.
