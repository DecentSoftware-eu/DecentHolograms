package eu.decentsoftware.holograms.api.menu;

@FunctionalInterface
public interface MenuSearchMatcher {
	boolean matches(String searchTerm, MenuButton button);
}
