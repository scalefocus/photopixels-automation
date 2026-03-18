package com.photopixels.ios.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;

public class IOSNavbarPage {

    private final AppiumDriver driver;

    public IOSNavbarPage(AppiumDriver driver) {
        this.driver = driver;
    }

    // ─── Element Getters ───────────────────────────────────────────────────────

    public WebElement getPhotosTab() {
        return driver.findElement(AppiumBy.xpath(
                "(//XCUIElementTypeButton[@name='Photos'])[1]"
        ));
    }
    public WebElement getFavoritesTab() {
        return driver.findElement(AppiumBy.xpath("(//XCUIElementTypeButton[@name='Favorites'])[1]"
        ));
    }

    public WebElement getTrashTab() {
        return driver.findElement(AppiumBy.xpath(
                "(//XCUIElementTypeButton[@name='Trash'])[1]"
        ));
    }

    public WebElement getSettingsButton() {
        return driver.findElement(AppiumBy.xpath(
                "(//XCUIElementTypeButton[@name='person.circle'])[2]"
        ));
    }

    // ─── Actions ───────────────────────────────────────────────────────────────

    public void goToPhotos() {
        getPhotosTab().click();
    }

    public void goToFavorites() {
        getFavoritesTab().click();
    }

    public void goToTrash() {
        getTrashTab().click();
    }

    public void goToSettings() {
        getSettingsButton().click();
    }
}