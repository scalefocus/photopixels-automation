package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.File;
import java.util.List;

public class AlbumsPage extends MediaContentPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5[normalize-space()='Albums']")
    private WebElement albumsHeader;

    @FindBy(xpath = "//a[normalize-space()='Create Album']")
    private WebElement createNewAlbum;

    @FindBy(xpath = "//input[@id='name']")
    private WebElement albumNameField;

    @FindBy(xpath = "//button[normalize-space()='Create Album']")
    private WebElement clickCreateAlbumButton;

    @FindBy(css = "#root div:nth-of-type(2) > div:nth-of-type(2) > div")
    private WebElement albumCreatedMessage;

    @FindBy(css = "[data-testid='DeleteIcon']")
    private WebElement deleteAlbumButton;

    @FindBy(xpath = "//button[normalize-space()='OK']")
    private WebElement confirmDelete;

    @FindBy(css = "div[role='dialog']")
    private WebElement confirmationDialog;

    @FindBy(css = "div[role='status']")
    private WebElement albumDeletedToast;

    @FindBy(css = "a.MuiTypography-root")
    private WebElement albumCardName;

    @FindBy(xpath = "//button[normalize-space()='Delete Album']")
    private WebElement deleteAlbum;

    @FindBy(css = "input[aria-label='Album selection']")
    private WebElement selectAlbum;

    @FindBy(xpath = "//button[@aria-label='Изтрий избраните']")
    private WebElement deleteSelectedAlbum;

    @FindBy(xpath = "//button[normalize-space()='Delete']")
    private WebElement deleteSelectedAlbums;

    @FindBy(xpath = "//div[@role='status']")
    private WebElement selectedAlbumDeletedMessage;

    @FindBy(xpath = "//a[normalize-space()='Test Album']")
    private WebElement albumNameLink;

    @FindBy(css = "img[alt='thumbnail']")
    private List<WebElement> albumThumbnails;

    @FindBy(xpath = "//label[normalize-space()='Browse Files']")
    private WebElement uploadButton;

    @FindBy(xpath = "//input[@type='file']")
    private WebElement fileInput;

    @FindBy(css = "span.MuiCircularProgress-root")
    private WebElement uploadLoader;

    private String deletedMessage;

    public AlbumsPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get albums header")
    public String getAlbumsHeader() {
        waitForElementToBeVisible(albumsHeader);

        return albumsHeader.getText();
    }

    @Step("Click on the Create Album button")
    public void createAlbum() {
        waitForElementToBeVisible(createNewAlbum);
        createNewAlbum.click();
    }

    @Step("Click on the Create Album button")
    public void enterAlbumsName(String albumName) {
        waitForElementToBeVisible(albumNameField);
        albumNameField.click();
        albumNameField.sendKeys(albumName);
        waitForElementToBeVisible(clickCreateAlbumButton);
        clickCreateAlbumButton.click();
    }

    @Step("Get album created message")
    public String getAlbumCreatedMessage() {

        return getStatusMessage();
    }

    @Step("Delete album function")
    public void deleteAlbum() {
        waitForElementToBeVisible(deleteAlbumButton);
        deleteAlbumButton.click();


        Alert alert = driver.switchTo().alert();
        this.deletedMessage = alert.getText();
        alert.accept();
    }

    @Step("Read toast deleted message")
    public String getToastDeletedMessage() {

        return getStatusMessage();
    }

    @Step("Click the album name")
    public void clickAlbumName() {
        waitForElementToBeVisible(albumCardName);
        albumCardName.click();
    }

    @Step("Delete album from details")
    public void deleteAlbumFromDetails() {
        waitForElementToBeVisible(deleteAlbum);
        deleteAlbum.click();

        Alert alert = driver.switchTo().alert();
        this.deletedMessage = alert.getText();
        alert.accept();
    }

    @Step("Delete album with selection")
    public void deleteAlbumWithSelection() {
        waitForElementToBeVisible(selectAlbum);
        selectAlbum.click();

        waitForElementToBeVisible(deleteSelectedAlbum);
        deleteSelectedAlbum.click();

        waitForElementToBeVisible(deleteSelectedAlbums);
        deleteSelectedAlbums.click();
    }

    @Step("Read toast deleted message from selection")
    public String getSelectedAlbumDeletedMessage() {

            return getStatusMessage();
    }

    @Step("Click album name")
    public void openAlbum() {
        waitForElementToBeVisible(albumNameLink);
        albumNameLink.click();
    }

    @Step("Check if image is present in the album")
    public boolean isImagePresentInAlbum() {
        return !albumThumbnails.isEmpty();
    }

    @Step("Wait for upload to finish")
    public void waitForUploadToFinish() {
        waitForElementToBeVisible(uploadLoader);
        waitForElementToDisappear(uploadLoader);
    }

    @Step("Upload media")
    public void uploadMediaFromAlbum(String filePath) {
        waitForElementToBeVisible(uploadButton);

        File file = new File(filePath);
        String absolutePath = file.getAbsolutePath();
        fileInput.sendKeys(absolutePath);

        waitForUploadToFinish();
    }

    @Step("Get upload success message")
    public String getUploadSuccessMessage() {

        return getStatusMessage();
    }
}
