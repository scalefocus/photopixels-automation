package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpUserPage extends NavigationPage {

    private WebDriver driver;

    //TODO to fix the xpaths
    @FindBy(xpath = "//div//h5[contains(@class, 'css-12s6qm0')]")
    private WebElement signUpNewUserHeader;

    @FindBy(xpath = "//input[@id='name']")
    private WebElement newNameUser;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement newEmailAddress;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement newPassword;

    @FindBy(xpath = "//div[@id='root']//button[contains(@class, 'css-1nms917')]")
    private WebElement signUpNewUserButton;

    public SignUpUserPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Sign Up new user successfully")
    public LoginPage signUpNewUser(String name, String email, String password) {
        newNameUser.sendKeys(name);
        newEmailAddress.sendKeys(email);
        newPassword.sendKeys(password);

        signUpNewUserButton.click();

        return new LoginPage(driver);
    }

    @Step("Fill credentials")
    public void fillCredentials(String name, String email, String password) {

        if ( name != null ) {
            newNameUser.sendKeys(name);
        }

        if ( email != null ) {
            newEmailAddress.sendKeys(email);
        }

        if ( password != null ) {
            newPassword.sendKeys(password);
        }
    }

    @Step("Check if Sign Up button is enabled")
    public boolean isSignUpButtonEnabled() {
        return signUpNewUserButton.isEnabled();
    }
}
