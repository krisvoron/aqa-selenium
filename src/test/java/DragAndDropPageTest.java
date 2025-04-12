import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import static data.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.*;

public class DragAndDropPageTest {
    WebDriver webDriver;
    Actions actions;
    private static final String DRAG_AND_DROP_PAGE = BASE_PAGE + "drag-and-drop.html";
    private static final String THE_SAME_LOCATION_ERROR_MESSAGE = "Locations of elements are the same";
    private static final String DRAG_AND_DROP_ERROR_MESSAGE = "The source and target locations after grag and drop are different";

    @BeforeEach
    void setup() {
        webDriver = new ChromeDriver();
        webDriver.get(DRAG_AND_DROP_PAGE);
        webDriver.manage().window().maximize();
        actions = new Actions(webDriver);
    }

    @AfterEach
    void end() {
        webDriver.quit();
    }

    @Test
    void dragAndDropTest() {
        WebElement source = webDriver.findElement(By.id("draggable"));
        WebElement target = webDriver.findElement(By.id("target"));
        Point defaultSourceLocation = source.getLocation();
        Point targetLocation = target.getLocation();
        actions.dragAndDrop(source, target).perform();
        Point newSourceLocation = source.getLocation();
        assertAll(
                () -> assertNotEquals(defaultSourceLocation, targetLocation, THE_SAME_LOCATION_ERROR_MESSAGE),
                () -> assertEquals(targetLocation, newSourceLocation, DRAG_AND_DROP_ERROR_MESSAGE)
        );
    }
}
