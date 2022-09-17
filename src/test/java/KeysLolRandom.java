import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class KeysLolRandom {
    private WebDriver driver;
    private By randomBitcoin = By.cssSelector("[title=\"Random page\"]");
    private By randomEthereum = By.xpath("/html/body/main/div/ul/li[10]/a");
    private By grey = By.cssSelector(".empty");
    private By yellow = By.cssSelector(".used");
    private By green = By.cssSelector(".filled");
    private By price = By.cssSelector("h4 span.badge");
    private By undefinedTransaction = By.xpath("//*[contains(text(),'(? tx)')]");
    private By captcha = By.cssSelector(".g-recaptcha div>iframe");
    private int i = 0;

    @BeforeTest
    private void setUp() {
        WebDriverManager.chromedriver().setup();//Automatic Selenium WebDriver binaries management instead of executable path.
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=C:/Users/Kelnik/AppData/Local/Google/Chrome/User Data");
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        //.manage().window().maximize();
        // options.addArguments("--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");
        // driver = new ChromeDriver(options);
    }


    @Test
    public void bitcoinExplorer() {
        driver.get("https://keys.lol/are-you-human");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(captcha));
        driver.switchTo().frame(driver.findElement(captcha));
        driver.findElement(By.cssSelector(".recaptcha-checkbox-unchecked")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".recaptcha-checkbox-checked")));
        driver.switchTo().defaultContent();
        driver.findElement(By.cssSelector(".btn.mt-8")).click();
        //Captcha
        driver.get("https://keys.lol/bitcoin/random");
        fluentWait();
        checkBitcoin();
    }

    @Test
    public void ethereumExplorer() {
        driver.get("https://privatekeys.pw/keys/ethereum/random");
        checkEthereum();
    }

    private void checkBitcoin() {
        List<WebElement> element = driver.findElements(green);
        if (element.size() != 0) {
            element.forEach(e -> System.out.println(e.getText()));
        } else {
            driver.findElement(randomBitcoin).click();
            fluentWait();
            checkBitcoin();
        }
    }

    private void fluentWait() {
        FluentWait<WebDriver> wait = new FluentWait<>(driver);
        wait.withTimeout(Duration.ofSeconds(5));
        wait.pollingEvery(Duration.ofMillis(100));
        wait.ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(undefinedTransaction));
    }

    private void checkEthereum() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(price));
        WebElement webElement = driver.findElement(price);
        if (!webElement.getText().equals("0")) {
            for (WebElement e : driver.findElements(By.cssSelector("tbody tr td"))) {
                System.out.println(e.getText());
            }
        } else {
            driver.findElement(randomEthereum).click();
            i++;
            System.out.println("Refreshed " + i + " times");
            checkEthereum();
        }
    }

    //@AfterTest
    private void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

