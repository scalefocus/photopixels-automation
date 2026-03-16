package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class TrashPage extends MediaContentPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5[text()='Trash']")
    private WebElement trashHeader;

    @FindBy(xpath = "//button[normalize-space()='Empty Trash']")
    private WebElement emptyTrashButton;

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
