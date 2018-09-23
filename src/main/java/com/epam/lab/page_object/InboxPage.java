package com.epam.lab.page_object;

import com.epam.lab.parser.MyParser;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class InboxPage {

    private WebDriver driver;

    private WebDriverWait wait;

    @FindBy(xpath = "//tr[1]/td/div[contains(@class, 'oZ-jc T-Jo J-J5-Ji ')]")
    private WebElement message1;

    @FindBy(xpath = "//tr[2]/td/div[contains(@class, 'oZ-jc T-Jo J-J5-Ji ')]")
    private WebElement message2;

    @FindBy(xpath = "//tr[3]/td/div[contains(@class, 'oZ-jc T-Jo J-J5-Ji ')]")
    private WebElement message3;

    @FindBy(css = "div.T-I.J-J5-Ji.nX.T-I-ax7.T-I-Js-Gs.mA")
    private WebElement deleteButton;

    @FindBy(id = "link_undo")
    private WebElement undoButton;

    public InboxPage(WebDriver webDriver) {
        Properties driverProps = new MyParser().parsePropertiesFile("./src/main/properties/driver.properties");
        driver = webDriver;
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(driverProps.getProperty("implicit_wait")), TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Integer.parseInt(driverProps.getProperty("explicit_wait")));
        PageFactory.initElements(driver, this);
    }


    public void selectAndDeleteMessages() {
        selectMessage(message1, 1);
        selectMessage(message2, 2);
        selectMessage(message3, 3);
        deleteMessages();
    }

    private void selectMessage(WebElement message, int index) {
        try {
            message.click();
        } catch (StaleElementReferenceException e) {
            message = driver.findElement(By.xpath(("//tr[" + String.valueOf(index) + "]/td/div[contains(@class, 'oZ-jc T-Jo J-J5-Ji ')]")));
            message.click();
        }
    }

    private void deleteMessages() {
        try {
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable((deleteButton))).click();
            Thread.sleep(4000);
            wait.until(ExpectedConditions.elementToBeClickable((undoButton))).click();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkUndoResult() {
        try {
            driver.findElement(By.id(message1.getAttribute("id")));
            driver.findElement(By.id(message2.getAttribute("id")));
            driver.findElement(By.id(message3.getAttribute("id")));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
