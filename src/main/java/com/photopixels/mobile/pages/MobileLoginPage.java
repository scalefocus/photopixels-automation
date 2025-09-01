package com.photopixels.mobile.pages;

import com.photopixels.helpers.WaitOperationHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import static java.time.Duration.ofSeconds;

public class MobileLoginPage extends WaitOperationHelper {

    private AppiumDriver driver;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Username']/parent::android.widget.EditText")
    private WebElement usernameField;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Password']/parent::android.widget.EditText")
    private WebElement passwordField;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='LOGIN']/following-sibling::android.widget.Button")
    private WebElement loginButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='REGISTER']/following-sibling::android.widget.Button")
    private WebElement registerButton;

    @AndroidFindBy(xpath = "//android.widget.Toast[1]")
    private WebElement activeToastNotification;

    public MobileLoginPage(AppiumDriver driver) {
        super(driver);
        this.driver = driver;

        // This time out is set because test can be run on slow Android SDK emulator
        PageFactory.initElements(new AppiumFieldDecorator(driver, ofSeconds(5)), this);
    }

    @Step("Login successfully mobile app")
    public HomePage login(String username, String password) {
        fillCredentialsMobile(username, password);

        loginButton.click();

        return new HomePage(driver);
    }

    @Step("Login with error")
    public ErrorPopUpPage loginWithError(String username, String password) {
        fillCredentialsMobile(username, password);

        loginButton.click();

        return new ErrorPopUpPage(driver);
    }

    @Step("Fill credentials mobile app")
    public void fillCredentialsMobile(String username, String password) {
        waitForElementToBeVisible(usernameField);

        if (username != null) {
            usernameField.sendKeys(username);
        }

        if (password != null) {
            passwordField.sendKeys(password);
        }
    }

    @Step("Check if login button is displayed")
    public boolean isLoginButtonDisplayed() {
        return loginButton.isDisplayed();
    }

    @Step("Click Registration button")
    public RegistrationPage clickRegistrationButton() {
        registerButton.click();
        return new RegistrationPage(driver);
    }

    @Step("Get Username field input value")
    public String getUsernameFieldValue() {
        return usernameField.getText();
    }

    @Step("Check if a toast notification string appeared on screen")
    public boolean isToastNotificationDisplayed(String expectedToast) {
        try {
            return expectedToast.equals(activeToastNotification.getText());
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            return false;
        }
    }
}
