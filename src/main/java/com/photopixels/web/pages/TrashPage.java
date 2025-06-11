package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TrashPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(css = ".error-message")
    private WebElement errorMessage;

    public TrashPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get upload error message")
    public String getUploadErrorMessage() {
        waitForElementToBeVisible(errorMessage);

        return errorMessage.getText();
    }

}
