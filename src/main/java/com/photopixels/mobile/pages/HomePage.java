package com.photopixels.mobile.pages;

import com.photopixels.helpers.WaitOperationHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static java.time.Duration.ofSeconds;

public class HomePage extends WaitOperationHelper {

    private AppiumDriver driver;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Home']")
    private WebElement homeButton;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Settings']")
    private WebElement settingsButton;

    @AndroidFindBy(xpath = "//android.widget.ProgressBar")
    private WebElement loadingIndicator;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Sync media']/following-sibling::android.widget.Button")
    private WebElement syncMediaButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_allow_button']")
    private WebElement allowNotificationsButton;

    @AndroidFindBy(xpath = "//android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_allow_all_button']")
    private WebElement allowAccessAllPhotosButton;

    @AndroidFindBy(xpath = "//K0.w0/android.view.View/android.view.View/android.view.View/android.view.View[1]//android.view.View")
    private List<WebElement> photos;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Upload device media' and @resource-id='android:id/title']")
    private WebElement uploadNotification;

    public HomePage(AppiumDriver driver) {
        super(driver);
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

    @Step("Navigate to settings page")
    public SettingsPage navigateToSettings() {
        settingsButton.click();
        return new SettingsPage(driver);
    }

    @Step("Click Home button")
    public void clickHomeButton() {
        homeButton.click();
    }

    @Step("Click Sync media button")
    public void clickSyncMediaButton() {
        syncMediaButton.click();
    }

    @Step("Allow notification from the PhotoPixels App")
    public void clickAllowNotificationsButton() {
        allowNotificationsButton.click();
    }

    @Step("Allow all photos access from the PhotoPixels App")
    public void clickAllowAllPhotosAccessButton() {
        allowAccessAllPhotosButton.click();
    }

    public void waitForUploadToFinish() {
        waitForElementToBeVisible(photos.get(0));
        ((AndroidDriver) driver).openNotifications();
        waitForElementToBeVisible(uploadNotification);
        try {
            waitForElementToDisappear(uploadNotification);
        } catch (TimeoutException e) {
            // Sometimes the upload can take more time
            System.out.println("Uploading takes more than usual...");
            waitForElementToDisappear(uploadNotification);
        }

        ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
    }
}
