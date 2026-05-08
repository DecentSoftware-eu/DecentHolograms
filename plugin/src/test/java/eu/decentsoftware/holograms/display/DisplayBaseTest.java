package eu.decentsoftware.holograms.display;

import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisplayBaseTest {

    private static Object[][] provideConstruction_nulls() {
        DecentLocation location = new DecentLocation("world", 0, 100, 0, 0, 0);
        DisplaySettings settings = new DisplaySettings();
        return new Object[][]{
                {null, location, settings, "name cannot be null"},
                {"display", null, settings, "location cannot be null"},
                {"display", location, null, "settings cannot be null"},
        };
    }

    @ParameterizedTest
    @MethodSource("provideConstruction_nulls")
    void testConstruction_nulls(String name, DecentLocation location, DisplaySettings settings, String expectedMessage) {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new TestDisplay(name, location, settings));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            // Blank
            "",
            " ",
            "\n",
            "\t",
            // Invalid characters
            "#name",
            "@name",
            "name with spaces",
            "/name",
            "\\name",
            "na|me",
            "\"name\"",
            "'name'",
            "name.",
            "name,",
            "name=",
            "name*",
            "name+",
    })
    void testConstruction_invalidName(String name) {
        DecentLocation location = new DecentLocation("world", 0, 100, 0, 0, 0);
        DisplaySettings settings = new DisplaySettings();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TestDisplay(name, location, settings));

        assertEquals("Display name '" + name + "' does not match the regex " + Common.NAME_REGEX + ".", exception.getMessage());
    }

    @Test
    void testConstruction_valid() {
        String name = "valid-Name_1";
        DecentLocation location = new DecentLocation("world", 0, 100, 0, 0, 0);
        DisplaySettings settings = new DisplaySettings();

        DisplayBase display = new TestDisplay(name, location, settings);

        assertEquals(name, display.getName());
        assertEquals(location, display.getLocation());
        assertEquals(settings, display.getSettings());
    }

    private static class TestDisplay extends DisplayBase {
        public TestDisplay(String name, DecentLocation location, DisplaySettings settings) {
            super(name, location, settings);
        }

        @Override
        public DisplayType getType() {
            return null;
        }
    }
}