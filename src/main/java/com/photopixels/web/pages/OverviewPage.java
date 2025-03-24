package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class OverviewPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5[contains(@class,'css-1ne0cvc')]")
    private WebElement overviewHeader;

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
}
