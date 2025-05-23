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

    @FindBy(xpath = "//nav/div[@role='button']")
    private WebElement logoutButton;

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
}
