package com.photopixels.ios.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
    public void waitForPhotosTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Photos']"),
                "value", "1"
        ));
    }

    public void waitForFavoritesTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Favorites']"),
                "value", "1"
        ));
    }

    public void waitForTrashTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Trash']"),
                "value", "1"
        ));
    }

    public void waitForAlbumsTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Albums']"),
                "value", "1"
        ));
    }

    public void waitForSettingsTabActive() {
        wait.until(ExpectedConditions.attributeToBe(
                AppiumBy.xpath("//XCUIElementTypeButton[@name='Settings']"),
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

    public void goToSettings() {
        getSettingsButton().click();
        waitForSettingsTabActive();
    }
}