import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
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
import java.time.Duration;
import java.util.Date;
import java.util.List;

public class TestClass {

    private WebDriver driver;
    /*
    Page selectors, we use xpath as it allows to search elements by its text.
    */
    private final By careers = By.xpath("//a[text()='Careers']");
    private final By vacancies = By.xpath("//a[text()='Vacancies']");
    private final By position = By.xpath("//a[text()='TEST AUTOMATION ENGINEER']");
    private final By skills = By.xpath("//h1[text()='TEST AUTOMATION ENGINEER vacancy']//following::ul[1]/li");//There's an error in test task description: skills are separated by <li> tag, not <br>.

    /*
    Let's initialize Webdriver here, make browser maximized, and setup an implicit wait.
    */
    @BeforeTest
    private void setUp(String browserName) {
        switch (browserName) {
            case "Chrome" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setHeadless(false);
                driver = new ChromeDriver(chromeOptions);//Automatic Selenium WebDriver binaries management instead of executable path.
            }
            case "Firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(false);
                driver = new FirefoxDriver(firefoxOptions);//Automatic Selenium WebDriver binaries management instead of executable path.
            }
            case "Edge" -> {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();//Automatic Selenium WebDriver binaries management instead of executable path.
            }
        }
        driver.manage().window().maximize();
        //driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    /*
    We can use POM model but we don't want do that as we've only one test.
    */
    @Test
    void verifySkillsTest(String browserName) {
        try {
            setUp(browserName);
            driver.get("https://ctco.lv/en");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(careers));
            Actions action = new Actions(driver);
            action.moveToElement(driver.findElement(careers)).build().perform();//We use moveToElement to simulate mouse hover action.
            wait.until(ExpectedConditions.visibilityOfElementLocated(vacancies)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(position)).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(skills));//Explicit wait for elements to be visible on page.
            List<WebElement> elements = driver.findElements(skills);
            elements.stream().map(WebElement::getText).forEach(System.out::println);//This optional part just shows us that we check right elements.
            Assert.assertEquals(elements.size(), 8, "Amount of skills is incorrect!");
        } catch (Exception e) {
            System.out.println(browserName + " Exception");
            e.printStackTrace();
        } finally {
            tearDown();
        }
    }

    /*
    We can take a screenshot on test fail here. And it's amazing.
    */
    @AfterMethod
    private void screenShot(ITestResult result) {
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
        System.out.println("WebDriver terminated");
    }
}