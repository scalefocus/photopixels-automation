package com.photopixels.mobile.pages;

import com.photopixels.helpers.WaitOperationHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import static java.time.Duration.ofSeconds;

public class RegistrationPage extends WaitOperationHelper {

    private final AppiumDriver driver;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Name']/parent::android.widget.EditText")
    private WebElement nameField;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Email']/parent::android.widget.EditText")
    private WebElement emailField;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Password']/parent::android.widget.EditText")
    private WebElement passwordField;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Confirm password']/parent::android.widget.EditText")
    private WebElement confirmPasswordField;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='REGISTER']")
    private WebElement registerButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text=\"Name\"]/following-sibling::android.widget.TextView")
    private WebElement nameFieldErrorMessage;

    public RegistrationPage(AppiumDriver driver) {
        super(driver);
        this.driver = driver;

        // This time out is set because test can be run on slow Android SDK emulator
        PageFactory.initElements(new AppiumFieldDecorator(driver, ofSeconds(5)), this);
    }

    @Step("Successful registration of a new user")
    public MobileLoginPage registerNewUser(String name, String email, String password) {
        fillNameField(name);
        fillEmailField(email);
        fillPasswordField(password);
        fillConfirmPasswordField(password);
        registerButton.click();
        return new MobileLoginPage(driver);
    }

    @Step("Fill in Name field")
    public void fillNameField(String name) {
        if (name != null) {
            nameField.sendKeys(name);
        }
    }

    @Step("Fill in Email field")
    public void fillEmailField(String email) {
        if (email != null) {
            emailField.sendKeys(email);
        }
    }

    @Step("Fill in Password field")
    public void fillPasswordField(String password) {
        if (password != null) {
            passwordField.sendKeys(password);
        }
    }

    @Step("Fill in Confirm Password field")
    public void fillConfirmPasswordField(String confirmPassword) {
        if (confirmPassword != null) {
            confirmPasswordField.sendKeys(confirmPassword);
        }
    }

    public boolean isNameFieldErrorMessageVisible() {
        try {
            waitForElementToBeVisible(driver, nameFieldErrorMessage, 2);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
