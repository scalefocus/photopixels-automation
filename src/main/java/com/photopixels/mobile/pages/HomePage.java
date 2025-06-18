package com.photopixels.mobile.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import static java.time.Duration.ofSeconds;

public class HomePage {

    private AppiumDriver driver;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Home']")
    private WebElement homeButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Settings']")
    private WebElement settingsButton;

    @AndroidFindBy(xpath = "//android.widget.ProgressBar")
    private WebElement loadingIndicator;

    public HomePage(AppiumDriver driver) {
        this.driver = driver;

        // This time out is set because test can be run on slow Android SDK emulator
        PageFactory.initElements(new AppiumFieldDecorator(driver, ofSeconds(5)), this);
    }

    @Step("Check if 'Home' button is displayed")
    public boolean isHomeButtonDisplayed() {
        return homeButton.isDisplayed();
    }

    @Step("Check if 'Settings' button is displayed")
    public boolean isSettingsButtonDisplayed() {
        return settingsButton.isDisplayed();
    }
}
