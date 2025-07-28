package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class FavoritesPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    @FindBy(xpath = "//h5[text()='Favorites']")
    private WebElement favoritesHeader;

    @FindBy(css = "[data-testid='FavoriteIcon']")
    private List<WebElement> favoriteIcon;

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


    public FavoritesPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get favorites header")
    public String getFavoritesHeader() {
        waitForElementToBeVisible(favoritesHeader);

        return favoritesHeader.getText();
    }

    @Step("Select media")
    public void selectMediaByIndex(int index) {
        waitForAllElementsToBeVisible(selectMediaButton);
        List<WebElement> icons = selectMediaButton;
        if (index >= 0 && index < icons.size()) {
            icons.get(index).click();
        } else {
            throw new IllegalArgumentException("Invalid index for CheckCircleIcon: " + index);
        }
    }

    @Step("Verify all media items have a favorite icon")
    public boolean allMediaHasFavoriteIcon() {
        if (favoriteIcon.isEmpty()) {
        }

        for (WebElement icon : favoriteIcon) {
            if (!icon.isDisplayed()) {
                return false;
            }
        }

        return true;
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
