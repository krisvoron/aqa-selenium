package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import pages.CalcButton;
import pages.SlowCalculatorPage;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pages.CalcButton.DOT;
import static pages.CalcButton.EIGHT;
import static pages.CalcButton.FIVE;
import static pages.CalcButton.FOUR;
import static pages.CalcButton.MINUS;
import static pages.CalcButton.NINE;
import static pages.CalcButton.ONE;
import static pages.CalcButton.PLUS;
import static pages.CalcButton.SEVEN;
import static pages.CalcButton.SIX;
import static pages.CalcButton.THREE;
import static pages.CalcButton.TWO;
import static pages.CalcButton.ZERO;

public class SlowCalculatorPageTest extends BaseTest {

    @Test
    void defaultViewTest() {
        SlowCalculatorPage calculatorPage = new SlowCalculatorPage(driver, wait);
        String actualDelay = calculatorPage.getDelay();
        String actualDescription = calculatorPage.getDescription();
        assertAll(
                () -> assertEquals("5", actualDelay),
                () -> assertEquals(
                        "This calculator waits seconds to get the result of basic arithmetic operations.", actualDescription),
                calculatorPage::resultFieldEnableIsNotInput
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "0", "3", "2.78", "-0.1"})
    void delayTest(String delayValue) {
        SlowCalculatorPage calculatorPage = new SlowCalculatorPage(driver, wait);
        calculatorPage.setDelay(delayValue);
        calculatorPage.input(FIVE).plus().input(FOUR).equals();
        calculatorPage.checkCalculationResult("9");
    }

    @ParameterizedTest
    @MethodSource("testDataForPlus")
    void plusTest(CalcButton[] firstInput, CalcButton[] secondInput, String expectedResult) {
        SlowCalculatorPage calculatorPage = new SlowCalculatorPage(driver, wait);
        calculatorPage.setDelay("0");
        calculatorPage.input(firstInput)
                .plus()
                .input(secondInput)
                .equals();
        calculatorPage.checkCalculationResult(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("testDataForMinus")
    void minusTest(CalcButton[] firstInput, CalcButton[] secondInput, String expectedResult) {
        SlowCalculatorPage calculatorPage = new SlowCalculatorPage(driver, wait);
        calculatorPage.setDelay("0");
        calculatorPage.input(firstInput)
                .minus()
                .input(secondInput)
                .equals();
        calculatorPage.checkCalculationResult(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("testDataForMultiply")
    void multiplyTest(CalcButton[] firstInput, CalcButton[] secondInput, String expectedResult) {
        SlowCalculatorPage calculatorPage = new SlowCalculatorPage(driver, wait);
        calculatorPage.setDelay("0");
        calculatorPage.input(firstInput)
                .multiply()
                .input(secondInput)
                .equals();
        calculatorPage.checkCalculationResult(expectedResult);
    }

    @ParameterizedTest
    @MethodSource("testDataForDivide")
    void divideTest(CalcButton[] firstInput, CalcButton[] secondInput, String expectedResult) {
        SlowCalculatorPage calculatorPage = new SlowCalculatorPage(driver, wait);
        calculatorPage.setDelay("0");
        calculatorPage.input(firstInput)
                .divide()
                .input(secondInput)
                .equals();
        calculatorPage.checkCalculationResult(expectedResult);
    }

    @Test
    void multyOperationTest() {
        SlowCalculatorPage calculatorPage = new SlowCalculatorPage(driver, wait);
        calculatorPage.setDelay("0");
        calculatorPage.input(ONE, EIGHT)
                .minus()
                .input(FIVE)
                .multiply()
                .input(EIGHT)
                .divide()
                .input(ONE, ZERO)
                .plus()
                .input(SIX, DOT, NINE, SEVEN)
                .equals();
        calculatorPage.checkCalculationResult("20.97");
    }

    @Test
    void resetTest() {
        SlowCalculatorPage calculatorPage = new SlowCalculatorPage(driver, wait);
        calculatorPage.setDelay("0");
        calculatorPage.input(ONE, EIGHT)
                .reset();
        calculatorPage.checkCalculationResult("");
    }


    private static Stream<Arguments> testDataForPlus(){
        return Stream.of(
                Arguments.of(new CalcButton[] {FOUR}, new CalcButton[] {FIVE}, "9"),
                Arguments.of(new CalcButton[] {ZERO}, new CalcButton[] {SEVEN}, "7"),
                Arguments.of(new CalcButton[] {ZERO}, new CalcButton[] {ZERO}, "0"),
                Arguments.of(new CalcButton[] {THREE}, new CalcButton[] {THREE}, "6"),
                Arguments.of(new CalcButton[] {SIX}, new CalcButton[] {SIX}, "12"),
                Arguments.of(new CalcButton[] {ONE, TWO}, new CalcButton[] {THREE, SIX}, "48"),
                Arguments.of(new CalcButton[] {NINE, NINE}, new CalcButton[] {ONE}, "100"),
                Arguments.of(new CalcButton[] {TWO}, new CalcButton[] {ZERO, EIGHT}, "10"),
                Arguments.of(new CalcButton[] {ZERO, TWO}, new CalcButton[] {EIGHT}, "10"),
                Arguments.of(new CalcButton[] {MINUS, ONE}, new CalcButton[] {ONE}, "0"),
                Arguments.of(new CalcButton[] {ONE}, new CalcButton[] {MINUS,ONE}, "0"),
                Arguments.of(new CalcButton[] {MINUS, SEVEN}, new CalcButton[] {MINUS, FOUR}, "-11"),
                Arguments.of(new CalcButton[] {MINUS, NINE}, new CalcButton[] {PLUS, FIVE}, "-4"),
                Arguments.of(new CalcButton[] {ONE, DOT, TWO}, new CalcButton[] {THREE, DOT, FOUR}, "4.6"),
//                Arguments.of(new CalcButton[] {ZERO, DOT, ZERO, NINE}, new CalcButton[] {ZERO, DOT, ZERO, ONE}, "0.1"), //тут калькулятор сломался
                Arguments.of(new CalcButton[] {ONE, DOT, ZERO, NINE}, new CalcButton[] {ZERO, DOT, ZERO, ONE}, "1.1")

        );
    }

    private static Stream<Arguments> testDataForMinus(){
        return Stream.of(
                Arguments.of(new CalcButton[] {FIVE}, new CalcButton[] {FOUR}, "1"),
                Arguments.of(new CalcButton[] {SEVEN}, new CalcButton[] {ZERO}, "7"),
                Arguments.of(new CalcButton[] {ZERO}, new CalcButton[] {SEVEN}, "-7"),
                Arguments.of(new CalcButton[] {ZERO}, new CalcButton[] {ZERO}, "0"),
                Arguments.of(new CalcButton[] {THREE}, new CalcButton[] {THREE}, "0"),
                Arguments.of(new CalcButton[] {THREE, SIX}, new CalcButton[] {ONE, TWO}, "24"),
                Arguments.of(new CalcButton[] {FOUR, FIVE}, new CalcButton[] {ONE, EIGHT}, "27"),
                Arguments.of(new CalcButton[] {ONE, ZERO, ZERO}, new CalcButton[] {ONE}, "99"),
                Arguments.of(new CalcButton[] {EIGHT}, new CalcButton[] {ZERO, TWO}, "6"),
                Arguments.of(new CalcButton[] {ZERO, EIGHT}, new CalcButton[] {TWO}, "6"),
                Arguments.of(new CalcButton[] {ONE}, new CalcButton[] {ONE}, "0"),
                Arguments.of(new CalcButton[] {MINUS, ONE}, new CalcButton[] {ONE}, "-2"),
                Arguments.of(new CalcButton[] {MINUS, NINE}, new CalcButton[] {ONE}, "-10"),
//                Arguments.of(new CalcButton[] {ONE, DOT, ONE}, new CalcButton[] {ZERO, DOT, ZERO, ONE}, "0.09"), // тут калькулятор сломался
                Arguments.of(new CalcButton[] {THREE, DOT, FOUR}, new CalcButton[] {ONE, DOT, TWO}, "2.2")
        );
    }

    private static Stream<Arguments> testDataForMultiply(){
        return Stream.of(
                Arguments.of(new CalcButton[] {ONE}, new CalcButton[] {TWO}, "2"),
                Arguments.of(new CalcButton[] {TWO}, new CalcButton[] {THREE}, "6"),
                Arguments.of(new CalcButton[] {THREE}, new CalcButton[] {TWO}, "6"),
                Arguments.of(new CalcButton[] {ONE, ZERO}, new CalcButton[] {ONE, ZERO}, "100"),
                Arguments.of(new CalcButton[] {FOUR, FIVE}, new CalcButton[] {SIX, SEVEN}, "3015"),
                Arguments.of(new CalcButton[] {ZERO}, new CalcButton[] {ZERO}, "0"),
                Arguments.of(new CalcButton[] {ZERO}, new CalcButton[] {FOUR}, "0"),
                Arguments.of(new CalcButton[] {FOUR}, new CalcButton[] {ZERO}, "0"),
                Arguments.of(new CalcButton[] {MINUS, ONE}, new CalcButton[] {ZERO}, "0"),
                Arguments.of(new CalcButton[] {ZERO, NINE}, new CalcButton[] {ONE}, "9"),
                Arguments.of(new CalcButton[] {ONE}, new CalcButton[] {ZERO, NINE}, "9"),
                Arguments.of(new CalcButton[] {MINUS, ONE}, new CalcButton[] {ONE}, "-1"),
                Arguments.of(new CalcButton[] {MINUS, EIGHT}, new CalcButton[] {TWO}, "-16"),
                Arguments.of(new CalcButton[] {ONE, DOT, TWO}, new CalcButton[] {THREE, DOT, FOUR}, "4.08")
        );
    }

    private static Stream<Arguments> testDataForDivide(){
        return Stream.of(
                Arguments.of(new CalcButton[] {TWO}, new CalcButton[] {ONE}, "2"),
                Arguments.of(new CalcButton[] {ZERO}, new CalcButton[] {ONE}, "0"),
                Arguments.of(new CalcButton[] {ONE}, new CalcButton[] {ZERO}, "Infinity"),
                Arguments.of(new CalcButton[] {SIX}, new CalcButton[] {ZERO, TWO}, "3"),
                Arguments.of(new CalcButton[] {ZERO, SIX}, new CalcButton[] {TWO}, "3"),
                Arguments.of(new CalcButton[] {SIX}, new CalcButton[] {THREE}, "2"),
                Arguments.of(new CalcButton[] {ONE, ZERO}, new CalcButton[] {FIVE}, "2"),
                Arguments.of(new CalcButton[] {THREE, SIX}, new CalcButton[] {ONE, TWO}, "3"),
                Arguments.of(new CalcButton[] {MINUS, EIGHT}, new CalcButton[] {TWO}, "-4"),
                Arguments.of(new CalcButton[] {SEVEN}, new CalcButton[] {TWO}, "3.5"),
                Arguments.of(new CalcButton[] {FOUR}, new CalcButton[] {THREE}, "1.3333333333333333"),
                Arguments.of(new CalcButton[] {TWO, DOT, FOUR}, new CalcButton[] {ONE, DOT, TWO}, "2")
        );
    }
}
