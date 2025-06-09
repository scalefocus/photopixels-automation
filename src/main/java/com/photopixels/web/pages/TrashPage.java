package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TrashPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5[contains(@class,'css-1ne0cvc')]")
    private WebElement overviewHeader;

    @FindBy(css = "[data-testid='CloudUploadIcon']")
    private WebElement uploadButton;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileInput;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    public TrashPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get overview header")
    public String getOverviewHeader() {
        waitForElementToBeVisible(overviewHeader);

        return overviewHeader.getText();
    }

    @Step("Upload media")
    public void clickUploadMedia() {
        waitForElementToBeVisible(uploadButton);
        uploadButton.click();
    }

    @Step("Upload media")
    public void uploadMedia(String filePath) {
        waitForElementToBeVisible(uploadButton);
        uploadButton.click();
        waitForElementToBeClickable(fileInput);
        fileInput.sendKeys(filePath);
    }

    @Step("Get upload error message")
    public String getUploadErrorMessage() {
        waitForElementToBeVisible(errorMessage);

        return errorMessage.getText();
    }

}
