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
import java.util.concurrent.TimeUnit;

public class GMailTest {

    private WebDriver driver;
    private MyParser myParser;
    private final Logger LOG = Logger.getLogger(GMailTest.class);

    static {
        PropertyConfigurator.configure("./src/main/properties/log4j.properties");
    }

    @BeforeClass
    public void init() {
        myParser = new MyParser();
        Properties driverProperties = myParser.parsePropertiesFile("./src/main/properties/driver.properties");
        System.setProperty("webdriver.chrome.driver", driverProperties.getProperty("browser_driver"));
        driver = new ChromeDriver();
        driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(driverProperties.getProperty("implicit_wait")), TimeUnit.SECONDS);
    }

    @Test
    public void testUndoWithMessagesDeletion() {
        LOG.info("inside test");
        Document xml = myParser.parseXML("./src/main/resources/LoginAndPassword.xml");
        AuthorizationPage authorizationPage = new AuthorizationPage(driver);
        InboxPage inboxPage = new InboxPage(driver);
        driver.get("https://mail.google.com");
        authorizationPage.logIn(xml.getElementsByTagName("email").item(0).getTextContent(),
                xml.getElementsByTagName("password").item(0).getTextContent());
        inboxPage.selectAndDeleteMessages(3);
        Assert.assertTrue(inboxPage.isUndoCompleted());
        LOG.info("test successfully passed");
    }

    @AfterClass
    public void exit() {
        driver.quit();
    }
}
