import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;

import java.util.List;

import static data.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DropDownMenuPageTest {
    WebDriver webDriver;
    Actions actions;
    private static final String DROP_DOWN_MENU_PAGE = BASE_PAGE + "dropdown-menu.html";
    private static final String MENU_LOCATOR = "./following-sibling::ul/li/a";
    private static final String BACKGROUND_COLOR = "background-color";
    private static final String TEXT = "text";
    private static final String TITLE_ERROR_MESSAGE = "The field name is incorrect";
    private static final String UNEXPECTED_VALUE_ERROR_MESSAGE = "The value is unexpected";
    private static final String LIST_SIZE_ERROR_MESSAGE = "The size of list is unexpected";
    private static final List<String> expectedMenu = List.of("Action", "Another action", "Something else here", "Separated link");

    @BeforeEach
    void setup() {
        webDriver = new ChromeDriver();
        webDriver.get(DROP_DOWN_MENU_PAGE);
        webDriver.manage().window().maximize();
        actions = new Actions(webDriver);
    }

    @AfterEach
    void end() {
        webDriver.quit();
    }

    @Test
    void leftClickTest() {
        WebElement leftClickDropDownButton = webDriver.findElement(By.id("my-dropdown-1"));
        String defaultBackgroundColor = toHex(leftClickDropDownButton.getCssValue(BACKGROUND_COLOR));
        List<WebElement> actualMenu = leftClickDropDownButton.findElements(By.xpath(MENU_LOCATOR));
        actions.moveToElement(leftClickDropDownButton).sendKeys(Keys.LEFT).perform();
        assertAll(
                () -> assertEquals("Use left-click here", leftClickDropDownButton.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("#000000", defaultBackgroundColor, UNEXPECTED_VALUE_ERROR_MESSAGE),

                () -> assertEquals("#0d6efd", toHex(leftClickDropDownButton.getCssValue(BACKGROUND_COLOR)),
                        UNEXPECTED_VALUE_ERROR_MESSAGE),
                () -> checkMenu(actualMenu)
        );
    }

    @Test
    void rightClickTest() {
        WebElement leftClickDropDownButton = webDriver.findElement(By.id("my-dropdown-2"));
        String defaultBackgroundColor = toHex(leftClickDropDownButton.getCssValue(BACKGROUND_COLOR));
        List<WebElement> actualMenu = leftClickDropDownButton.findElements(By.xpath(MENU_LOCATOR));
        actions.moveToElement(leftClickDropDownButton).sendKeys(Keys.RIGHT).perform();
        assertAll(
                () -> assertEquals("Use right-click here", leftClickDropDownButton.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("#000000", defaultBackgroundColor, UNEXPECTED_VALUE_ERROR_MESSAGE),

                () -> assertEquals("#198754", toHex(leftClickDropDownButton.getCssValue(BACKGROUND_COLOR)),
                        UNEXPECTED_VALUE_ERROR_MESSAGE),
                () -> checkMenu(actualMenu)
        );
    }

    @Test
    void doubleClickTest() {
        WebElement leftClickDropDownButton = webDriver.findElement(By.id("my-dropdown-3"));
        String defaultBackgroundColor = toHex(leftClickDropDownButton.getCssValue(BACKGROUND_COLOR));
        List<WebElement> actualMenu = leftClickDropDownButton.findElements(By.xpath(MENU_LOCATOR));
        actions.moveToElement(leftClickDropDownButton).doubleClick().perform();
        assertAll(
                () -> assertEquals("Use double-click here", leftClickDropDownButton.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("#000000", defaultBackgroundColor, UNEXPECTED_VALUE_ERROR_MESSAGE),

                () -> assertEquals("#dc3545", toHex(leftClickDropDownButton.getCssValue(BACKGROUND_COLOR)),
                        UNEXPECTED_VALUE_ERROR_MESSAGE),
                () -> checkMenu(actualMenu)
        );
    }

    private String toHex(String color) {
        return Color.fromString(color).asHex();
    }

    private void checkMenu(List<WebElement> actualMenu) {
        assertEquals(expectedMenu.size(), actualMenu.size(), LIST_SIZE_ERROR_MESSAGE);
        for (int i = 0; i < actualMenu.size(); i++) {
            String actualText = actualMenu.get(i).getDomProperty(TEXT);
            String expectedText = DropDownMenuPageTest.expectedMenu.get(i);
            assertEquals(expectedText, actualText, UNEXPECTED_VALUE_ERROR_MESSAGE);
        }
    }
}
