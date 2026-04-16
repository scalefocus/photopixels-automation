package com.photopixels.ios.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class IOSAlbumPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    public IOSAlbumPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ─── Element Getters ───────────────────────────────────────────────────────

    public WebElement getAlbumsHeader() {
        return driver.findElement(AppiumBy.name("Albums"));
    }

    public WebElement getAlbumByName(String albumName) {
        return driver.findElement(AppiumBy.xpath(
                "//XCUIElementTypeStaticText[@name='" + albumName + "']"
        ));
    }

    public List<WebElement> getAllAlbums() {
        return driver.findElements(AppiumBy.xpath(
                "//XCUIElementTypeScrollView//XCUIElementTypeStaticText[@name!='Add new album']"
        ));
    }

    public WebElement getAddNewAlbumButton() {
        return driver.findElement(AppiumBy.name("Add new album"));
    }

    public WebElement getAddAlbumIcon() {
        return driver.findElement(AppiumBy.name("plus"));
    }

    public WebElement getAlbumImageByIndex(int index) {
        return driver.findElement(AppiumBy.xpath(
                "(//XCUIElementTypeScrollView//XCUIElementTypeImage[@name='rectangle.stack.fill'])[" + index + "]"
        ));
    }
    public WebElement albumNamePlaceholder () {
        return driver.findElement(AppiumBy.xpath("//XCUIElementTypeTextField[@value=\"Album name\"]"));
    }
    public WebElement cancelButton() {
        return driver.findElement(AppiumBy.accessibilityId("Cancel"));
    }
    public WebElement createButton() {
        return driver.findElement(AppiumBy.accessibilityId("Create"));
    }
    public WebElement moreOptions () {
        return driver.findElement(AppiumBy.accessibilityId("ellipsis"));
    }
    public WebElement addItemsButton () {
        return driver.findElement(AppiumBy.xpath("//XCUIElementTypeButton[@name=\"plus\"]"));
    }
    public WebElement removeAlbumButton () {
        return driver.findElement(AppiumBy.xpath("//XCUIElementTypeButton[@name=\"trash\"]"));
    }
    public WebElement addPhotosButton() {
        return driver.findElement(AppiumBy.accessibilityId("Add Photos"));
    }
    public WebElement noPhotosIndicator () {
        return driver.findElement(AppiumBy.accessibilityId("No Photos"));
    }
    public WebElement deleteButtonDialog () {
        return driver.findElement(AppiumBy.accessibilityId("Delete"));
    }
    public WebElement addSelectedPhotosButton() {
        return driver.findElement(AppiumBy.xpath("//XCUIElementTypeButton[contains(@name,'Add') and @visible='true']"));
    }
    public WebElement cancelSelectedPhotosButton() {
        return driver.findElement(AppiumBy.xpath("//XCUIElementTypeButton[@name=\"Cancel\"]"));
    }
    public List<WebElement> images () { return driver.findElements(AppiumBy.xpath("//XCUIElementTypeImage[not(@name)]"));}

    // ─── Actions ───────────────────────────────────────────────────────────────

    public void clickAlbumByName(String albumName) {
        getAlbumByName(albumName).click();
    }
    public void clickAlbumByIndex(int index) {
        getAlbumImageByIndex(index).click();
    }
    public void clickAddNewAlbum() {
        getAddNewAlbumButton().click();
    }

    public void clickAddAlbumIcon() {
        getAddAlbumIcon().click();
    }


    // ─── State Checks ──────────────────────────────────────────────────────────

    public boolean isAlbumVisible(String albumName) {
        try {
            return getAlbumByName(albumName).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isAlbumsHeaderVisible() {
        try {
            return getAlbumsHeader().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isAddNewAlbumButtonVisible() {
        try {
            return getAddNewAlbumButton().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isAddAlbumIconVisible() {
        try {
            return getAddAlbumIcon().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isNoPhotosIndicatorVisible() {
        return noPhotosIndicator().isDisplayed();
    }
    public boolean waitForAlbumVisible(String albumName) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(30));
            customWait.pollingEvery(Duration.ofMillis(500))
                    .ignoring(NoSuchElementException.class)
                    .until((d) -> isAlbumVisible(albumName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getAlbumCount() {
        return getAllAlbums().size();
    }
    public int getAlbumImageCount() {
        return images().size();
    }

    public void enterAlbumName (String albumName) {
        albumNamePlaceholder().sendKeys(albumName);
    }
    public void cancelAlbumCreation () {
        cancelButton().click();
    }
    public void createAlbum () {
        createButton().click();
    }
    public void openOptions () {
        moreOptions().click();
    }
    public void addItems () {
        addItemsButton().click();
    }
    public void removeAlbum () {
        removeAlbumButton().click();
    }
    public void addPhotos () {
        addPhotosButton().click();
    }
    public void deleteActionDialog () {
        deleteButtonDialog().click();
    }

    public void deleteAlbumAction () {
        openOptions();
        removeAlbum();
        deleteActionDialog();
    }

    public void addSelectedPhotos(){
        addSelectedPhotosButton().click();
    }
    public void cancelSelectedPhotos() {
        cancelSelectedPhotosButton().click();
    }
    public void selectPhoto(int index) {
        images().get(index).click();
    }

}