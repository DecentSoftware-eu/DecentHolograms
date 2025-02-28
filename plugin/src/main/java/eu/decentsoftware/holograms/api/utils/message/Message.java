package eu.decentsoftware.holograms.api.utils.message;

import eu.decentsoftware.holograms.api.utils.Common;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public final class Message {

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

	public static void sendPaginatedMessage(Player player, int currentPage, String commandFormat, int itemsPerPage, List<String> header, List<String> footer, List<String> items) {
		Validate.notNull(player);
		Validate.notNull(items);
		Message.sendPaginatedMessage(player, currentPage, commandFormat, itemsPerPage, header, footer, items, s -> s);
	}

	public static <T> void sendPaginatedMessage(Player player, int currentPage, String commandFormat, int itemsPerPage, List<String> header, List<String> footer, List<T> objects, Function<T, String> parseItem) {
		Validate.notNull(player);
		Validate.notNull(objects);
		Validate.notNull(parseItem);

		final int itemsTotal = objects.size();
		final int maxPage = itemsTotal % itemsPerPage == 0 ? itemsTotal / itemsPerPage - 1 : itemsTotal / itemsPerPage;
		if (currentPage > maxPage) currentPage = maxPage;
		final int startIndex = currentPage * itemsPerPage;
		final int endIndex = Math.min(startIndex + itemsPerPage, itemsTotal);

		if (header != null && !header.isEmpty()) {
			int finalCurrentPage = currentPage;
			header.forEach(line -> Common.tell(player, line.replace("{page}", String.valueOf(finalCurrentPage + 1))));
		}
		for (int i = startIndex; i < endIndex; i++) {
			T object = objects.get(i);
			Common.tell(player, parseItem.apply(object));
		}
		if (footer != null && !footer.isEmpty()) {
			footer.forEach(line -> Common.tell(player, line));
		}
		player.sendMessage("");
		if (maxPage > 0) {
			player.spigot().sendMessage(Message.getPagesComponents(currentPage, maxPage == currentPage, commandFormat));
			player.sendMessage("");
		}
	}

}
