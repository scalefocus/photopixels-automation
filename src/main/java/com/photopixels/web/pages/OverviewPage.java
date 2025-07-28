package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.util.List;

public class OverviewPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5[contains(@class,'css-1ne0cvc')]")
    private WebElement overviewHeader;

    @FindBy(css = "[data-testid='CloudUploadIcon']")
    private WebElement uploadButton;

    @FindBy(css = "span.MuiCircularProgress-root")
    private WebElement uploadLoader;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileInput;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    @FindBy(css = "[data-testid='CheckCircleOutlineIcon']")
    private List<WebElement> selectMediaButton;

    @FindBy(css = "[data-testid='CheckCircleIcon']")
    private List<WebElement> selectedMedia;

    @FindBy(xpath = "//*[@id='root']//header//p")
    private WebElement selectedCountText;

    @FindBy(css = "[data-testid='DeleteIcon']")
    private WebElement deleteMediaButton;

    @FindBy(xpath = "//button[normalize-space()='Move to Trash']")
    private WebElement moveToTrashButton;

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

    @Step("Select media")
    public void selectMedia(int index) {
        if (index >= 0 && index < selectMediaButton.size()) {
            selectMediaButton.get(index).click();
        } else {
            throw new IllegalArgumentException("Invalid index for CheckCircleIcon: " + index);
        }
    }

    @Step("Delete media")
    public void deleteMedia() {
        waitForElementToBeVisible(deleteMediaButton);
        deleteMediaButton.click();
        waitForElementToBeVisible(moveToTrashButton);
        moveToTrashButton.click();
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
    public String getMediaAddedToFavoritesMessage() {

        return getStatusMessage();
    }

    @Step("Get selected media info text")
    public String getSelectedMediaText() {
        waitForElementToBeVisible(selectedCountText);
        return selectedCountText.getText().trim();
    }
}
