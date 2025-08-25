package com.photopixels.mobile.pages;

import com.photopixels.helpers.WaitOperationHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
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

    @AndroidFindBy(xpath = "//android.view.View[1]")
    private WebElement firstPicture;

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
    public void navigateToSettings() {
        settingsButton.click();
    }

    @Step("Navigate to home page")
    public void navigateToHome() {
        homeButton.click();
    }

    @Step("Click Sync media button and allow permissions for notifications and photo access")
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

    public void waitForUploadToFinish(AppiumDriver mobileDriver) throws InterruptedException {
        //TODO: Find a better wait for waiting
        // Open notification shade
        waitForElementToDisappear(syncMediaButton);
        ((AndroidDriver) mobileDriver).openNotifications();

        boolean uploading = true;
        while (uploading) {
            //Search for the Upload Notification from the Photo Pixels App
            List<WebElement> uploadNotifications = mobileDriver.findElements(By.xpath("//*[contains(@text,'Upload device media')]"));

            if (uploadNotifications.isEmpty()) {
                uploading = false;
            } else {
                Thread.sleep(4000);
            }
        }
    }
}
