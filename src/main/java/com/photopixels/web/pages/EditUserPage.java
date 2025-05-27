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
    private JavascriptExecutor js;

    @FindBy(css = "[data-testid='button-edit-user']")
    private WebElement editUserButton;

    @FindBy(css = "#root h5")
    private WebElement editUserHeader;

    @FindBy(xpath = "//p[contains(text(), 'Delete User')]")
    private WebElement deleteUserOption;

    @FindBy(css = "[data-testid='DeleteIcon']")
    private WebElement deleteUserButton;

    @FindBy(xpath = "//*[contains(normalize-space(text()), 'Account Deleted.')]")
    private WebElement userDeletedMessage;

    @FindBy(css = "#root p")
    private WebElement userQuotaText;

    @FindBy(xpath = "//*[contains(text(), 'Change User Quota')]/ancestor::div[@role='button']")
    private WebElement userQuotaEdit;

    @FindBy(css = ".MuiFormControl-root input[type='number']")
    private WebElement quotaValueSetter;

    @FindBy(css = "[data-testid='button-change-new-quota']")
    private WebElement quotaFormSubmit;

    @FindBy(xpath = "//div[contains(@class, 'go') and contains(text(), 'Quota changed successfully')]") //No other way to handle the locator, due to the lack of attributes that could be handled by selenium.
    private WebElement quotaChangedValidationMessage;

    @FindBy(xpath = "//p[contains(normalize-space(.), 'of') and contains(normalize-space(.), 'GB')]") //No other way to handle this locator, given it's inner properties needed to check the values of storage per user.
    private WebElement quotaValue;

    @FindBy(xpath = "//p[contains(text(), 'Reset Password')]")
    private WebElement resetUserPassword;

    @FindBy(css = "#root input[name='password']")
    private WebElement newUserPassword;

    @FindBy(css = "#root input[name='confirmPassword']")
    private WebElement confirmNewPassword;

    @FindBy(css = "button[data-testid='button-reset-password']")
    private WebElement resetPasswordBtn;

    @FindBy(xpath = "//div[contains(text(), 'Password changed successfully')]")
    private WebElement passwordResetConfirmation;

    @FindBy(css = "div[role='status'][aria-live='polite']")
    private WebElement passwordErrorMessage;


    public EditUserPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Edit user element")
    public EditUserPage clickEditUser() {
        waitForElementToBeVisible(editUserButton);
        editUserButton.click();
        return new EditUserPage(driver);
    }

    @Step("Delete user function")
    public void deleteUser() {
        waitForElementToBeVisible(editUserHeader);
        deleteUserOption.click();
        waitForElementToBeVisible(deleteUserButton);
        deleteUserButton.click();
    }

    @Step("Get user deleted message")
    public String getUserDeletedMsg() {
        waitForElementToBeVisible(userDeletedMessage);

        return userDeletedMessage.getText();
    }

    @Step("Edit user quote")
    public void editUserQuota(String quotaValue) {
        waitForElementToBeVisible(userQuotaText);
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
        waitForElementToBeVisible(quotaChangedValidationMessage);

        return quotaChangedValidationMessage.getText().trim();
    }

    @Step("Get validation message for a field using JavaScript")
    private String getValidationMessage(WebElement element) {
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
        waitForElementToBeVisible(passwordResetConfirmation);

        return passwordResetConfirmation.getText().trim();
    }

    @Step("Get password changed incorrectly message")
    public String getPasswordChangedErrorMessage() {
        waitForElementToBeVisible(passwordErrorMessage);

        return passwordErrorMessage.getText().trim();
    }
}