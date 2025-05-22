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

public class UsersPage extends NavigationPage {

    private WebDriver driver;
    private JavascriptExecutor js;

    @FindBy(css = "input[placeholder='Search Users']")
    private WebElement usersSearchBarElement;

    @FindBy(css = "tbody tr.MuiTableRow-root td:nth-child(2)")
    private List<WebElement> emailElements;

    @FindBy(css = "tbody tr.MuiTableRow-root td:nth-child(3)")
    private List<WebElement> roleElements;

    @FindBy(css = "table tbody tr:first-child td:nth-child(6) button")
    private WebElement editUserButton;

    @FindBy(css = "#root h5")
    private WebElement editUserHeader;

    @FindBy(xpath = "//div[@id='root']/div[1]/div/div[2]/div[2]/div[6]/div/div[1]")
    private WebElement deleteUserOption;

    @FindBy(xpath = "//div[@id='root']//div[6]//div[2]//button[2]")
    private WebElement deleteUserButton;

    @FindBy(xpath = "//*[contains(normalize-space(text()), 'Account Deleted.')]")
    private WebElement userDeletedMessage;

    @FindBy(css = "#root div:nth-child(2) div:nth-child(2) div:nth-child(1) p")
    private WebElement userQuotaText;

    @FindBy(xpath = "//div[@id='root']//div[2]//div[3]//div[1]/div[2]")
    private WebElement userQuotaEdit;

    @FindBy(css = ".MuiFormControl-root input[type='number']")
    private WebElement quotaValueSetter;

    @FindBy(xpath = "//div[@id='root']//div[2]//div[3]//div[2]//form//div[2]/button[2]")
    private WebElement quotaFormSubmit;

    @FindBy(xpath = "//div[contains(@class, 'go') and contains(text(), 'Quota changed successfully')]") //No other way to handle the locator, due to the lack of attributes that could be handled by selenium.
    private WebElement quotaChangedValidationMessage;

    @FindBy(xpath = "//*[@id='root']/div[1]/div/div[2]/div[2]/div[2]/p")
    private WebElement quotaValue;

    @FindBy(xpath = "//*[@id='root']/div[1]/div/div[2]/div[2]/div[5]/div/div[1]/div[1]")
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


    public UsersPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Return users search bar element")
    public UsersPage searchUser(String credential) {
        waitForElementToBeVisible(usersSearchBarElement);
        usersSearchBarElement.click();
        usersSearchBarElement.sendKeys(credential);

        return this;
    }

    @Step("Get emails from search results")
    public List<String> getEmailsFromResults() {
        return emailElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Verify search results contain correct email")
    public boolean hasSearchResultEmail(String expectedEmail) {
        List<String> emails = getEmailsFromResults();
        if (emails.isEmpty()) {
            return false;
        }
        return emails.contains(expectedEmail);
    }

    @Step("Get roles from search results")
    public List<String> getRolesFromResults() {
        return roleElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Verify search result role is expected role")
    public boolean hasSearchResultRole(String expectedRole) {
        List<String> roles = getRolesFromResults();
        if (roles.isEmpty()) {
            return false;
        }
        String actualRole = roles.get(0); // Since we expect exactly one result
        return actualRole.equalsIgnoreCase(expectedRole);
    }

    @Step("Edit user element")
    public void clickEditUser() {
        waitForElementToBeVisible(editUserButton);
        editUserButton.click();
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
        quotaValueSetter.sendKeys(Keys.chord(Keys.CONTROL, "a"));
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