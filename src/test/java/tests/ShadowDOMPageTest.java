package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import static constants.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShadowDOMPageTest extends BaseTest{
    private static final String SHADOW_DOM_PAGE = BASE_PAGE + "shadow-dom.html";

    @BeforeEach
    void setup() {
        driver.get(SHADOW_DOM_PAGE);
    }

    @Test
    void shadowDOMTest() {
        WebElement shadowContent = driver.findElement(By.id("content"));
        SearchContext shadowRoot = shadowContent.getShadowRoot();
        WebElement textElement = shadowRoot.findElement(By.cssSelector("p"));
        assertEquals("Hello Shadow DOM", textElement.getText());
    }
}
