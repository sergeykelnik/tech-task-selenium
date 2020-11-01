import io.github.bonigarcia.wdm.WebDriverManager;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestClass {

    private WebDriver driver;
    /*
    Page selectors, we use xpath as it allows to search elements by its text.
    */
    private By careers = By.xpath("//a[text()='Careers']");
    private By vacancies = By.xpath("//a[text()='Vacancies']");
    private By testAutomationLink = By.xpath("//a[text()='Test Automation Engineer']");
    private By skills = By.xpath("//h1[text()='Test Automation Engineer']//following::ul[1]/li");//There's an error in test task description: skills are separated by <li> tag, not <br>.

    /*
    Let's initialize Webdriver here, make browser maximized, and setup an implicit wait.
    */
    @BeforeTest
    private void setuUp() {
        WebDriverManager.chromedriver().setup();//Automatic Selenium WebDriver binaries management instead of executable path.
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    /*
    We can use POM model but we don't want do that as we've only one test.
    */
    @Test
    private void verifySkillsTest() {
        driver.get("https://ctco.lv/en");
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(careers)).perform();//We use moveToElement to simulate mouse hover action.
        driver.findElement(vacancies).click();
        driver.findElement(testAutomationLink).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(skills));//Explicit wait for elements to be visible on page.
        List<WebElement> elements = driver.findElements(skills);
        elements.stream().map(WebElement::getText).forEach(System.out::println);//This optional part just shows us that we check right elements.
        Assert.assertEquals(elements.size(), 5, "Amount of skills is incorrect!");
    }

    /*
    We can take a screen shot on test fail here. And it's amazing.
    */
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

    /*
    Do not forget to stop Webdriver!
    */
    @AfterTest
    private void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}