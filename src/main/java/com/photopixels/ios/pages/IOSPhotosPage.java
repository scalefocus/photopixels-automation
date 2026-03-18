package com.photopixels.ios.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class IOSPhotosPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public IOSPhotosPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ─── Element Getters ───────────────────────────────────────────────────────

    public WebElement getSyncPhotosButton() {
        return driver.findElement(AppiumBy.accessibilityId("Sync Photos"));
    }

    public WebElement getPhotoByIndex(int index) {
        return driver.findElement(AppiumBy.xpath(
                "//XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeOther[1]/XCUIElementTypeImage[" + index + "]"
        ));
    }

    public List<WebElement> getAllPhotos() {
        return driver.findElements(AppiumBy.xpath(
                "//XCUIElementTypeScrollView/XCUIElementTypeOther[3]/XCUIElementTypeOther[1]/XCUIElementTypeImage[not(@name)]"
        ));
    }

    public WebElement getHeartButton() {
        return driver.findElement(AppiumBy.accessibilityId("heart"));
    }

    public WebElement getFilledHeartButton() {
        return driver.findElement(AppiumBy.accessibilityId("heart.fill"));
    }

    public WebElement getDeleteButton() {
        return driver.findElement(AppiumBy.accessibilityId("trash"));
    }

    public WebElement getConfirmDeleteButton() {
        return driver.findElement(AppiumBy.accessibilityId("Delete"));
    }

    public WebElement getCancelDeleteButton() {
        return driver.findElement(AppiumBy.accessibilityId("Cancel"));
    }

    public WebElement getConfirmRestoreButton() {
        return driver.findElement(AppiumBy.accessibilityId("Restore"));
    }

    public WebElement getBackButton() {
        return driver.findElement(
                AppiumBy.accessibilityId("Back")
        );
    }

    public WebElement getRestoreButton() {
        return driver.findElement(AppiumBy.accessibilityId("arrow.uturn.backward.circle"));
    }

    public WebElement getDeletePhotoPopUp() {
        return driver.findElement(AppiumBy.xpath(
                "//XCUIElementTypeStaticText[@name='Delete Photo']"
        ));
    }

    public WebElement getDeletePhotoPermanentlyPopUp() {
        return driver.findElement(AppiumBy.xpath(
                "//XCUIElementTypeStaticText[@name='Delete Permanently?']"
        ));
    }

    public WebElement getRestorePhotoPopUp() {
        return driver.findElement(AppiumBy.xpath(
                "//XCUIElementTypeStaticText[@name='Restore Photos?']"
        ));
    }

    // ─── Actions ───────────────────────────────────────────────────────────────

    public void clickSyncPhotos() {
        getSyncPhotosButton().click();
    }

    public void openPhoto(int index) {
        getPhotoByIndex(index).click();
    }

    public void openPhoto() {
        openPhoto(1); // default index = 1
    }

    public int getPhotoCount() {
        return getAllPhotos().size();
    }

    public void deletePhoto() {
        getDeleteButton().click();
    }

    public void addToFavorites() {
        getHeartButton().click();
    }

    public void removeFromFavorites() {
        getFilledHeartButton().click();
    }

    public void confirmDelete() {
        getConfirmDeleteButton().click();
    }

    public void cancelDelete() {
        getCancelDeleteButton().click();
    }

    public void clickBackButton() {
        getBackButton().click();
    }

    public void restorePhoto() {
        getRestoreButton().click();
    }

    public void addPhotoToFavorites() {
        getHeartButton().click();
    }

    public void clickConfirmRestore() {
        getConfirmRestoreButton().click();
    }

    // ─── State Checks ──────────────────────────────────────────────────────────

    public boolean isPhotoFavorite() {
        try {
            return getFilledHeartButton().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean canBeAddedToFavorites() {
        try {
            return getHeartButton().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}