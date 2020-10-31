import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class TestClass {

    private WebDriver driver;
    private By careers = By.cssSelector("#menu-item-127 a");
    private By vacancies = By.cssSelector("#menu-item-131 a");
    private By testAutomationLink = By.cssSelector("#menu-item-3249 a");

    @BeforeTest
    private void setuUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    private void verifySkillsTest() {
        driver.get("https://ctco.lv/en");
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(careers)).build().perform();
        driver.findElement(vacancies).click();

    }

    @AfterTest
    private void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}