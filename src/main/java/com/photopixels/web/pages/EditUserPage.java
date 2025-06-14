package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class EditUserPage extends NavigationPage {

    private WebDriver driver;

    @FindBy(css = "#root h5")
    private WebElement editUserHeader;

    @FindBy(xpath = "//*[contains(text(), 'Delete User')]/ancestor::div[@role='button']")
    private WebElement deleteUserOption;

    @FindBy(css = "[data-testid='DeleteIcon']")
    private WebElement deleteUserButton;

    @FindBy(css = "#root p")
    private WebElement userQuotaText;

    @FindBy(xpath = "//*[contains(text(), 'Change User Quota')]/ancestor::div[@role='button']")
    private WebElement userQuotaEdit;

    @FindBy(css = ".MuiFormControl-root input[type='number']")
    private WebElement quotaValueSetter;

    @FindBy(css = "[data-testid='button-change-new-quota']")
    private WebElement quotaFormSubmit;

    @FindBy(xpath = "//p[contains(@class,'css-1bfbqgm')]")
    private WebElement quotaValue;

    @FindBy(xpath = "//p[contains(text(), 'Reset Password')]")
    private WebElement resetUserPassword;

    @FindBy(css = "#root input[name='password']")
    private WebElement newUserPassword;

    @FindBy(css = "#root input[name='confirmPassword']")
    private WebElement confirmNewPassword;

    @FindBy(css = "button[data-testid='button-reset-password']")
    private WebElement resetPasswordBtn;

    @FindBy(css = "div[role='status'][aria-live='polite']")
    private WebElement passwordErrorMessage;


    public EditUserPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }


    @Step("Delete user function")
    public void deleteUser() {
        waitForElementToBeVisible(deleteUserOption);
        deleteUserOption.click();

        waitForElementToBeVisible(deleteUserButton);
        deleteUserButton.click();
    }

    @Step("Get user deleted message")
    public String getUserDeletedMsg() {
        return getStatusMessage();
    }

    @Step("Edit user quote")
    public void editUserQuota(String quotaValue) {
        waitForElementToBeVisible(userQuotaEdit);
        userQuotaEdit.click();
        waitForElementToBeVisible(quotaValueSetter);
        quotaValueSetter.click();
        quotaValueSetter.sendKeys(Keys.chord(Keys.CONTROL, "a")); // .clear() does not work on this element,
        // likely because it's a custom UI component (React Select), hence sending BACKSPACE/DELETE keys is necessary.

        quotaValueSetter.sendKeys(quotaValue);
        quotaFormSubmit.click();
    }

    @Step("Get user quota changed message")
    public String getUserQuotaChangedMessage() {
        return getStatusMessage();
    }

    @Step("Get validation message for a field using JavaScript")
    private String getValidationMessage(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return arguments[0].validationMessage;", element);
    }

    @Step("Get name field validation message")
    public String getQuotaValueValidationMessage() {

        return getValidationMessage(quotaValueSetter);
    }

    @Step("Get quota paragraph text")
    public String getQuotaParagraphText() {
        waitForElementToBeVisible(quotaValue);
        return quotaValue.getText().trim();
    }

    @Step("Click password reset drop down")
    public void userPasswordReset() {
        waitForElementToBeVisible(resetUserPassword);
        resetUserPassword.click();
    }

    @Step("Enter new User password")
    public void enterNewUserPassword(String newPassword, String newConfirmedPassword) {
        waitForElementToBeVisible(newUserPassword);
        newUserPassword.click();
        newUserPassword.sendKeys(newPassword);
        confirmNewPassword.clear();
        confirmNewPassword.click();
        confirmNewPassword.sendKeys(newConfirmedPassword);
    }

    @Step("Click password reset")
    public void clickPasswordReset() {
        waitForElementToBeVisible(resetPasswordBtn);
        resetPasswordBtn.click();
    }

    @Step("Get password changed message")
    public String getPasswordChangedMessage() {
        return getStatusMessage();
    }

    @Step("Get password changed incorrectly message")
    public String getPasswordChangedErrorMessage() {
        waitForElementToBeVisible(passwordErrorMessage);

        return passwordErrorMessage.getText().trim();
    }
}