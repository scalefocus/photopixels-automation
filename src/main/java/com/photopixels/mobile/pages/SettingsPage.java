package com.photopixels.mobile.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import static java.time.Duration.ofSeconds;

public class SettingsPage {

    private AppiumDriver driver;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Require Power for Sync']/following-sibling::android.view.View")
    private WebElement requirePowerForSync;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Require WIFI for Sync']/following-sibling::android.view.View")
    private WebElement requireWiFiForSync;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Sync Google Photos']/following-sibling::android.view.View")
    private WebElement SyncGooglePhotos;

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Logout']")
    private WebElement logoutButton;

    public SettingsPage(AppiumDriver driver) {
        this.driver = driver;

        // This time out is set because test can be run on slow Android SDK emulator
        PageFactory.initElements(new AppiumFieldDecorator(driver, ofSeconds(5)), this);
    }

    @Step("Turn Require WiFi for Sync option to {requirewifi}")
    public void turnRequireWiFiForSyncOnOff(boolean requirewifi) {
        if(requirewifi){
            if (!Boolean.parseBoolean(requireWiFiForSync.getDomAttribute("checked"))) {
                requireWiFiForSync.click();
            }
        }
        else {
            if (Boolean.parseBoolean(requireWiFiForSync.getDomAttribute("checked"))) {
                requireWiFiForSync.click();
            }
        }
    }

}
