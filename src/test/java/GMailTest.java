import com.epam.lab.page_object.AuthorizationPage;
import com.epam.lab.page_object.InboxPage;
import com.epam.lab.parser.MyParser;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import java.util.Properties;

public class GMailTest {

    private WebDriver driver;
    private MyParser myParser;
    private Logger LOG;

    @BeforeClass
    public void init() {
        LOG = Logger.getLogger(GMailTest.class);
        PropertyConfigurator.configure("./src/main/properties/log4j.properties");
        myParser = new MyParser();
        Properties driverProperties = myParser.parsePropertiesFile("./src/main/properties/driver.properties");
        System.setProperty("webdriver.chrome.driver", driverProperties.getProperty("browser_driver"));
        driver = new ChromeDriver();
    }

    @Test
    public void testWithMessages() {
        LOG.info("start test...");

        Document xml = myParser.parseXML("./src/main/resources/LoginAndPassword.xml");
        AuthorizationPage authorizationPage = new AuthorizationPage(driver);
        InboxPage inboxPage = new InboxPage(driver);

        driver.get("https://www.google.com");
        WebElement search = driver.findElement(By.name("q"));
        search.sendKeys("gmail");
        search.submit();
        WebElement gMailLink = driver.findElement(By.xpath("//a[contains(@href, 'https://www.google.com/gmail/')]"));
        gMailLink.click();
        WebElement signIn = driver.findElement(By.cssSelector("a.gmail-nav__nav-link.gmail-nav__nav-link__sign-in"));
        signIn.click();
        authorizationPage.logIn(xml.getElementsByTagName("email").item(0).getTextContent(),
                xml.getElementsByTagName("password").item(0).getTextContent());
        inboxPage.selectAndDeleteMessages();
        Assert.assertEquals(true, inboxPage.checkUndoResult());
    }

    @AfterClass
    public void exit() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.quit();

        LOG.info("test successfully passed");
    }
}