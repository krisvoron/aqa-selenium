package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static constants.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoadingImagesPageTest extends BaseTest{
    JavascriptExecutor js;
    private static final String LOADING_IMAGES_PAGE = BASE_PAGE + "loading-images.html";

    @BeforeEach
    void setup() {
        driver.get(LOADING_IMAGES_PAGE);
        js = (JavascriptExecutor) driver;
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "compass,img/compass.png",
            "calendar,img/calendar.png",
            "award,img/award.png",
            "landscape,img/landscape.png"
    })
    void imageLoadingTest(String id, String pngName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        WebElement compass = driver.findElement(By.id(id));
        boolean isLoaded = (boolean) js.executeScript("return arguments[0].complete && arguments[0].naturalWidth > 0", compass);
        assertAll(
                () -> assertTrue(isLoaded),
                () -> assertEquals(BASE_PAGE + pngName, compass.getDomProperty("src"))
        );
    }

    @Test
    void allImageIsLoadedTest() {
        String textBeforePicturesLoaded = driver.findElement(By.id("text")).getText();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("spinner")));
        List<WebElement> images = driver.findElement(By.id("image-container")).findElements(By.tagName("img"));
        String textAfterPicturesLoaded = driver.findElement(By.id("text")).getText();
        assertAll(
                () -> assertEquals("Please wait until the images are loaded...", textBeforePicturesLoaded),
                () -> assertEquals(4, images.size()),
                () -> assertEquals("Done!", textAfterPicturesLoaded)
        );
    }
}
