package eu.decentsoftware.holograms.utils.message;

import eu.decentsoftware.holograms.utils.Common;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Message {

	public static void sendHoverSuggest(Player player, String text, String hoverText, String suggest) {
		player.spigot().sendMessage(new ComponentBuilder(text)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hoverText)))
				.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggest))
				.create());
	}

	public static void sendHoverURL(Player player, String text, String hoverText, String url) {
		player.spigot().sendMessage(new ComponentBuilder(text)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hoverText)))
				.event(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
				.create());
	}

	public static BaseComponent[] getPagesComponents(final int page, boolean maxPage, String commandFormat) {
		List<BaseComponent> baseComponents = new ArrayList<>();
		if (page == 0 && maxPage) {
			baseComponents.addAll(Arrays.asList(TextComponent.fromLegacyText(Common.colorize(String.format(" &b««« &8| &3Page #%d &8| &b»»»", page + 1)))));
		} else if (page == 0) {
			baseComponents.addAll(Arrays.asList(TextComponent.fromLegacyText(Common.colorize(String.format(" &b««« &8| &3Page #%d &8| ", page + 1)))));
			baseComponents.addAll(Arrays.asList(new ComponentBuilder(Common.colorize("&b»»»"))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Common.colorize("Next page"))))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(commandFormat, page + 2)))
					.create()));
		} else if (maxPage) {
			baseComponents.addAll(Arrays.asList(new ComponentBuilder(" ")
					.reset()
					.append(Common.colorize("&b«««"))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Common.colorize("Previous page"))))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(commandFormat, page)))
					.create()));
			baseComponents.addAll(Arrays.asList(TextComponent.fromLegacyText(Common.colorize(String.format(" &8| &3Page #%d &8| &b»»»", page + 1)))));
		} else {
			baseComponents.addAll(Arrays.asList(new ComponentBuilder(" ")
					.reset()
					.append(Common.colorize("&b«««"))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Common.colorize("Previous page"))))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(commandFormat, page)))
					.create()));
			baseComponents.addAll(Arrays.asList(TextComponent.fromLegacyText(Common.colorize(String.format(" &8| &3Page #%d &8| ", page + 1)))));
			baseComponents.addAll(Arrays.asList(new ComponentBuilder(Common.colorize("&b»»»"))
					.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Common.colorize("Next page"))))
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format(commandFormat, page + 2)))
					.create()));
		}
		return baseComponents.toArray(new BaseComponent[0]);
	}

}
