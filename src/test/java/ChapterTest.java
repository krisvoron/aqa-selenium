import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.stream.Stream;

import static data.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChapterTest {
    WebDriver webDriver;
    private static final String chaptersXPath = "//div[@class='card-body']";
    private static final String titleXPath = "//h1[contains(@class, 'display-6')]";

    @BeforeEach
    void setup(){
        webDriver = new ChromeDriver();
        webDriver.get(BASE_PAGE);
        webDriver.manage().window().maximize();
    }

    @AfterEach
    void end(){
       webDriver.quit();
    }



    @ParameterizedTest(name = "{0}")
    @MethodSource("testData")
    @DisplayName("Проверка ссылок на https://bonigarcia.dev/:")
    void chapterTest(String expectedLinkName, String hrefValue, String expectedPageTitle, String chapterName){
        WebElement link = webDriver.findElement(By.xpath(getXPath(chapterName, hrefValue)));
        String actualLinkName = link.getText();
        link.click();
        String actualUrl = webDriver.getCurrentUrl();
        String actualPageTitle = webDriver.findElements(By.name("frame-header")).isEmpty() ?
                webDriver.findElement(By.xpath(titleXPath)).getText() :
                webDriver.switchTo().frame("frame-header").findElement(By.xpath(titleXPath)).getText();
        Assertions.assertAll(
                () -> assertEquals(expectedLinkName, actualLinkName, "Link name does not match the expected value"),
                () -> assertEquals(BASE_PAGE + hrefValue, actualUrl, "The URL does not match the expected value"),
                () -> assertEquals(expectedPageTitle, actualPageTitle, "Page title does not match the expected value")
        );
    }

    private static Stream<Arguments> testData(){
        return Stream.of(
                Arguments.of("Web form", "web-form.html", "Web form", "Chapter 3. WebDriver Fundamentals"),
                Arguments.of("Navigation", "navigation1.html", "Navigation example", "Chapter 3. WebDriver Fundamentals"),
                Arguments.of("Dropdown menu", "dropdown-menu.html", "Dropdown menu", "Chapter 3. WebDriver Fundamentals"),
                Arguments.of("Mouse over", "mouse-over.html", "Mouse over", "Chapter 3. WebDriver Fundamentals"),
                Arguments.of("Drag and drop", "drag-and-drop.html", "Drag and drop", "Chapter 3. WebDriver Fundamentals"),
                Arguments.of("Draw in canvas", "draw-in-canvas.html", "Drawing in canvas", "Chapter 3. WebDriver Fundamentals"),
                Arguments.of("Loading images", "loading-images.html", "Loading images", "Chapter 3. WebDriver Fundamentals"),
                Arguments.of("Slow calculator", "slow-calculator.html", "Slow calculator", "Chapter 3. WebDriver Fundamentals"),

                Arguments.of("Long page", "long-page.html", "This is a long page", "Chapter 4. Browser-Agnostic Features"),
                Arguments.of("Infinite scroll", "infinite-scroll.html", "Infinite scroll", "Chapter 4. Browser-Agnostic Features"),
                Arguments.of("Shadow DOM", "shadow-dom.html", "Shadow DOM", "Chapter 4. Browser-Agnostic Features"),
                Arguments.of("Cookies", "cookies.html", "Cookies", "Chapter 4. Browser-Agnostic Features"),
                Arguments.of("Frames", "frames.html", "Frames", "Chapter 4. Browser-Agnostic Features"),
                Arguments.of("IFrames", "iframes.html", "IFrame", "Chapter 4. Browser-Agnostic Features"),
                Arguments.of("Dialog boxes", "dialog-boxes.html", "Dialog boxes", "Chapter 4. Browser-Agnostic Features"),
                Arguments.of("Web storage", "web-storage.html", "Web storage", "Chapter 4. Browser-Agnostic Features"),

                Arguments.of("Geolocation", "geolocation.html", "Geolocation", "Chapter 5. Browser-Specific Manipulation"),
                Arguments.of("Notifications", "notifications.html", "Notifications", "Chapter 5. Browser-Specific Manipulation"),
                Arguments.of("Get user media", "get-user-media.html", "Get user media", "Chapter 5. Browser-Specific Manipulation"),
                Arguments.of("Multilanguage", "multilanguage.html", "Multilanguage page", "Chapter 5. Browser-Specific Manipulation"),
                Arguments.of("Console logs", "console-logs.html", "Console logs", "Chapter 5. Browser-Specific Manipulation"),

                Arguments.of("Login form", "login-form.html", "Login form", "Chapter 7. The Page Object Model (POM)"),
                Arguments.of("Slow login", "login-slow.html", "Slow login form", "Chapter 7. The Page Object Model (POM)"),

                Arguments.of("Random calculator", "random-calculator.html", "Random calculator", "Chapter 8. Testing Framework Specifics"),

                Arguments.of("Download files", "download.html", "Download files", "Chapter 9. Third-Party Integrations"),
                Arguments.of("A/B Testing", "ab-testing.html", "A/B Testing", "Chapter 9. Third-Party Integrations"),
                Arguments.of("Data types", "data-types.html", "Data types", "Chapter 9. Third-Party Integrations")
        );
    }

    private String getXPath(String chapterName, String href){
        return chaptersXPath + "/h5[text()='" + chapterName + "']/../a[@href='" + href + "']";
    }
}
