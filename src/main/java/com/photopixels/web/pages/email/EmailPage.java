package com.photopixels.web.pages.email;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EmailPage {

    private WebDriver driver;

    @FindBy(id = "emailFrame")
    private WebElement emailFrame;

    @FindBy(xpath = "//body//p[contains(@style, '40px')]")
    private WebElement codeText;

    public EmailPage(WebDriver driver) {
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get reset code from the email")
    public String getCodeFromEmail() {
        driver.switchTo().frame(emailFrame);

        return codeText.getText();
    }
}
