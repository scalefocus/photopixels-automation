package com.photopixels.web.pages;

import com.photopixels.helpers.WaitOperationHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NavigationPage extends WaitOperationHelper {

    private WebDriver driver;

    @FindBy(xpath = "//p[contains(@class,'css-pfq02h')]")
    private WebElement userName;

    @FindBy(xpath = "//nav/a[@href='/']")
    private WebElement overviewMenu;

    @FindBy(xpath = "//a[@href='/settings']")
    private WebElement settingsMenu;

    @FindBy(xpath = "//a[@href='/users']")
    private WebElement usersMenu;

    @FindBy(xpath = "//a[@href='/create-user']")
    private WebElement createUserMenu;

    @FindBy(xpath = "//a[@href='/admin-settings']")
    private WebElement adminSettingsMenu;

    @FindBy(xpath = "//a[@href='/trash']")
    private WebElement trashMenu;

    @FindBy(xpath = "//nav/div[@role='button']")
    private WebElement logoutButton;

    // This element is used for info messages that are displayed for successful operations like save and create
    @FindBy(xpath = "//div[@role='status']")
    private WebElement statusMessage;

    public NavigationPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get user name")
    public String getUserName() {
        waitForElementToBeVisible(userName);

        return userName.getText();
    }

    @Step("Go to Create New User tab")
    public CreateUserPage goToCreateNewUser() {
        waitForElementToBeVisible(createUserMenu);
        createUserMenu.click();

        return new CreateUserPage(driver);
    }

    @Step("Go to Users tab")
    public UsersPage goToUserTab() {
        waitForElementToBeVisible(usersMenu);
        usersMenu.click();

        return new UsersPage(driver);
    }

    @Step("Go to Trash tab")
    public TrashPage goToTrashTab() {
        waitForElementToBeVisible(trashMenu);
        trashMenu.click();

        return new TrashPage(driver);
    }

    @Step("Log out of main application")
    public LoginPage logOut() {
        waitForElementToBeVisible(logoutButton);
        logoutButton.click();

        return new LoginPage(driver);
    }

    @Step("Get status message")
    public String getStatusMessage() {
        waitForElementToBeVisible(statusMessage);

        return statusMessage.getText().trim();
    }
}
