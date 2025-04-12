import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static data.Constants.BASE_PAGE;
import static data.Constants.HOME_PAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NavigationPageTest {
    WebDriver webDriver;
    private static final String HREF = "href";
    private static final String CLASS = "class";
    private static final String NAVIGATION_PAGE = BASE_PAGE + "navigation1.html";
    private static final String TITLE_ERROR_MESSAGE = "The field name is incorrect";
    private static final String CONTENT_ERROR_MESSAGE = "The page content is unexpected";
    private static final String UNEXPECTED_HREF_ERROR_MESSAGE = "The href is unexpected";
    private static final String UNEXPECTED_LINK_NAME_ERROR_MESSAGE = "The name of link is unexpected";
    private static final String ELEMENT_IS_NOT_ACTIVE_ERROR_MESSAGE = "The element is not active";
    private static final String ELEMENT_IS_ACTIVE_ERROR_MESSAGE = "The element should be disabled";

    @BeforeEach
    void setup() {
        webDriver = new ChromeDriver();
        webDriver.get(NAVIGATION_PAGE);
        webDriver.manage().window().maximize();
    }

    @AfterEach
    void end() {
        webDriver.quit();
    }

    @ParameterizedTest(name = "Page {0}")
    @MethodSource("checkPageContent")
    void pageContentTest(String page, String content) {
        String pageXPath = "//nav//a[@href='navigation" + page + ".html']";
        WebElement currentPage = webDriver.findElement(By.xpath(pageXPath));
        currentPage.click();
        WebElement pageTitle = webDriver.findElement(By.xpath("//h1[@class='display-6']"));
        WebElement pageText = webDriver.findElement(By.xpath("//p[@class='lead']"));
        WebElement backToIndex = webDriver.findElement(By.xpath("//a[@href='index.html']"));
        assertAll(
                () -> assertEquals("page-item active",
                        webDriver.findElement(By.xpath(pageXPath + "/..")).getDomAttribute(CLASS),
                        ELEMENT_IS_NOT_ACTIVE_ERROR_MESSAGE),
                () -> assertEquals("Navigation example", pageTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals(content, pageText.getText(), CONTENT_ERROR_MESSAGE),
                () -> assertEquals("Back to index", backToIndex.getText(), UNEXPECTED_LINK_NAME_ERROR_MESSAGE)
        );
    }

    @Test
    void checkDisabledPreviousButtonTest() {
        List<WebElement> pages = webDriver.findElements(
                By.xpath("//a[@class='page-link' and text()!='Next' and text()!='Previous']"));
        WebElement firstPage = getElementWithMinPage(pages);
        if(firstPage != null) firstPage.click();
        String previousXPath = "//a[text()='Previous']";
        WebElement previous = webDriver.findElement(By.xpath(previousXPath));
        assertAll(
                () -> assertEquals("#", previous.getDomAttribute(HREF), UNEXPECTED_HREF_ERROR_MESSAGE),
                () -> assertTrue(webDriver.findElement(By.xpath(previousXPath + "/.."))
                                .getDomAttribute(CLASS).contains("disabled"), ELEMENT_IS_ACTIVE_ERROR_MESSAGE)
        );
    }

    @Test
    void checkDisabledNextButtonTest() {
        List<WebElement> pages = webDriver.findElements(
                By.xpath("//a[@class='page-link' and text()!='Next' and text()!='Previous']"));
        WebElement lastPage = getElementWithMaxPage(pages);
        if(lastPage != null) lastPage.click();
        String nextXPath = "//a[text()='Next']";
        WebElement next = webDriver.findElement(By.xpath(nextXPath));
        assertAll(
                () -> assertEquals("#", next.getDomAttribute(HREF), UNEXPECTED_HREF_ERROR_MESSAGE),
                () -> assertTrue(webDriver.findElement(By.xpath(nextXPath + "/.."))
                        .getDomAttribute(CLASS).contains("disabled"), ELEMENT_IS_ACTIVE_ERROR_MESSAGE)
        );
    }

    @Test
    void checkActivePreviousButtonTest() {
        String pageXPath = "//nav//a[@href='navigation2.html']";
        WebElement page = webDriver.findElement(By.xpath(pageXPath));
        page.click();
        String previousXPath = "//a[text()='Previous']";
        WebElement previous = webDriver.findElement(By.xpath(previousXPath));
        assertAll(
                () -> assertEquals("navigation1.html", previous.getDomAttribute(HREF),
                        UNEXPECTED_HREF_ERROR_MESSAGE),
                () -> assertFalse(webDriver.findElement(By.xpath(previousXPath + "/.."))
                        .getDomAttribute(CLASS).contains("disabled"), ELEMENT_IS_ACTIVE_ERROR_MESSAGE)
        );
    }

    @Test
    void checkActiveNextButtonTest() {
        String pageXPath = "//nav//a[@href='navigation2.html']";
        WebElement page = webDriver.findElement(By.xpath(pageXPath));
        page.click();
        String nextXPath = "//a[text()='Next']";
        WebElement next = webDriver.findElement(By.xpath(nextXPath));
        assertAll(
                () -> assertEquals("navigation3.html", next.getDomAttribute(HREF),
                        UNEXPECTED_HREF_ERROR_MESSAGE),
                () -> assertFalse(webDriver.findElement(By.xpath(nextXPath + "/.."))
                        .getDomAttribute(CLASS).contains("disabled"), ELEMENT_IS_ACTIVE_ERROR_MESSAGE)
        );
    }

    @Test
    void checkBackToIndexLink() {
        WebElement backToIndex = webDriver.findElement(By.xpath("//a[@href='index.html']"));
        backToIndex.click();
        assertEquals(HOME_PAGE, webDriver.getCurrentUrl());
    }

    private static Stream<Arguments> checkPageContent() {
        return Stream.of(
                Arguments.of("1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."),
                Arguments.of("2", "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi " +
                        "ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit " +
                        "esse cillum dolore eu fugiat nulla pariatur."),
                Arguments.of("3", "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
                        "officia deserunt mollit anim id est laborum.")
        );
    }

    private static WebElement getElementWithMaxPage(List<WebElement> pages) {
        return pages.stream()
                .max(Comparator.comparingInt(e -> Integer.parseInt(e.getText().trim())))
                .orElse(null);
    }

    private static WebElement getElementWithMinPage(List<WebElement> pages) {
        return pages.stream()
                .min(Comparator.comparingInt(e -> Integer.parseInt(e.getText().trim())))
                .orElse(null);
    }
}
