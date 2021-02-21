import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class KeysLolRandom {
    private WebDriver driver;
    private By random = By.cssSelector("[title=\"Random page\"]");
    private By empty = By.cssSelector(".empty");
    private By filled = By.cssSelector(".filled");
    private By used = By.cssSelector(".used");

    @BeforeTest
    private void setuUp() {
        WebDriverManager.chromedriver().setup();//Automatic Selenium WebDriver binaries management instead of executable path.
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @Test
    public void assignmentOperatorsExplored() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 5);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/Users/Kelnik/AppData/Local/Google/Chrome/User Data");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get("https://keys.lol/bitcoin/2");
        check();
    }

    private void check() throws InterruptedException {
        try {
            WebElement element = driver.findElement(filled);
            System.out.println(element.getText());
        } catch (Exception e) {
            getRandom();
        }
    }

    private void getRandom() throws InterruptedException {
        driver.findElement(random).click();
        Thread.sleep(3000);
        check();
    }

    @AfterTest
    private void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

