package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CreateUserPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5")
    private WebElement createUserHeader;

    @FindBy(xpath = "//form//input[@type='radio']")
    private WebElement selectAdminRole;

    @FindBy(xpath = "//*[@id='name']")
    private WebElement newNameUser;

    @FindBy(xpath = "//*[@id='email']")
    private WebElement newEmailAddress;

    @FindBy(xpath = "//*[@id='password']")
    private WebElement newPassword;

    @FindBy(xpath = "//*[@id='root']//form/button")
    private WebElement createNewUserButton;

    @FindBy(css = ".MuiAlert-standardSuccess .MuiAlert-message")
    private WebElement userCreatedMsg;

    @FindBy(css = "form .MuiAlert-message li")
    private List<WebElement> passwordErrorMessages;

    public CreateUserPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get create user header")
    public String getCreateUserHeader() {
        waitForElementToBeVisible(createUserHeader);

        return createUserHeader.getText();
    }

    @Step("Create new user successfully")
    public void createUser(String name, String email, String password) {
        waitForElementToBeClickable(newNameUser);

        newNameUser.clear();
        newNameUser.sendKeys(name);

        newEmailAddress.sendKeys(email);
        newPassword.sendKeys(password);

        createNewUserButton.click();
    }

    @Step("Return select role element")
    public void selectAdminUserRole() {
        selectAdminRole.click();
    }

    @Step("Get user created message")
    public String getUserCreatedMsg() {
        waitForElementToBeVisible(userCreatedMsg);

        return userCreatedMsg.getText();
    }

    @Step("Get validation message for a field using JavaScript")
    public String getValidationMessage(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        return (String) js.executeScript("return arguments[0].validationMessage;", element);
    }

    @Step("Get name field validation message")
    public String getNewNameUserValidationMessage() {
        return getValidationMessage(newNameUser);
    }

    @Step("Get email address field validation message")
    public String getEmailAddressValidationMessage() {
        return getValidationMessage(newEmailAddress);
    }

    @Step("Return password field element for verification")
    public String getPassword() {
        return newPassword.getAttribute("value");
    }

    @Step("Get password field validation message")
    public String getPasswordValidationMessage() {
        return getValidationMessage(newPassword);
    }

    @Step("Get all error messages")
    public List<String> getErrorMessages() {
        waitForAllElementsToBeVisible(passwordErrorMessages);

        return passwordErrorMessages.stream()
                .map(element -> element.getText().trim())
                .collect(Collectors.toList());
    }
}