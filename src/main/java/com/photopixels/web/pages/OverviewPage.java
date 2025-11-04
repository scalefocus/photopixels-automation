package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;

public class OverviewPage extends MediaContentPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5[contains(@class,'css-1ne0cvc')]")
    private WebElement overviewHeader;

    @FindBy(css = "[data-testid='CloudUploadIcon']")
    private WebElement uploadButton;

    @FindBy(css = "span.MuiCircularProgress-root")
    private WebElement uploadLoader;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileInput;

    @FindBy(css = "[aria-label='Add to Favorites']")
    private WebElement addToFavoritesIcon;


    public OverviewPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get overview header")
    public String getOverviewHeader() {
        waitForElementToBeVisible(overviewHeader);

        return overviewHeader.getText();
    }

    @Step("Wait for upload to finish")
    public void waitForUploadToFinish() {
        waitForElementToBeVisible(uploadLoader);
        waitForElementToDisappear(uploadLoader);
    }

    @Step("Upload media")
    public void uploadMedia(String filePath) {
        waitForElementToBeVisible(uploadButton);

        File file = new File(filePath);
        String absolutePath = file.getAbsolutePath();
        fileInput.sendKeys(absolutePath);

        waitForUploadToFinish();
    }

    @Step("Add to Favorites")
    public void addToFavoritesMedia() {
        waitForElementToBeVisible(addToFavoritesIcon);
        addToFavoritesIcon.click();
    }

    @Step("Get upload success message")
    public String getUploadSuccessMessage() {

        return getStatusMessage();
    }

    @Step("Get delete media message")
    public String getDeleteMediaMessage() {

        return getStatusMessage();
    }

    @Step("Get media added to Favorites message")
    public String getFavoriteMediaMessage() {

        return getStatusMessage();
    }
}
