package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CreateUserPage extends NavigationPage {

    private WebDriver driver;
    private JavascriptExecutor js;

    @FindBy(xpath = "//div[1]/h5[contains(text(), 'Create A New User')]")
    private WebElement createUserHeader;

    @FindBy(xpath = "//form//span[contains(text(), 'Admin')]")
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

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message')]//li")
    private List<WebElement> passwordFormatErrorMessages;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message')]/li[1]")
    private  WebElement characterPasswordRequirement;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message')]/li[2]")
    private  WebElement alphanumericPasswordRequirement;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message')]/li[3]")
    private  WebElement digitPasswordRequirement;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message')]/li[4]")
    private  WebElement uppercasePasswordRequirement;

    @FindBy(xpath = "//div[contains(@class, 'MuiAlert-message')]//li[1]")
    private  WebElement duplicateEmailRequirement;

    @FindBy(css = "input[placeholder='Search Users']")
    private WebElement usersSearchBarElement;

    @FindBy(css = "tbody tr.MuiTableRow-root td:nth-child(2)")
    private List<WebElement> emailElements;

    @FindBy(css = "tbody tr.MuiTableRow-root td:nth-child(3)")
    private List<WebElement> roleElements;

    public CreateUserPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get create user header")
    public String getCreateUserHeader() {
        waitForElementToBeVisible(createUserHeader);

        return createUserHeader.getText();
    }

    @Step("Create new user successfully")
    public CreateUserPage createUser(String name, String email, String password) {
        newNameUser.sendKeys(name);
        newEmailAddress.sendKeys(email);
        newPassword.sendKeys(password);
        createNewUserButton.click();

        return this;
    }

    @Step("Return select role element")
    public CreateUserPage selectAdminUserRole() {
        selectAdminRole.click();

        return this;
    }

    @Step("Get user created message")
    public String getUserCreatedMsg() {
        waitForElementToBeVisible(userCreatedMsg);

        return userCreatedMsg.getText();
    }

    @Step("Get validation message for a field using JavaScript")
    public String getValidationMessage(WebElement element) {
        return (String) js.executeScript("return arguments[0].validationMessage;", element);
    }

    @Step("Return name field attribute for verification")
    public String getNewNameUser() {
        return newNameUser.getAttribute("value");
    }

    @Step("Get name field validation message")
    public String getNewNameUserValidationMessage() {
        return getValidationMessage(newNameUser);
    }

    @Step("Return email address field element for verification")
    public String getEmailAddress() {
        return newEmailAddress.getAttribute("value");
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

    @Step("Return 8 character field text for verification")
    public String getCharacterPasswordRequirement() {
        waitForElementToBeVisible(characterPasswordRequirement);
        return characterPasswordRequirement.getText();
    }

    @Step("Return alphanumeric field element for verification")
    public String getAlphanumericPasswordRequirement() {
        waitForElementToBeVisible(alphanumericPasswordRequirement);
        return alphanumericPasswordRequirement.getText();
    }

    @Step("Return one digit field element for verification")
    public String getDigitPasswordRequirement() {
        waitForElementToBeVisible(digitPasswordRequirement);
        return digitPasswordRequirement.getText();
    }

    @Step("Return one uppercase field element for verification")
    public String getUppercasePasswordRequirement() {
        waitForElementToBeVisible(uppercasePasswordRequirement);
        return uppercasePasswordRequirement.getText();
    }

    @Step("Return duplicate email element for verification")
    public String getDuplicateEmailRequirement() {
        waitForElementToBeVisible(duplicateEmailRequirement);
        return duplicateEmailRequirement.getText();
    }

}