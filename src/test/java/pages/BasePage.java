package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static constants.Constants.BONI_GARCIA_PAGE;

public class BasePage {
    protected static final String BASE_PAGE = BONI_GARCIA_PAGE + "selenium-webdriver-java/";
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }
}
