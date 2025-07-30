package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MediaContentPage extends NavigationPage {

    private WebDriver driver;


    @FindBy(css = "[data-testid='CheckCircleOutlineIcon']")
    private List<WebElement> selectMediaButton;

    @FindBy(xpath = "//*[@id='root']//header//p")
    private WebElement selectedCountText;

    @FindBy(css = "[data-testid='DeleteIcon']")
    private WebElement deleteMediaButton;

    @FindBy(css = "[data-testid='DeleteForeverIcon']")
    private WebElement deleteMediaPermanentlyButton;

    @FindBy(xpath = "//button[normalize-space()='Delete Permanently']")
    private WebElement deletePermanentlyConfirmationButton;

    @FindBy(xpath = "//button[normalize-space()='Move to Trash']")
    private WebElement moveToTrashButton;


    public MediaContentPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Select media")
    public void selectMedia(int index) {
        waitMs();
        if (index >= 0 && index < selectMediaButton.size()) {
            selectMediaButton.get(index).click();
        } else {
            throw new IllegalArgumentException("Invalid index for CheckCircleIcon: " + index);
        }
    }

    @Step("Get selected media info text")
    public String getSelectedMediaText() {
        waitForElementToBeVisible(selectedCountText);
        return selectedCountText.getText().trim();
    }

    @Step("Delete media")
    public void deleteMedia() {
        waitForElementToBeVisible(deleteMediaButton);
        deleteMediaButton.click();
        waitForElementToBeVisible(moveToTrashButton);
        moveToTrashButton.click();
    }

    @Step("Delete media")
    public void deleteMediaPermanently() {
        waitForElementToBeVisible(deleteMediaPermanentlyButton);
        deleteMediaPermanentlyButton.click();
        waitForElementToBeVisible(deletePermanentlyConfirmationButton);
        deletePermanentlyConfirmationButton.click();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }
}
