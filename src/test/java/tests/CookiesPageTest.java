package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static constants.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.TestUtils.toHex;

public class CookiesPageTest extends BaseTest {
    Actions actions;
    private static final String COOKIES_PAGE = BASE_PAGE + "cookies.html";
    private static final String BACKGROUND_COLOR = "background-color";
    private static final String COLOR = "color";
    private static final String EMPTY_FIELD = "";

    @BeforeEach
    void setup() {
        driver.get(COOKIES_PAGE);
        actions = new Actions(driver);
    }

    @Test
    void checkColorChangesUnderMouseTest() {
        WebElement button = driver.findElement(By.id("refresh-cookies"));
        String actualButtonText = button.getText();
        String defaultBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
        String defaultTextColor = toHex(button.getCssValue(COLOR));

        actions.moveToElement(button).perform();

        wait.until(driver -> {
            String currentBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
            return !currentBGColor.equals(defaultBGColor);
        });
        assertAll(
                () -> assertEquals("Display cookies", actualButtonText),
                () -> assertEquals("#000000", defaultBGColor),
                () -> assertEquals("#0d6efd", defaultTextColor),
                () -> {
                    String actualBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
                    assertEquals("#0d6efd", actualBGColor);
                },
                () -> {
                    String actualTextColor = toHex(button.getCssValue(COLOR));
                    assertEquals("#ffffff", actualTextColor);
                }
        );
    }

    @Test
    void clickDisplayCookiesButtonTest() {
        WebElement button = driver.findElement(By.id("refresh-cookies"));
        String defaultCookiesList = driver.findElement(By.id("cookies-list")).getText();
        Set<Cookie> cookies = driver.manage().getCookies();

        button.click();

        Map<String, String> shownCookies = getDisplayedCookies();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultCookiesList),
                () -> assertEquals(cookies.size(), shownCookies.size()),
                () -> checkDisplayedCookies(cookies, shownCookies)
        );
    }

    private Map<String, String> getDisplayedCookies() {
        Map<String, String> shownCookies = new HashMap<>();
        Arrays.stream(driver.findElement(By.id("cookies-list")).getText().split("\n"))
                .map(line -> line.split("=", 2))
                .filter(parts -> parts.length == 2)
                .forEach(parts -> shownCookies.put(parts[0].trim(), parts[1].trim()));
        return shownCookies;
    }

    private void checkDisplayedCookies(Set<Cookie> cookies, Map<String, String> shownCookies) {
        cookies.forEach(cookie -> {
            String key = cookie.getName();
            String expectedValue = cookie.getValue();
            String actualValue = shownCookies.get(key);
            assertNotNull(actualValue, "Cookie '" + key + "' is not shown on the page");
            assertEquals(expectedValue, actualValue, "Value for cookie '" + key + "' doesn't match");
        });
    }
}
