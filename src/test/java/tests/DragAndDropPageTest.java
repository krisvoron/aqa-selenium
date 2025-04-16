package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static constants.Constants.BASE_PAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DragAndDropPageTest extends BaseTest{
    Actions actions;
    private static final String DRAG_AND_DROP_PAGE = BASE_PAGE + "drag-and-drop.html";
    private static final String THE_SAME_LOCATION_ERROR_MESSAGE = "Locations of elements are the same";
    private static final String DRAG_AND_DROP_ERROR_MESSAGE = "The source and target locations after grag and drop are different";

    @BeforeEach
    void setup() {
        driver.get(DRAG_AND_DROP_PAGE);
        actions = new Actions(driver);
    }

    @Test
    void dragAndDropTest() {
        WebElement source = driver.findElement(By.id("draggable"));
        WebElement target = driver.findElement(By.id("target"));
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
