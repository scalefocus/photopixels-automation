package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OverviewPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5[contains(@class,'css-1ne0cvc')]")
    private WebElement overviewHeader;

    @FindBy(css = "[data-testid='CloudUploadIcon']")
    private WebElement uploadButton;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileInput;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    @FindBy(css = "[data-testid='CheckCircleOutlineIcon']")
    private List<WebElement> selectMediaButton;

    @FindBy(css = "[data-testid='CheckCircleIcon']")
    private List<WebElement> selectedMedia;

    @FindBy(css = "[data-testid='DeleteIcon']")
    private WebElement deleteMediaButton;

    @FindBy(xpath = "//body/div[2]/div[3]//button[2]")
    private WebElement moveToTrashButton;

    @FindBy(xpath = "//*[@id='root']/div[2]/div/div/div[2]")
    private WebElement successfulUploadMessage;

    @FindBy(xpath = "//*[contains(text(), 'Object(s) trashed successfully')]")
    private WebElement deleteSuccessMessage;


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

    @Step("Click upload media button")
    public void clickUploadMedia() {
        waitForElementToBeVisible(uploadButton);
        uploadButton.click();
    }

    @Step("Upload media")
    public void uploadMedia(String filePath) {
        waitForElementToBeVisible(uploadButton);
        fileInput.sendKeys(filePath);
    }

    @Step("Get upload error message")
    public String getUploadErrorMessage() {
        waitForElementToBeVisible(errorMessage);

        return errorMessage.getText();
    }

    @Step("Select media")
    public void selectMedia(int index) {
        List<WebElement> icons = selectMediaButton;
        if (index >= 0 && index < icons.size()) {
            icons.get(index).click();
        } else {
            throw new IllegalArgumentException("Invalid index for CheckCircleIcon: " + index);
        }
    }

    @Step("Check selected media")
    public boolean isNthMediaSelected(int index) {
        List<WebElement> allIcons = selectedMedia;
        return index < allIcons.size();
    }

    @Step("Delete media")
    public void deleteMedia() {
        waitForElementToBeVisible(deleteMediaButton);
        deleteMediaButton.click();
        waitForElementToBeVisible(moveToTrashButton);
        moveToTrashButton.click();
    }

    @Step("Get upload success message")
    public String getUploadSuccessMessage() {
        waitForElementToBeVisible(successfulUploadMessage);

        return successfulUploadMessage.getText();
    }

    @Step("Get delete media message")
    public String getDeleteMediaMessage() {
        waitForElementToBeVisible(deleteSuccessMessage);

        return deleteSuccessMessage.getText();
    }
}
