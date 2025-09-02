package com.photopixels.mobile;

import com.photopixels.base.MobileBaseTest;
import com.photopixels.constants.Constants;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.mobile.pages.MobileLoginPage;
import com.photopixels.mobile.pages.RegistrationPage;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.photopixels.constants.Constants.REGISTRATION_SUCCESSFUL;

@Listeners(StatusTestListener.class)
@Feature("Mobile")
public class MobileRegisterNewUserTests extends MobileBaseTest {

    String random = RandomStringUtils.randomAlphabetic(6);
    String randomValidName = "TestUser" + random;
    String randomValidEmail = "testUserBox" + random + "@test.com";
    String password = Constants.PASSWORD;

    @Test(description = "Successful new user registration")
    @Description("Successful registration of a new user in mobile app")
    @Story("Register User")
    @Severity(SeverityLevel.CRITICAL)
    public void registerNewUserMobileTest() {
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        loginPage = registrationPage.registerNewUser(randomValidName, randomValidEmail, password, password);

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
        String random = RandomStringUtils.randomAlphanumeric(6);
        String randomValidAlphanumericName = "TestUser" + random;
        String randomValidEmail = "testUserBox" + random + "@test.com";
        String password = Constants.PASSWORD;

        // TODO: Remove when issue is fixed
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/183");

        MobileLoginPage loginPage = loadPhotoPixelsApp();
        RegistrationPage registrationPage = loginPage.clickRegistrationButton();
        registrationPage.registerNewUser(randomValidAlphanumericName, randomValidEmail, password, password);

        Assert.assertFalse(registrationPage.isNameFieldErrorMessageVisible(), "Name field validation fails");
    }

    @DataProvider(name = "singleEmptyField")
    public Object[][] getSingleEmptyField() {
        return new Object [][]  {
                { "", randomValidEmail, password, password},
                {randomValidName, "", password, password},
                {randomValidName, randomValidEmail, "", password},
                {randomValidName, randomValidEmail, password, ""}
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

}
