package com.epam.lab.page_object;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AuthorizationPage {

    private WebDriver driver;

    @FindBy(xpath = "//input[@id='identifierId']")
    private WebElement email;

    @FindBy(id = "identifierNext")
    private WebElement emailNextButton;

    @FindBy(name = "password")
    private WebElement password;

    public AuthorizationPage(WebDriver webDriver) {
        driver = webDriver;
        PageFactory.initElements(driver, this);
    }

    private void enterEmail(String mail) {
        email.sendKeys(mail);
        emailNextButton.click();
    }

    private void enterPassword(String pass) {
        password.sendKeys(pass + Keys.ENTER);
    }

    public void logIn(String mail, String pass) {
        enterEmail(mail);
        enterPassword(pass);
    }
}
