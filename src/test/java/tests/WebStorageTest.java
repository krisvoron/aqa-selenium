package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.LinkedHashMap;
import java.util.Map;

import static constants.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.TestUtils.toHex;

public class WebStorageTest extends BaseTest{
    Actions actions;
    JavascriptExecutor js;
    private static final String WEB_STORAGE_PAGE = BASE_PAGE + "web-storage.html";
    private static final String BACKGROUND_COLOR = "background-color";
    private static final String COLOR = "color";
    private static final String EMPTY_FIELD = "";

    @BeforeEach
    void setup() {
        driver.get(WEB_STORAGE_PAGE);
        actions = new Actions(driver);
        js = (JavascriptExecutor) driver;
    }

    @Test
    void checkColorChangesForDisplayLocalStorageButtonTest() {
        WebElement button = driver.findElement(By.id("display-local"));
        String actualButtonText = button.getText();
        String defaultBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
        String defaultTextColor = toHex(button.getCssValue(COLOR));

        actions.moveToElement(button).perform();

        wait.until(driver -> {
            String currentTextColor = toHex(button.getCssValue(BACKGROUND_COLOR));
            return !currentTextColor.equals(defaultBGColor);
        });
        assertAll(
                () -> assertEquals("Display local storage", actualButtonText),
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
    void checkColorChangesForDisplaySessionStorageButtonTest() {
        WebElement button = driver.findElement(By.id("display-session"));
        String actualButtonText = button.getText();
        String defaultBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
        String defaultTextColor = toHex(button.getCssValue(COLOR));

        actions.moveToElement(button).perform();

        wait.until(driver -> {
            String currentBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
            return !currentBGColor.equals(defaultBGColor);
        });
        assertAll(
                () -> assertEquals("Display session storage", actualButtonText),
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
    void clickDisplaySessionStorageButtonTest() {
        WebElement displaySessionStorageButton = driver.findElement(By.id("display-session"));
        String defaultSessionStorageList = driver.findElement(By.id("session-storage")).getText();
        String sessionStorage = getSessionStorageAsJson();

        displaySessionStorageButton.click();

        String shownSessionStorageList = driver.findElement(By.id("session-storage")).getText();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultSessionStorageList),
                () -> assertEquals(sessionStorage, shownSessionStorageList)
        );
    }

    @Test
    void clickDisplayLocalStorageButtonTest() {
        WebElement displayLocalStorageButton = driver.findElement(By.id("display-local"));
        String defaultLocalStorageList = driver.findElement(By.id("local-storage")).getText();
        String localStorage = getLocalStorageAsJson();

        displayLocalStorageButton.click();

        String shownLocalStorageList = driver.findElement(By.id("local-storage")).getText();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultLocalStorageList),
                () -> assertEquals(localStorage, shownLocalStorageList)
        );
    }

    @Test
    void addValuesToLocalStarageAndClickDisplayLocalStorageButtonTest() {
        WebElement displayLocalStorageButton = driver.findElement(By.id("display-local"));
        String defaultLocalStorageList = driver.findElement(By.id("local-storage")).getText();

        addValueToLocalStorage("key_1", "value_1");
        addValueToLocalStorage("key_2", "value_2");
        String localStorage = getLocalStorageAsJson();

        displayLocalStorageButton.click();

        String shownLocalStorageList = driver.findElement(By.id("local-storage")).getText();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultLocalStorageList),
                () -> assertEquals(localStorage, shownLocalStorageList)
        );
    }

    private String getLocalStorageAsJson() {
        Map<String, String> localStorageMap = new LinkedHashMap<>();
        long length = (Long) js.executeScript("return window.localStorage.length;");
        for (int i = 0; i < length; i++) {
            String key = (String) js.executeScript("return window.localStorage.key(arguments[0]);", i);
            String value = (String) js.executeScript("return window.localStorage.getItem(arguments[0]);", key);
            localStorageMap.put(key, value);
        }

        try {
            return new ObjectMapper().writeValueAsString(localStorageMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert localStorage to JSON", e);
        }
    }

    private String getSessionStorageAsJson() {
        Map<String, String> sessionStorageMap = new LinkedHashMap<>();
        long length = (Long) js.executeScript("return window.sessionStorage.length;");
        for (int i = 0; i < length; i++) {
            String key = (String) js.executeScript("return window.sessionStorage.key(arguments[0]);", i);
            String value = (String) js.executeScript("return window.sessionStorage.getItem(arguments[0]);", key);
            sessionStorageMap.put(key, value);
        }

        try {
            return new ObjectMapper().writeValueAsString(sessionStorageMap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert sessionStorage to JSON", e);
        }
    }

    private void addValueToLocalStorage(String key, String value) {
        long lengthBeforeAdd = (Long) js.executeScript("return window.localStorage.length;");
        js.executeScript(String.format(
                "window.localStorage.setItem('%s','%s');", key, value
        ));
        long lengthAfterAdd = (Long) js.executeScript("return window.localStorage.length;");
        assertEquals(lengthBeforeAdd + 1, lengthAfterAdd,
                "Something wrong with adding value to local storage");
    }
}
