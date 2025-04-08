package com.photopixels.web;

import com.photopixels.base.WebBaseTest;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.web.pages.CreateUserPage;
import com.photopixels.web.pages.LoginPage;
import com.photopixels.web.pages.OverviewPage;
import io.qameta.allure.*;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.photopixels.constants.Constants.*;

@Listeners(StatusTestListener.class)
@Feature("Web")
public class CreateNewUserTests extends WebBaseTest {

    private String newEmail;
    private String password;
    private String adminEmail;
    private String adminPassword;
    private String randomName;
    private String invalidEmail;
    private String invalidPassword;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        randomName = "User_" + RandomString.make(5);
        password = inputData.getPassword();
        adminEmail = inputData.getUsernameAdmin();
        adminPassword = inputData.getPasswordAdmin();
        invalidEmail = inputData.getInvalidEmail();
        invalidPassword = inputData.getInvalidPassword();
    }

    @Test(description = "Successful user creation")
    @Description("Successful creation of a user with correct login rules")
    @Story("Create New User")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserSuccessfullyTest() {
        CreateUserPage createUserPage = new CreateUserPage(driver);
        newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";

        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
        createUserPage.goToCreateUserTab();
        Assert.assertEquals(createUserPage.getCreateUserHeader(), CREATE_NEW_USER, "Correct header");
        createUserPage.createUser(randomName, newEmail, password);
        Assert.assertEquals(createUserPage.getUserCreatedMsg(), USER_CREATED, "User is successfully created" );
    }

    @Test(description = "Successful user creation")
    @Description("Successful creation of admin user with correct login rules")
    @Story("Create New user")
    @Severity(SeverityLevel.CRITICAL)
    public void createAdminSuccessfullyTest() {
        CreateUserPage createUserPage = new CreateUserPage(driver);
        newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";

        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
        createUserPage.goToCreateUserTab();
        createUserPage.selectAdminUserRole();
        createUserPage.createUser(randomName, newEmail, password);
        Assert.assertEquals(createUserPage.getUserCreatedMsg(), USER_CREATED, "User is successfully created" );
    }

    @Test(description = "Unsuccessful user creation")
    @Description("Unsuccessful creation of a user invoking name error message")
    @Story("Create New User")
    @Severity(SeverityLevel.CRITICAL)
    public void emptyNameField() {
        String emptyName = "";
        CreateUserPage createUserPage = new CreateUserPage(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
        createUserPage.goToCreateUserTab();
        createUserPage.createUser(emptyName, newEmail, password);
        createUserPage.waitMs();

        String message = (String) js.executeScript("return arguments[0].validationMessage;", createUserPage.getNewNameUser());
        Assert.assertEquals(message, "Please fill out this field.");
    }

    @Test(description = "Unsuccessful user creation")
    @Description("Unsuccessful creation of a user invoking email address error message")
    @Story("Create New User")
    @Severity(SeverityLevel.CRITICAL)
    public void emptyEmailAddressField() {
        String emptyEmail = "";
        CreateUserPage createUserPage = new CreateUserPage(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
        createUserPage.goToCreateUserTab();
        createUserPage.createUser(randomName, emptyEmail, password);

        String message = (String) js.executeScript("return arguments[0].validationMessage;", createUserPage.getEmailAddress());
        Assert.assertEquals(message, "Please fill out this field.");
    }

    @Test(description = "Unsuccessful user creation")
    @Description("Unsuccessful creation of a user invoking email format error message for missing @ sign")
    @Story("Create New User")
    @Severity(SeverityLevel.CRITICAL)
    public void invalidEmailFormatError() {
        CreateUserPage createUserPage = new CreateUserPage(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
        createUserPage.goToCreateUserTab();

        createUserPage.createUser(randomName, invalidEmail, password);

        String message = (String) js.executeScript("return arguments[0].validationMessage;", createUserPage.getEmailAddress());
        Assert.assertEquals(message, "Please include an '@' in the email address. '" + invalidEmail + "' is missing an '@'.");
    }

    @Test(description = "Unsuccessful user creation")
    @Description("Unsuccessful creation of a user invoking name error message")
    @Story("Create New User")
    @Severity(SeverityLevel.CRITICAL)
    public void emptyPasswordField() {
        String emptyPassword = "";
        CreateUserPage createUserPage = new CreateUserPage(driver);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";


        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
        createUserPage.goToCreateUserTab();
        createUserPage.createUser(randomName, newEmail, emptyPassword);

        String message = (String) js.executeScript("return arguments[0].validationMessage;", createUserPage.getPassword());
        Assert.assertEquals(message, "Please fill out this field.");
    }

    @Test(description = "Unsuccessful user creation")
    @Description("Unsuccessful creation of a user due to invalid password format")
    @Story("Create New User")
    @Severity(SeverityLevel.CRITICAL)
    public void invalidPasswordFormatError() {
        CreateUserPage createUserPage = new CreateUserPage(driver);
        newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";

        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
        createUserPage.goToCreateUserTab();
        createUserPage.createUser(randomName, newEmail, invalidPassword);

        Assert.assertEquals(createUserPage.getCharacterPasswordRequirement(), EIGHT_CHARACTERS_REQUIREMENT);
        Assert.assertEquals(createUserPage.getAlphanumericPasswordRequirement(), ALPHANUMERIC_RREQUIREMENT);
        Assert.assertEquals(createUserPage.getDigitPasswordRequirement(), ONE_DIGIT_REQUIREMENT);
        Assert.assertEquals(createUserPage.getUppercasePasswordRequirement(), ONE_UPPERCASE_REQUIREMENT);
    }

    @Test(description = "Unsuccessful user creation")
    @Description("Unsuccessful creation of a user due to duplicate email")
    @Story("Create New User")
    @Severity(SeverityLevel.CRITICAL)
    public void duplicateEmailError() {
        CreateUserPage createUserPage = new CreateUserPage(driver);
        String duplicateEmail = "test710270045@test.com";

        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
        createUserPage.goToCreateUserTab();
        createUserPage.createUser(randomName, duplicateEmail, password);

        Assert.assertEquals(createUserPage.getDuplicateEmailRequirement(), getDuplicateEmailError(duplicateEmail));
    }

}