package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.stream.Stream;

import static constants.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.TestUtils.toHex;

public class DialogBoxesPageTest extends BaseTest {
    JavascriptExecutor js;
    Actions actions;
    private static final String DIALOG_BOXES_PAGE = BASE_PAGE + "dialog-boxes.html";
    private static final String BACKGROUND_COLOR = "background-color";
    private static final String COLOR = "color";
    private static final String EMPTY_FIELD = "";

    @BeforeEach
    void setup() {
        driver.get(DIALOG_BOXES_PAGE);
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("checkColorChangesUnderMouseTestData")
    void checkColorChangesUnderMouseTest(String id,
                                         String expectedButtonText,
                                        String expectedDefaultTextColor,
                                        String expectedBGColor,
                                        String expectedTextColor) {
        WebElement button = driver.findElement(By.id(id));
        String actualButtonText = button.getText();
        String defaultBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
        String defaultTextColor = toHex(button.getCssValue(COLOR));

        actions.moveToElement(button).perform();
        wait.until(driver -> {
            String currentBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
            return !currentBGColor.equals(defaultBGColor);
        });
        assertAll(
                () -> assertEquals(expectedButtonText, actualButtonText),
                () -> assertEquals("#000000", defaultBGColor),
                () -> assertEquals(expectedDefaultTextColor, defaultTextColor),
                () -> {
                    String actualBGColor = toHex(button.getCssValue(BACKGROUND_COLOR));
                    assertEquals(expectedBGColor, actualBGColor);
                },
                () -> {
                    String actualTextColor = toHex(button.getCssValue(COLOR));
                    assertEquals(expectedTextColor, actualTextColor);
                }
        );
    }

    @Test
    void alertTest() {
        WebElement button = driver.findElement(By.id("my-alert"));
        button.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert();
        String text = alert.getText();

        alert.accept();
        driver.switchTo().defaultContent();
        assertAll(
                () -> assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert()),
                () -> assertEquals("Hello world!", text)
        );
    }

    @Test
    void confirmOkTest() {
        WebElement button = driver.findElement(By.id("my-confirm"));
        String defaultConfirmText = driver.findElement(By.id("confirm-text")).getText();
        button.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert();
        String text = alert.getText();

        alert.accept();
        driver.switchTo().defaultContent();
        String actualConfirmText = driver.findElement(By.id("confirm-text")).getText();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultConfirmText),
                () -> assertEquals("Is this correct?", text),
                () -> assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert()),
                () -> assertEquals("You chose: true", actualConfirmText)
        );
    }

    @Test
    void confirmCancelTest() {
        WebElement button = driver.findElement(By.id("my-confirm"));
        String defaultConfirmText = driver.findElement(By.id("confirm-text")).getText();
        button.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert();

        alert.dismiss();
        driver.switchTo().defaultContent();
        String actualConfirmText = driver.findElement(By.id("confirm-text")).getText();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultConfirmText),
                () -> assertEquals("You chose: false", actualConfirmText),
                () -> assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert())
        );
    }

    @Test
    void promptOkTest() {
        WebElement button = driver.findElement(By.id("my-prompt"));
        String defaultPromptText = driver.findElement(By.id("prompt-text")).getText();
        button.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert();
        String text = alert.getText();
        String inputText = "text";
        alert.sendKeys(inputText);

        alert.accept();
        driver.switchTo().defaultContent();
        String actualPromptText = driver.findElement(By.id("prompt-text")).getText();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultPromptText),
                () -> assertEquals("Please enter your name", text),
                () -> assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert()),
                () -> assertEquals("You typed: " + inputText, actualPromptText)
        );
    }

    @Test
    void promptCancelTest() {
        WebElement button = driver.findElement(By.id("my-prompt"));
        String defaultPromptText = driver.findElement(By.id("prompt-text")).getText();
        button.click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert();

        alert.dismiss();
        driver.switchTo().defaultContent();
        String actualPromptText = driver.findElement(By.id("prompt-text")).getText();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultPromptText),
                () -> assertThrows(NoAlertPresentException.class, () -> driver.switchTo().alert()),
                () -> assertEquals("You typed: null", actualPromptText)
        );
    }

    @Test
    void modalDefaultViewTest() {
        WebElement button = driver.findElement(By.id("my-modal"));
        String defaultModalText = driver.findElement(By.id("modal-text")).getText();

        button.click();
        wait.until(ExpectedConditions.domAttributeToBe(
                driver.findElement(By.id("example-modal")), "aria-modal", "true"));
        WebElement modalTitle = driver.findElement(By.id("exampleModalLabel"));
        WebElement modalBody = driver.findElement(By.cssSelector("div.modal-body"));
        WebElement closeButton = driver.findElement(By.cssSelector("button.btn.btn-secondary.model-button"));
        WebElement saveButton = driver.findElement(By.cssSelector("button.btn.btn-primary.model-button"));
        assertAll(
                () -> assertEquals(EMPTY_FIELD, defaultModalText),
                () -> assertEquals("Modal title", modalTitle.getText()),
                () -> assertEquals("This is the modal body", modalBody.getText()),
                () -> assertEquals("Close", closeButton.getText()),
                () -> assertEquals("Save changes", saveButton.getText())
        );
    }

    @Test
    void modalSaveTest() {
        WebElement button = driver.findElement(By.id("my-modal"));
        button.click();
        wait.until(ExpectedConditions.domAttributeToBe(
                driver.findElement(By.id("example-modal")), "aria-modal", "true"));
        WebElement saveButton = driver.findElement(By.cssSelector("button.btn.btn-primary.model-button"));

        saveButton.click();
        wait.until(ExpectedConditions.domAttributeToBe(
                driver.findElement(By.id("example-modal")), "aria-hidden", "true"));
        String actualModalText = driver.findElement(By.id("modal-text")).getText();
        assertEquals("You chose: Save changes", actualModalText);
    }

    @Test
    void modalCloseTest() {
        WebElement button = driver.findElement(By.id("my-modal"));
        button.click();
        wait.until(ExpectedConditions.domAttributeToBe(
                driver.findElement(By.id("example-modal")), "aria-modal", "true"));
        WebElement closeButton = driver.findElement(By.cssSelector("button.btn.btn-secondary.model-button"));

        closeButton.click();
        wait.until(ExpectedConditions.domAttributeToBe(
                driver.findElement(By.id("example-modal")), "aria-hidden", "true"));
        String actualModalText = driver.findElement(By.id("modal-text")).getText();
        assertEquals("You chose: Close", actualModalText);
    }


    private static Stream<Arguments> checkColorChangesUnderMouseTestData() {
        return Stream.of(
                Arguments.of("my-alert", "Launch alert", "#0d6efd", "#0d6efd", "#ffffff"),
                Arguments.of("my-confirm", "Launch confirm", "#198754", "#198754", "#ffffff"),
                Arguments.of("my-prompt", "Launch prompt", "#dc3545", "#dc3545", "#ffffff"),
                Arguments.of("my-modal", "Launch modal", "#ffc107", "#ffc107", "#000000")
        );
    }
}
