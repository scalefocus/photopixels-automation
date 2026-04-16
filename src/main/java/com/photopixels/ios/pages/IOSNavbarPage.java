package com.photopixels.ios.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class IOSNavbarPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public IOSNavbarPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ─── Element Getters ───────────────────────────────────────────────────────

    public WebElement getPhotosTab() {
        return driver.findElement(AppiumBy.xpath(
                "(//XCUIElementTypeButton[@label='Photos'])"
        ));
    }
    public WebElement getFavoritesTab() {
        return driver.findElement(AppiumBy.xpath("(//XCUIElementTypeButton[@label='Favorites'])"
        ));
    }

    public WebElement getTrashTab() {
        return driver.findElement(AppiumBy.xpath(
                "//XCUIElementTypeButton[@label='Trash']"
        ));
    }
    public WebElement getAlbumTab() {
        return driver.findElement(AppiumBy.xpath(
                "//XCUIElementTypeButton[@label='Albums']"
        ));
    }

    public WebElement getSettingsButton() {
        return driver.findElement(AppiumBy.xpath(
                "//XCUIElementTypeButton[@label='Settings']"
        ));
    }
    public void waitForPhotosTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@label='Photos']"),
                "value", "1"
        ));
    }

    public void waitForFavoritesTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@label='Favorites']"),
                "value", "1"
        ));
    }

    public void waitForTrashTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@label='Trash']"),
                "value", "1"
        ));
    }

    public void waitForAlbumsTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@label='Albums']"),
                "value", "1"
        ));
    }

    public void waitForSettingsTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@label='Settings']"),
                "value", "1"
        ));
    }

    // ─── Actions ───────────────────────────────────────────────────────────────

    public void goToPhotos() {
        getPhotosTab().click();
        waitForPhotosTabActive();
    }

    public void goToFavorites() {
        getFavoritesTab().click();
        waitForFavoritesTabActive();
    }

    public void goToTrash() {
        getTrashTab().click();
        waitForTrashTabActive();
    }

    public void goToAlbums() {
        getAlbumTab().click();
        waitForAlbumsTabActive();
    }

    public void goToSettings() {
        getSettingsButton().click();
        waitForSettingsTabActive();
    }

    public boolean isLoggedIn() {
        try {
            // Settings button only exists when logged in
            List<WebElement> settingsButtons = driver.findElements(
                    AppiumBy.accessibilityId("Settings")
            );
            return !settingsButtons.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}