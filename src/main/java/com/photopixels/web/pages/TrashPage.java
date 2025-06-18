package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class TrashPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    @FindBy(xpath = "//h5[text()='Trash']")
    private WebElement trashHeader;

    @FindBy(xpath = "//div[@id='root']//div[contains(@class, 'upload-panel')]")
    private WebElement trashMessagePrompt;

    @FindBy(css = "[data-testid='CheckCircleOutlineIcon']")
    private List<WebElement> selectMediaButton;

    @FindBy(css = "[data-testid='DeleteForeverIcon']")
    private WebElement deleteMediaButton;

    @FindBy(xpath = "//button[contains(@class, 'MuiButton-text') and text()='Empty Trash']")
    private WebElement emptyTrashButton;

    @FindBy(xpath = "//button[normalize-space()='Delete Permanently']")
    private WebElement deletePermanentlyConfirmationButton;

    @FindBy(xpath = "//button[contains(@class, 'MuiButton-containedError') and text()='Empty Trash']")
    private WebElement emptyTrashConfirmationButton;


    public TrashPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get trash header")
    public String getTrashHeader() {
        waitForElementToBeVisible(trashHeader);

        return trashHeader.getText();
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

    @Step("Delete media")
    public void deleteMediaPermanently() {
        waitForElementToBeVisible(deleteMediaButton);
        deleteMediaButton.click();
        waitForElementToBeVisible(deletePermanentlyConfirmationButton);
        deletePermanentlyConfirmationButton.click();
    }

    @Step("Empty trash with a button")
    public void emptyTrash() {
        waitForElementToBeVisible(emptyTrashButton);
        emptyTrashButton.click();
        waitForElementToBeVisible(emptyTrashConfirmationButton);
        emptyTrashConfirmationButton.click();
    }

    @Step("Get delete media permanently message")
    public String getDeleteMediaMessage() {
        return getStatusMessage();
    }
}
