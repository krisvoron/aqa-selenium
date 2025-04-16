package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class SlowCalculatorPage extends BasePage {
    private static final String SLOW_CALCULATOR_PAGE = BASE_PAGE + "slow-calculator.html";

    @FindBy(className = "lead")
    private WebElement description;

    @FindBy(id = "delay")
    private WebElement delayInput;

    @FindBy(className = "screen")
    private WebElement result;

    @FindBy(css = "#calculator span")
    private List<WebElement> calcButton;

    @FindBy(id = "spinner")
    private WebElement spinner;

    public SlowCalculatorPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        driver.get(SLOW_CALCULATOR_PAGE);
    }

    public String getDelay() {
        return delayInput.getDomProperty("value");
    }

    public String getDescription() {
        return description.getText();
    }

    public void setDelay(String input) {
        delayInput.clear();
        delayInput.sendKeys(input);
    }

    public SlowCalculatorPage input(CalcButton... calcButtons) {
        for(CalcButton calcButton: calcButtons){
            clickCalcButton(calcButton);
        }
        return this;
    }

    public SlowCalculatorPage plus() {
        return input(CalcButton.PLUS);
    }

    public SlowCalculatorPage minus() {
        return input(CalcButton.MINUS);
    }

    public SlowCalculatorPage multiply() {
        return input(CalcButton.MULTIPLY);
    }

    public SlowCalculatorPage divide() {
        return input(CalcButton.DIVIDE);
    }

    public void equals() {
         clickCalcButton(CalcButton.EQUAL);
    }

    public void reset() {
        clickCalcButton(CalcButton.RESET);
    }

    public String getResult() {
        String currentDelay = getDelay();
        String normalized = currentDelay.matches("-?\\d+(\\.\\d+)?") ? currentDelay : "0";
        double dur = Double.parseDouble(normalized);
        if(dur > 0) {
            String initialValue = result.getText().trim();
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofMillis(Math.round(dur * 1000)));
            customWait.until(d -> !result.getText().trim().equals(initialValue));
        }
        return result.getText().trim();
    }

    public void resultFieldEnableIsNotInput() {
        assertNotSame("input", result.getTagName());
    }

    public void checkCalculationResult(String expected) {
        String actual = getResult();
        assertEquals(expected, actual, "Expected: " + expected + ", but was: " + actual);
    }

    private void clickCalcButton(CalcButton key) {
        calcButton.stream()
                .filter(k -> k.getText().equals(key.getValue()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Key not found: " + key.getValue()))
                .click();
    }
}


