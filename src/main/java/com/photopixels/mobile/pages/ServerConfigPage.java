package com.photopixels.mobile.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import static java.time.Duration.ofSeconds;

public class ServerConfigPage {

    private AppiumDriver driver;

    @AndroidFindBy(className = "android.widget.EditText")
    private WebElement serverAddressField;

    @AndroidFindBy(className = "android.widget.Button")
    private WebElement nextButton;

    public ServerConfigPage(AppiumDriver driver) {
        this.driver = driver;

        // This time out is set because test can be run on slow Android SDK emulator
        PageFactory.initElements(new AppiumFieldDecorator(driver, ofSeconds(5)), this);
    }

    @Step("Fill server address")
    public void fillServerAddress(String serverAddress) {
        serverAddressField.sendKeys(serverAddress);
    }

    @Step("Click next button")
    public MobileLoginPage clickNextButton() {
        nextButton.click();

        return new MobileLoginPage(driver);
    }

    @Step("Check if server address is filled")
    public boolean isServerAddressFilled() {
        return !serverAddressField.getText().isEmpty();
    }
}
