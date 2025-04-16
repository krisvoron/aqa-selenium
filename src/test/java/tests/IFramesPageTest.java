package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static constants.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IFramesPageTest extends BaseTest{
    private static final String IFRAME_PAGE = BASE_PAGE + "iframes.html";
    private static final String FILE_NAME = "iframe_text.txt";

    @BeforeEach
    void setup() {
        driver.get(IFRAME_PAGE);
    }

    @Test
    void iframeTest() throws IOException {
        WebElement iframe = driver.findElement(By.id("my-iframe"));
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(FILE_NAME).getFile());
        String expectedContent = Files.readString(file.toPath());
        driver.switchTo().frame(iframe);
        String actualContent = driver.findElement(By.id("content")).getText();
        assertEquals(expectedContent, actualContent);
    }
}
