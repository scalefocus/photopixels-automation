package com.photopixels.mobile;

import com.photopixels.base.IApiBaseTest;
import com.photopixels.base.MobileBaseTest;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.mobile.pages.MobileLoginPage;
import com.photopixels.mobile.pages.RegistrationPage;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static com.photopixels.constants.Constants.PASSWORD;
import static com.photopixels.constants.Constants.REGISTRATION_SUCCESSFUL;

@Listeners(StatusTestListener.class)
@Feature("Mobile")
public class MobileRegisterNewUserTests extends MobileBaseTest implements IApiBaseTest {

    String random = RandomStringUtils.randomAlphabetic(6);
    private String randomValidName;
    private String randomValidEmail;
    private List<String> registeredUsers;

    @BeforeClass(alwaysRun = true)
    public void setup() {
    randomValidName = "TestUser" + random;
    randomValidEmail = "testUserBox" + random + "@test.com";
    registeredUsers = new ArrayList<>();
    registeredUsers.add(randomValidEmail);
    }

    @AfterClass
    public void deleteUsers() {
        IApiBaseTest.super.deleteRegisteredUsersAdmin(registeredUsers);
    }

    @Test(description = "Successful new user registration")
    @Description("Successful registration of a new user in mobile app")
    @Story("Register User")
    @Severity(SeverityLevel.CRITICAL)
    public void registerNewUserMobileTest() {
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        loginPage = registrationPage.registerNewUser(randomValidName, randomValidEmail, PASSWORD, PASSWORD);

        Assert.assertTrue(loginPage.isToastNotificationDisplayed(REGISTRATION_SUCCESSFUL), "Successful registration toast notification is missing");
        //Assert that user is on login page and username is prefilled with proper newly created user data
        //NOTE: Email is used for Username
        Assert.assertEquals(loginPage.getUsernameFieldValue(), randomValidEmail, "Username prefilled value is not correct");
    }


    @Test(description = "User can register with valid alphanumeric name")
    @Description("Successful registration of a new user with name which includes letters and digits")
    @Story("Register User")
    @Severity(SeverityLevel.NORMAL)
    public void registerNewUserWithAlphanumericNameMobileTest() {
        String randomValidAlphanumericName = randomValidName + "1";
        randomValidEmail =  "testUserBox" + randomValidAlphanumericName + "@test.com";
        registeredUsers.add(randomValidEmail);

        // TODO: Remove when issue is fixed
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/183");

        MobileLoginPage loginPage = loadPhotoPixelsApp();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        registrationPage.registerNewUser(randomValidAlphanumericName, randomValidEmail, PASSWORD, PASSWORD);

        Assert.assertFalse(registrationPage.isNameFieldErrorMessageVisible(), "Name field validation fails");
    }

    @DataProvider(name = "singleEmptyField")
    public Object[][] getSingleEmptyField() {
        return new Object [][]  {
                { "", randomValidEmail, PASSWORD, PASSWORD},
                {randomValidName, "", PASSWORD, PASSWORD},
                {randomValidName, randomValidEmail, "", PASSWORD},
                {randomValidName, randomValidEmail, PASSWORD, ""}
        };
    }
    @Test(description = "Register button is disabled when a required field is empty", dataProvider = "singleEmptyField")
    @Description("User cannot use Register button if a required field is not filled in")
    @Story("Register User")
    @Severity(SeverityLevel.NORMAL)
    public void registerButtonDisabledWhenFieldIsEmptyMobileTest(String name, String email, String password, String confirmPassword) {

        MobileLoginPage loginPage = loadPhotoPixelsApp();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        registrationPage.fillInRegistrationFields(name, email, password, confirmPassword);

        Assert.assertFalse(registrationPage.isRegisterButtonEnabled(), "Register button is enabled");
    }

    @DataProvider(name = "invalidPassword")
    public Object[][] getInvalidPassword() {
        return new Object [][]  {
                { "password1!"}, //missing capital letter
                {"Password1"}, //missing special symbol
                {"Password!"} // missing digit
        };
    }
    @Test(description = "Registration not allowed with invalid password", dataProvider = "invalidPassword")
    @Description("User cannot register a new user with invalid password - it should be at least 8 characters, one lowercase, one uppercase, and one non alphanumeric character")
    @Story("Register User")
    @Severity(SeverityLevel.NORMAL)
    public void registerWithInvalidPasswordNotAllowedMobileTest(String password) {

        // TODO: Remove when issue is fixed
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/191");

        MobileLoginPage loginPage = loadPhotoPixelsApp();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        registrationPage.registerNewUser(randomValidName, randomValidEmail, password, password);

        Assert.assertTrue(registrationPage.isPasswordFieldErrorMessageVisible(), "Password field validation fails, app accepts invalid password: " + password);
    }

}
