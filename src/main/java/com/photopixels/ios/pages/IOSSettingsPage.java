package com.photopixels.ios.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

public class IOSSettingsPage {

    private final AppiumDriver driver;

    public IOSSettingsPage(AppiumDriver driver){
        this.driver = driver;
    }

    private WebElement logoutButton () {
        return driver.findElement(AppiumBy.accessibilityId("Logout"));
    }

    public void logout () {
        logoutButton().click();
    }



}
