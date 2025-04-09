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
}
