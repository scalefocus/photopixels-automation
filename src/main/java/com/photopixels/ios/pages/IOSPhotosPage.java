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
                "(//XCUIElementTypeWindow[@index='0']//XCUIElementTypeScrollView//XCUIElementTypeImage[not(@name)])[" + index + "]"
        ));
    }
    public WebElement getPhotoByIndex2(int index) {
        return driver.findElement(AppiumBy.xpath(
                "(//XCUIElementTypeScrollView//XCUIElementTypeImage[not(@name)])[" + index + "]"
        ));
    }
    public List<WebElement> getAllPhotos2() {
        return driver.findElements(AppiumBy.xpath(
                "//XCUIElementTypeWindow[@index='0']//XCUIElementTypeScrollView//XCUIElementTypeImage[not(@name)]"
        ));
    }

    public List<WebElement> getAllPhotos() {
        return driver.findElements(AppiumBy.xpath(
                "//XCUIElementTypeScrollView//XCUIElementTypeImage[not(@name)]"
        ));
    }

    public WebElement getHeartButton() {
        return driver.findElement(AppiumBy.accessibilityId("heart"));
    }


    public WebElement getFilledHeartButton() {
        return driver.findElement(AppiumBy.accessibilityId("heart.fill"));
    }
    public WebElement getShareButton() {
        return driver.findElement(AppiumBy.accessibilityId("square.and.arrow.up"));
    }
    public List<WebElement> getDateSectionHeaders() {
        return driver.findElements(AppiumBy.xpath(
                "//XCUIElementTypeStaticText[contains(@traits, 'Header')]"
        ));
    }
    public List<WebElement> getHeartIconsOnGrid() {
        return driver.findElements(AppiumBy.xpath(
                "//XCUIElementTypeScrollView//XCUIElementTypeImage[@name='heart.fill']"
        ));
    }
    // Favorites tab badge count — value="1" on the tab button in the DOM
    public int getFavoritesTabBadgeCount() {
        WebElement favTab = driver.findElement(AppiumBy.accessibilityId("Favorites"));
        String value = favTab.getAttribute("value"); // returns "1", "2", etc.
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
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
        return driver.findElement(AppiumBy.accessibilityId("arrow.uturn.backward"));
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

    public WebElement getRestorePopUpButton() {
        return driver.findElement(AppiumBy.accessibilityId("Restore"));
    }


    // ─── Actions ───────────────────────────────────────────────────────────────

    public void clickSyncPhotos() {
        getSyncPhotosButton().click();
    }

    public void openPhoto(int index) {
        getPhotoByIndex(index).click();
    }
    public void openPhoto2(int index) {
        getPhotoByIndex2(index).click();
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
        getRestorePopUpButton().click();
    }



    public void addPhotoToFavorites() {
        getHeartButton().click();
    }
    public void removePhotoFromFavorites() {
        getFilledHeartButton().click();
    }

    public void clickConfirmRestore() {
        getConfirmRestoreButton().click();
    }
    public void clickShareButton() {
        getShareButton().click();
    }


    public List<String> getDateSectionHeaderTexts() {
        return getDateSectionHeaders()
                .stream()
                .map(WebElement::getText)
                .toList();
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
    public boolean isShareButtonVisible() {
        try {
            return getShareButton().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDeleteButtonVisible() {
        try {
            return getDeleteButton().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public boolean isLoveButtonVisible() {
        try {
            getFilledHeartButton().isDisplayed();
            return true;
        } catch (Exception e) {
            try {
                getHeartButton().isDisplayed();
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public boolean isHeartIconVisibleOnPhotoInGrid(int index) {
        // Checks the small heart.fill badge overlaid on a specific photo in the grid
        List<WebElement> hearts = getHeartIconsOnGrid();
        return index < hearts.size() && hearts.get(index).isDisplayed();
    }

    public boolean isFavoritesHeaderVisible() {
        try {
            driver.findElement(AppiumBy.xpath(
                    "//XCUIElementTypeStaticText[@name='Favorites']"
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}