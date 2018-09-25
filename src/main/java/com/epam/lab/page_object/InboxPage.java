package com.epam.lab.page_object;

import com.epam.lab.parser.MyParser;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Properties;

public class InboxPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private List<String> identifiers;
    private Logger LOG = Logger.getLogger(InboxPage.class);

    @FindAll({@FindBy(xpath = "//tr[1]/td/div[contains(@class, 'oZ-jc T-Jo J-J5-Ji ')]"),
            @FindBy(xpath = "//tr[2]/td/div[contains(@class, 'oZ-jc T-Jo J-J5-Ji ')]"),
            @FindBy(xpath = "//tr[3]/td/div[contains(@class, 'oZ-jc T-Jo J-J5-Ji ')]")})
    private List<WebElement> messages;

    @FindBy(css = "div.T-I.J-J5-Ji.nX.T-I-ax7.T-I-Js-Gs.mA")
    private WebElement deleteButton;

    @FindBy(id = "link_undo")
    private WebElement undoButton;

    public InboxPage(WebDriver webDriver) {
        Properties driverProps = new MyParser().parsePropertiesFile("./src/main/properties/driver.properties");
        driver = webDriver;
        wait = new WebDriverWait(driver, Integer.parseInt(driverProps.getProperty("explicit_wait")));
        PageFactory.initElements(driver, this);
        identifiers = new ArrayList<>();
    }

    private void selectMessage(WebElement message, int index) {
        try {
            message.click();
        } catch (StaleElementReferenceException e) {
            message = driver.findElement(By.xpath(("//tr[" + String.valueOf(index) + "]/td/div[contains(@class, 'oZ-jc T-Jo J-J5-Ji ')]")));
            message.click();
        }
        identifiers.add(message.getAttribute("id"));
    }

    private void deleteMessages() {
        wait.until(ExpectedConditions.elementToBeClickable((deleteButton))).click();
    }

    private void undo() {
        wait.until(ExpectedConditions.elementToBeClickable((undoButton))).click();
    }

    public void selectAndDeleteMessages(int quantity) {
        int i = 1;
        if (messages.size() >= quantity) {
            while (i <= quantity) {
                selectMessage(messages.get(i - 1), i);
                i++;
            }
        } else {
            LOG.error("Quantity of selected messages is larger than quantity of all located messages.");
        }
        deleteMessages();
        undo();
    }

    public boolean isUndoCompleted() {
        try {
            for (int i = 0; i < messages.size(); i++) {
                if (!messages.get(i).getAttribute("id").equals(identifiers.get(i)))
                    throw new NoSuchElementException("Can't find element with id=" + identifiers.get(i) + ". Element has been deleted.");
            }
            return true;
        } catch (NoSuchElementException e) {
            LOG.error(e.getMessage());
            return false;
        }
    }
}
