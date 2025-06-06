package eu.decentsoftware.holograms.api.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonTest {

    @ParameterizedTest(name = "currentVersion={0}, newVersion={1}, expectedResult={2}")
    @CsvSource({
            "1.2.3, 2.0.0, true",
            "1.2.3, 0.9.9, false",
            "1.2.3, 1.3.0, true",
            "1.2.3, 1.1.9, false",
            "1.2.3, 1.2.4, true",
            "1.2.3, 1.2.2, false",
            "1.2.3, 2.0.0, true",
            "1.2.3, 1.3.0, true",
            "1.2.3, 1.2.4, true",
            "1.2.3, 2.3.4, true",
            "1.2.3, 1.3.4, true",
            "1.2.3, 2.3.3, true",
            // Versions are equal
            "1.2.3, 1.2.3, false",
            // Invalid inputs
            "-1.2.3, 1.4.3, false",
            "1.2.3, 1.-4.3, false",
            "abc, 1.2.3, false",
            "1.2.3, xyz, false",
            "'', 1.2.3, false",
            "1.2.3, '', false",
            ", 1.2.3, false",
            "1.2.3,, false",
    })
    void testIsVersionHigher(String currentVersion, String newVersion, boolean expectedResult) {
        boolean result = Common.isVersionHigher(currentVersion, newVersion);

        assertEquals(expectedResult, result,
                String.format("Expected isVersionHigher('%s', '%s') to return %b", currentVersion, newVersion, expectedResult));
    }

}