import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestClass {

    private WebDriver driver;
    private By careers = By.xpath("//a[text()='Careers']");
    private By vacancies = By.xpath("//a[text()='Vacancies']");
    private By testAutomationLink = By.xpath("//a[text()='Test Automation Engineer']");
    private By skills = By.xpath("//h1[text()='Test Automation Engineer']//following::ul[1]/li");

    @BeforeTest
    private void setuUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    private void verifySkillsTest() throws InterruptedException {
        driver.get("https://ctco.lv/en");
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(careers)).build().perform();
        driver.findElement(vacancies).click();
        driver.findElement(testAutomationLink).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(skills));
        Assert.assertEquals(driver.findElements(skills).size(), "5");
    }

    @AfterMethod
    public void screenShot(ITestResult result) {
        if (ITestResult.FAILURE == result.getStatus()) {
            try {
                TakesScreenshot screenshot = (TakesScreenshot) driver;
                String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                File src = screenshot.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(src, new File("target/screenshots/screenshot_" + timestamp + ".png"));
                System.out.println("Successfully captured a screenshot");
            } catch (Exception e) {
                System.out.println("Exception while taking screenshot " + e.getMessage());
            }
        }
    }

    @AfterTest
    private void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}