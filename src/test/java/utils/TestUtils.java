package utils;

import org.openqa.selenium.support.Color;

public class TestUtils {
    public static String toHex(String color) {
        return Color.fromString(color).asHex();
    }
}
