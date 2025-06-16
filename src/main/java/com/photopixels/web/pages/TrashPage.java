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

    @FindBy(xpath = "//button[normalize-space()='Delete Permanently']")
    private WebElement deletePermanentlyButton;


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
        waitForElementToBeVisible(deletePermanentlyButton);
        deletePermanentlyButton.click();
    }
}
