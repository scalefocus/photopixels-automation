package com.photopixels.web.pages;

import com.photopixels.helpers.WaitOperationHelper;
import io.qameta.allure.Step;
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

    @FindBy(xpath = "//li[contains(., 'Email /'test710270045@test.com/' is already taken.')]")
    private  WebElement duplicateEmailRequirement;

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

        return new CreateUserPage(driver);
    }

    @Step("Return select role element")
    public CreateUserPage selectAdminUserRole() {
        selectAdminRole.click();

        return new CreateUserPage(driver);
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

    @Step("Return name field element for verification")
    public WebElement getNewNameUser() {
        return newNameUser;
    }

    @Step("Return email address field element for verification")
    public WebElement getEmailAddress() {
        return newEmailAddress;
    }

    @Step("Return password field element for verification")
    public WebElement getPassword() {
        return newPassword;
    }

    @Step("Return 8 character field text for verification")
    public String getCharacterPasswordRequirement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(characterPasswordRequirement));
        return characterPasswordRequirement.getText();
    }

    @Step("Return alphanumeric field element for verification")
    public String getAlphanumericPasswordRequirement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(alphanumericPasswordRequirement));
        return alphanumericPasswordRequirement.getText();
    }

    @Step("Return one digit field element for verification")
    public String getDigitPasswordRequirement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(digitPasswordRequirement));
        return digitPasswordRequirement.getText();
    }

    @Step("Return one uppercase field element for verification")
    public String getUppercasePasswordRequirement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(uppercasePasswordRequirement));
        return uppercasePasswordRequirement.getText();
    }

    @Step("Return duplicate email element for verification")
    public String getDuplicateEmailRequirement() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(duplicateEmailRequirement));
        return duplicateEmailRequirement.getText();
    }
}