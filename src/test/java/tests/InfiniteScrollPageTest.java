package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static constants.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InfiniteScrollPageTest extends BaseTest{
    JavascriptExecutor js;
    private static final String INFINITE_SCROLL_PAGE = BASE_PAGE + "infinite-scroll.html";

    @BeforeEach
    void setup() {
        driver.get(INFINITE_SCROLL_PAGE);
        js = (JavascriptExecutor) driver;
    }

    @Test
    void checkInfiniteScrollTest() {
        By pLocator = By.tagName("p");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        for (int i = 0; i < 5; i++){
            int initParagraphsNumber = driver.findElements(pLocator).size();
            WebElement lastParagraph = driver.findElements(pLocator).get(initParagraphsNumber - 1);
            String script = "arguments[0].scrollIntoView();";
            js.executeScript(script, lastParagraph);
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(pLocator, initParagraphsNumber));
            assertTrue(driver.findElements(pLocator).size() >= initParagraphsNumber + 20);
            i++;
        }
    }
}
