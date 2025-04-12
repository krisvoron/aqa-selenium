import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;


import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

import static data.Constants.BASE_PAGE;
import static data.Constants.HOME_PAGE;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebFormPageTest {
    WebDriver webDriver;
    Actions actions;
    JavascriptExecutor js;
    private static final String WEB_FORM_PAGE = BASE_PAGE + "web-form.html";
    private static final String SUBMIT_FORM = BASE_PAGE + "submitted-form.html";
    private static final String FILE_NAME = "test.txt";
    private static final String FIELD_NAME_PATH = "./ancestor::label";
    private static final String VALUE = "value";
    private static final String SCROLL_HEIGHT = "scrollHeight";
    private static final String CLIENT_HEIGHT = "clientHeight";
    private static final String PLACEHOLDER = "placeholder";
    private static final String TYPE = "type";
    private static final String EMPTY_FIELD = "";
    private static final String SIMPLE_TEXT = "Test_123$";
    private static final String TITLE_ERROR_MESSAGE = "The field name is incorrect";
    private static final String FIELD_NOT_EMPTY_ERROR_MESSAGE = "The field must be empty";
    private static final String INPUT_ERROR_MESSAGE = "Value should be shown in the field";
    private static final String SCROLL_ERROR_MESSAGE = "Scroll appearance is unexpected";
    private static final String DISABLED_FIELD_ERROR_MESSAGE = "The field should be disabled";
    private static final String READ_ONLY_FIELD_ERROR_MESSAGE = "The field should be read only";
    private static final String ELEMENT_SHOWN_ERROR_MESSAGE = "The element should be shown on the page";
    private static final String DEFAULT_TEXT_ERROR_MESSAGE = "Default placeholder/value is unexpected";
    private static final String URL_ERROR_MESSAGE = "The URL does not match the expected value";
    private static final String LIST_SIZE_ERROR_MESSAGE = "The size of list is unexpected";
    private static final String IS_NOT_SELECTED_ERROR_MESSAGE = "The element is not selected";
    private static final String SELECTED_ERROR_MESSAGE = "The element is selected";
    private static final String UNEXPECTED_VALUE_ERROR_MESSAGE = "The value is unexpected";
    private static final String UNEXPECTED_TYPE_ERROR_MESSAGE = "The type of the attribute is unexpected";

    @BeforeEach
    void setup() {
        webDriver = new ChromeDriver();
        webDriver.get(WEB_FORM_PAGE);
        webDriver.manage().window().maximize();
        actions = new Actions(webDriver);
        js = (JavascriptExecutor) webDriver;
    }

    @AfterEach
    void end() {
        webDriver.quit();
    }

    @Test
    void setValueInTextInputTest() {
        WebElement textInputField = webDriver.findElement(By.id("my-text-id"));
        WebElement textInputTitle = textInputField.findElement(By.xpath(FIELD_NAME_PATH));
        textInputField.sendKeys(SIMPLE_TEXT);
        assertAll(
                () -> assertEquals("Text input", textInputTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals(SIMPLE_TEXT, textInputField.getDomProperty(VALUE), INPUT_ERROR_MESSAGE)
        );
    }

    @Test
    void clearTextInputTest() {
        WebElement textInputField = webDriver.findElement(By.id("my-text-id"));
        textInputField.sendKeys(SIMPLE_TEXT);
        textInputField.clear();
        assertEquals(EMPTY_FIELD, textInputField.getDomProperty(VALUE), FIELD_NOT_EMPTY_ERROR_MESSAGE);
    }

    @Test
    void setValueInPasswordInputTest() {
        WebElement pswInputField = webDriver.findElement(By.name("my-password"));
        WebElement pswInputTitle = pswInputField.findElement(By.xpath(FIELD_NAME_PATH));
        pswInputField.sendKeys(SIMPLE_TEXT);
        assertAll(
                () -> assertEquals("Password", pswInputTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals(SIMPLE_TEXT, pswInputField.getDomProperty(VALUE), INPUT_ERROR_MESSAGE)
        );
    }

    @Test
    void clearPasswordInputTest() {
        WebElement pswInputField = webDriver.findElement(By.name("my-password"));
        pswInputField.sendKeys(SIMPLE_TEXT);
        pswInputField.clear();
        assertEquals(EMPTY_FIELD, pswInputField.getDomProperty(VALUE), FIELD_NOT_EMPTY_ERROR_MESSAGE);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("textAreaTestData")
    void setValueInTextAreaInputTest(String description, String textValue, Boolean scrollIsEnabled) {
        WebElement textAreaInputField = webDriver.findElement(By.name("my-textarea"));
        WebElement textAreaInputTitle = textAreaInputField.findElement(By.xpath(FIELD_NAME_PATH));
        textAreaInputField.sendKeys(textValue);
        assertAll(
                () -> assertEquals("Textarea", textAreaInputTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals(textValue, textAreaInputField.getDomProperty(VALUE), INPUT_ERROR_MESSAGE),
                () -> assertEquals(scrollIsEnabled,
                        Integer.valueOf(textAreaInputField.getDomProperty(SCROLL_HEIGHT)) >
                                Integer.valueOf(textAreaInputField.getDomProperty(CLIENT_HEIGHT)),
                        SCROLL_ERROR_MESSAGE)
        );
    }

    @Test
    void clearTextAreaInputTest() {
        WebElement textAreaInputField = webDriver.findElement(By.name("my-textarea"));
        textAreaInputField.sendKeys("wfefefe\nefefe\ntdwfwfw\nt");
        textAreaInputField.clear();
        assertAll(
                () -> assertEquals(EMPTY_FIELD, textAreaInputField.getDomProperty(VALUE), INPUT_ERROR_MESSAGE),
                () -> assertFalse(Integer.valueOf(textAreaInputField.getDomProperty(SCROLL_HEIGHT)) >
                        Integer.valueOf(textAreaInputField.getDomProperty(CLIENT_HEIGHT)),
                        SCROLL_ERROR_MESSAGE)
        );
    }

    @Test
    void disabledInputTest() {
        WebElement disabledInputField = webDriver.findElement(By.name("my-disabled"));
        WebElement disabledInputTitle = disabledInputField.findElement(By.xpath(FIELD_NAME_PATH));
        assertAll(
                () -> assertEquals("Disabled input", disabledInputTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertFalse(disabledInputField.isEnabled(), DISABLED_FIELD_ERROR_MESSAGE),
                () -> assertEquals("Disabled input", disabledInputField.getDomAttribute(PLACEHOLDER),
                        DEFAULT_TEXT_ERROR_MESSAGE)
        );
    }

    @Test
    void readonlyInputTest() {
        WebElement readonlyInputField = webDriver.findElement(By.name("my-readonly"));
        WebElement readonlyInputTitle = readonlyInputField.findElement(By.xpath(FIELD_NAME_PATH));
        readonlyInputField.sendKeys(SIMPLE_TEXT);
        assertAll(
                () -> assertEquals("Readonly input", readonlyInputTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertNotNull(readonlyInputField.getDomAttribute("readonly"), READ_ONLY_FIELD_ERROR_MESSAGE),
                () -> assertEquals("Readonly input", readonlyInputField.getDomAttribute(VALUE),
                        DEFAULT_TEXT_ERROR_MESSAGE)
        );
    }

    @Test
    void returnToIndexTest() {
        WebElement returnToIndexLink = webDriver.findElement(By.linkText("Return to index"));
        returnToIndexLink.click();
        assertEquals(HOME_PAGE, webDriver.getCurrentUrl(), URL_ERROR_MESSAGE);
    }

    @Test
    void dropdownSelectDefaultViewTest() {
        WebElement dropdownSelectField = webDriver.findElement(By.name("my-select"));
        WebElement dropdownSelectInputTitle = dropdownSelectField.findElement(By.xpath(FIELD_NAME_PATH));
        Select select = new Select(dropdownSelectField);
        WebElement selectedOption = select.getFirstSelectedOption();
        List<WebElement> optionList = select.getOptions();
        List<String> expectedOptionList = List.of("Open this select menu", "One", "Two", "Three");
        assertAll(
                () -> assertEquals("Dropdown (select)",
                        dropdownSelectInputTitle.getText().split("\n")[0].trim(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("Open this select menu", selectedOption.getText(),
                        DEFAULT_TEXT_ERROR_MESSAGE),
                () -> assertEquals(expectedOptionList.size(), optionList.size(), LIST_SIZE_ERROR_MESSAGE),
                () -> {
                    for (int i = 0; i < optionList.size(); i++) {
                        String actualText = optionList.get(i).getText();
                        String expectedText = expectedOptionList.get(i);
                        assertEquals(expectedText, actualText, UNEXPECTED_VALUE_ERROR_MESSAGE);
                    }
                }
        );
    }

    @Test
    void dropdownSelectTest() {
        WebElement dropdownSelectField = webDriver.findElement(By.name("my-select"));
        Select select = new Select(dropdownSelectField);
        List<WebElement> optionList = select.getOptions();
        for (int i = 0; i< optionList.size(); i++) {
            select.selectByIndex(i);
            assertTrue(optionList.get(i).isSelected(), IS_NOT_SELECTED_ERROR_MESSAGE);
        }
    }

    @Test
    void dropdownDataListDefaultViewTest() {
        WebElement dropdownDataListInput = webDriver.findElement(By.name("my-datalist"));
        WebElement dropdownDataListInputTitle = dropdownDataListInput.findElement(By.xpath(FIELD_NAME_PATH));
        List<WebElement> optionList = dropdownDataListInput.findElements(By.xpath("//following-sibling::datalist/option"));
        List<String> expectedOptionList = List.of("San Francisco", "New York", "Seattle", "Los Angeles", "Chicago");
        assertAll(
                () -> assertEquals("Dropdown (datalist)",
                        dropdownDataListInputTitle.getText().split("\n")[0].trim(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("Type to search...",
                        dropdownDataListInput.getDomAttribute(PLACEHOLDER), DEFAULT_TEXT_ERROR_MESSAGE),
                () -> assertEquals(expectedOptionList.size(), optionList.size(), LIST_SIZE_ERROR_MESSAGE),
                () -> {
                    for (int i = 0; i < optionList.size(); i++) {
                        String actualText = optionList.get(i).getDomAttribute(VALUE);
                        String expectedText = expectedOptionList.get(i);
                        assertEquals(expectedText, actualText, UNEXPECTED_VALUE_ERROR_MESSAGE);
                    }
                }
        );
    }

    @ParameterizedTest
    @CsvSource({"San F,San Francisco",
            " yorK,New York",
            "att,Seattle",
            "Ange,Los Angeles",
            "AGO, Chicago"
    })
    void dropdownDataListTest(String inputValue, String fieldValue) {
        WebElement dropdownDataListInput = webDriver.findElement(By.name("my-datalist"));
        dropdownDataListInput.sendKeys(inputValue);
        js.executeScript("arguments[0].value = '" + fieldValue +
                "'; arguments[0].dispatchEvent(new Event('change'));", dropdownDataListInput);
        assertEquals(fieldValue, dropdownDataListInput.getDomProperty(VALUE), UNEXPECTED_VALUE_ERROR_MESSAGE);
    }

    @Test
    void dropdownDataListClearTest() {
        WebElement dropdownDataListInput = webDriver.findElement(By.name("my-datalist"));
        var inputValue = "San F";
        var fieldValue = "San Francisco";
        dropdownDataListInput.sendKeys(inputValue);
        js.executeScript("arguments[0].value = '" + fieldValue +
                "'; arguments[0].dispatchEvent(new Event('change'));", dropdownDataListInput);
        dropdownDataListInput.clear();
        assertEquals(EMPTY_FIELD, dropdownDataListInput.getDomProperty(VALUE), FIELD_NOT_EMPTY_ERROR_MESSAGE);
    }

    @Test
    void fileInputDefaultViewTest() {
        WebElement fileInputField = webDriver.findElement(By.name("my-file"));
        WebElement fileInputTitle = fileInputField.findElement(By.xpath(FIELD_NAME_PATH));
        assertAll(
                () -> assertEquals("File input", fileInputTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("file", fileInputField.getDomAttribute(TYPE), UNEXPECTED_TYPE_ERROR_MESSAGE),
                () -> assertEquals(EMPTY_FIELD, fileInputField.getDomProperty(VALUE), FIELD_NOT_EMPTY_ERROR_MESSAGE)
        );
    }

    @Test
    void setFileInFileInputTest() {
        WebElement fileInputField = webDriver.findElement(By.name("my-file"));
        WebElement submitButton = webDriver.findElement(By.xpath("//button[@type='submit']"));
        URL url = WebFormPageTest.class.getClassLoader().getResource(FILE_NAME);
        File file = new File(url.getPath());
        fileInputField.sendKeys(file.getAbsolutePath());
        boolean value = fileInputField.getDomProperty(VALUE).contains(FILE_NAME);
        submitButton.submit();
        assertAll(
                () -> assertTrue(value, UNEXPECTED_VALUE_ERROR_MESSAGE),
                () -> assertTrue(webDriver.getCurrentUrl().startsWith(SUBMIT_FORM), URL_ERROR_MESSAGE),
                () -> assertTrue(webDriver.getCurrentUrl().contains("my-file=" + FILE_NAME), URL_ERROR_MESSAGE),
                () -> assertNotNull(webDriver.findElement(By.xpath("//h1[text()='Form submitted']")),
                        ELEMENT_SHOWN_ERROR_MESSAGE)
        );
    }

    @Test
    void uncheckCheckboxTest() {
        WebElement checkbox = webDriver.findElement(By.id("my-check-1"));
        WebElement checkboxTitle = checkbox.findElement(By.xpath(FIELD_NAME_PATH));
        boolean defaultCheckboxState = checkbox.isSelected();
        checkbox.click();
        assertAll(
                () -> assertEquals("Checked checkbox", checkboxTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertTrue(defaultCheckboxState, IS_NOT_SELECTED_ERROR_MESSAGE),
                () -> assertFalse(checkbox.isSelected(), SELECTED_ERROR_MESSAGE)
        );
    }


    @Test
    void checkCheckboxTest() {
        WebElement checkbox = webDriver.findElement(By.id("my-check-1"));
        checkbox.click();
        checkbox.click();
        assertAll(
                () -> assertTrue(checkbox.isSelected(), IS_NOT_SELECTED_ERROR_MESSAGE)
        );
    }

    @Test
    void checkDefaultCheckboxTest() {
        WebElement checkbox = webDriver.findElement(By.id("my-check-2"));
        WebElement checkboxTitle = checkbox.findElement(By.xpath(FIELD_NAME_PATH));
        boolean defaultCheckboxState = checkbox.isSelected();
        checkbox.click();
        assertAll(
                () -> assertEquals("Default checkbox", checkboxTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertFalse(defaultCheckboxState, SELECTED_ERROR_MESSAGE),
                () -> assertTrue(checkbox.isSelected(), IS_NOT_SELECTED_ERROR_MESSAGE)
        );
    }

    @Test
    void uncheckDefaultCheckboxTest() {
        WebElement checkbox = webDriver.findElement(By.id("my-check-2"));
        checkbox.click();
        checkbox.click();
        assertAll(
                () -> assertFalse(checkbox.isSelected(), SELECTED_ERROR_MESSAGE)
        );
    }

    @Test
    void chooseDefaultRatioTest() {
        WebElement checkedRatio = webDriver.findElement(By.id("my-radio-1"));
        WebElement defaultRatio = webDriver.findElement(By.id("my-radio-2"));
        WebElement checkedRatioTitle = checkedRatio.findElement(By.xpath(FIELD_NAME_PATH));
        WebElement defaultRatioTitle = defaultRatio.findElement(By.xpath(FIELD_NAME_PATH));
        boolean checkedRatioState = checkedRatio.isSelected();
        boolean defaultRatioState = defaultRatio.isSelected();
        defaultRatio.click();
        assertAll(
                () -> assertTrue(checkedRatioState, IS_NOT_SELECTED_ERROR_MESSAGE),
                () -> assertFalse(checkedRatio.isSelected(), SELECTED_ERROR_MESSAGE),
                () -> assertFalse(defaultRatioState, SELECTED_ERROR_MESSAGE),
                () -> assertTrue(defaultRatio.isSelected(), IS_NOT_SELECTED_ERROR_MESSAGE),
                () -> assertEquals("Checked radio", checkedRatioTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("Default radio", defaultRatioTitle.getText(), TITLE_ERROR_MESSAGE)
        );
    }

    @Test
    void chooseCheckedRatioTest() {
        WebElement checkedRatio = webDriver.findElement(By.id("my-radio-1"));
        WebElement defaultRatio = webDriver.findElement(By.id("my-radio-2"));
        defaultRatio.click();
        checkedRatio.click();
        assertAll(
                () -> assertTrue(checkedRatio.isSelected(), IS_NOT_SELECTED_ERROR_MESSAGE),
                () -> assertFalse(defaultRatio.isSelected(), SELECTED_ERROR_MESSAGE)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("colorTestData")
    void changeColorInColorPickerTest(String description, Color color, String hex) {
        WebElement colorPickerInput = webDriver.findElement(By.name("my-colors"));
        WebElement colorPickerTitle = colorPickerInput.findElement(By.xpath(FIELD_NAME_PATH));
        String actualDefaultColor = colorPickerInput.getDomAttribute(VALUE);
        String defaultColor = "#563d7c";
        String script = "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));";
        js.executeScript(script, colorPickerInput, color.asHex());
        assertAll(
                () -> assertEquals("Color picker", colorPickerTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("color", colorPickerInput.getDomAttribute(TYPE), UNEXPECTED_TYPE_ERROR_MESSAGE),
                () -> assertEquals(defaultColor, actualDefaultColor, UNEXPECTED_VALUE_ERROR_MESSAGE),
                () -> assertEquals(hex, colorPickerInput.getDomProperty(VALUE), UNEXPECTED_VALUE_ERROR_MESSAGE)
        );
    }

    @Test
    void setTextDataInDataPickerTest() {
        String inputDate = String.valueOf(LocalDate.now().getDayOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String expectedDate = LocalDate.now().format(formatter);

        WebElement datePickerInput = webDriver.findElement(By.name("my-date"));
        WebElement datePickerTitle = datePickerInput.findElement(By.xpath(FIELD_NAME_PATH));
        String getDefaultDatePickerValue = datePickerInput.getDomAttribute(VALUE);
        datePickerInput.click();
        datePickerInput.sendKeys(inputDate);
        datePickerInput.sendKeys(Keys.ENTER);
        assertAll(
                () -> assertEquals("Date picker", datePickerTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("text", datePickerInput.getDomAttribute(TYPE), UNEXPECTED_TYPE_ERROR_MESSAGE),
                () -> assertNull(getDefaultDatePickerValue, FIELD_NOT_EMPTY_ERROR_MESSAGE),
                () -> assertEquals(expectedDate, datePickerInput.getDomProperty(VALUE), UNEXPECTED_VALUE_ERROR_MESSAGE)
        );
    }

    @Test
    void defaultExampleRangeViewTest() {
        WebElement rangeInput = webDriver.findElement(By.name("my-range"));
        WebElement rangeTitle = rangeInput.findElement(By.xpath(FIELD_NAME_PATH));
        assertAll(
                () -> assertEquals("Example range", rangeTitle.getText(), TITLE_ERROR_MESSAGE),
                () -> assertEquals("5", rangeInput.getDomAttribute(VALUE), UNEXPECTED_VALUE_ERROR_MESSAGE),
                () -> assertEquals("range", rangeInput.getDomAttribute(TYPE), UNEXPECTED_TYPE_ERROR_MESSAGE)
        );
    }

    @Test
    void changeValueByKeyArrowRightInExampleRangeTest() {
        WebElement rangeInput = webDriver.findElement(By.name("my-range"));
        actions.moveToElement(rangeInput).click().sendKeys(Keys.ARROW_RIGHT).perform();
        assertEquals("6", rangeInput.getDomProperty(VALUE), UNEXPECTED_VALUE_ERROR_MESSAGE);
    }

    @Test
    void changeValueByKeyArrowLeftInExampleRangeTest() {
        WebElement rangeInput = webDriver.findElement(By.name("my-range"));
        actions.moveToElement(rangeInput).click().sendKeys(Keys.ARROW_LEFT).perform();
        assertEquals("4", rangeInput.getDomProperty(VALUE), UNEXPECTED_VALUE_ERROR_MESSAGE);
    }

    @Test
    void changeValueByMouseInExampleRangeTest() {
        WebElement rangeInput = webDriver.findElement(By.name("my-range"));
        int width = rangeInput.getSize().getWidth();
        int x = rangeInput.getLocation().getX();
        int y = rangeInput.getLocation().getY();
        for (int i = 0; i < 10; i++) {
            actions.moveToElement(rangeInput)
                    .clickAndHold()
                    .moveToLocation(x + width / 10 * i, y)
                    .release().perform();
            assertEquals(String.valueOf(i), rangeInput.getDomProperty(VALUE), UNEXPECTED_VALUE_ERROR_MESSAGE);
        }
    }

    private static Stream<Arguments> textAreaTestData() {
        return Stream.of(
                Arguments.of("Text without scroll", "wfefefe\nefefe\ntdwfwfw", false),
                Arguments.of("Text with scroll", "wfefefe\nefefe\ntdwfwfw\nt", true)
        );
    }

    private static Stream<Arguments> colorTestData() {
        return Stream.of(
                Arguments.of("Set red", new Color(100, 0, 0, 1), "#640000"),
                Arguments.of("Set blue", new Color(0, 0, 100, 1), "#000064"),
                Arguments.of("Set green", new Color(0, 100, 0, 1), "#006400"),
                Arguments.of("Set mixed of red, blue and green", new Color(100, 100, 100, 1), "#646464")
        );
    }
}
