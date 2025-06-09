package com.photopixels.web;

import com.photopixels.base.WebBaseTest;
import com.photopixels.helpers.WaitOperationHelper;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.web.pages.*;
import io.qameta.allure.*;
import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.photopixels.constants.Constants.OVERVIEW_HEADER;
import static com.photopixels.constants.Constants.PASSWORD;

@Listeners(StatusTestListener.class)
@Feature("Web")
public class SignUpNewUserTests extends WebBaseTest {
    private String randomName;
    private String randomEmail;

    private WaitOperationHelper waitHelper;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        randomName = "User_" + RandomString.make(5);

        randomEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";

        waitHelper = new WaitOperationHelper(driver);
    }


    @Test(description = "Successful creation of a user on Sign Up and login")
    @Description("Successful creation of a user on Sign Up and login")
    @Story("Create New User on Sign Up")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserOnSignUpAndLoginSuccessfullyTest() {
        LoginPage loginPage = loadPhotoPixelsApp();

        SignUpUserPage signUpUserPage = loginPage.openSignUpUserPage();

        loginPage = signUpUserPage.signUpNewUser(randomName, randomEmail, PASSWORD);

        /* This wait is necessary because when the page loads too fast the elements don't populate */
        waitHelper.waitMs();

        OverviewPage overviewPage = loginPage.login(randomEmail, PASSWORD);

        Assert.assertEquals(overviewPage.getUserName(), randomName, "The user name is not correct");
        Assert.assertEquals(overviewPage.getOverviewHeader(), OVERVIEW_HEADER,
                "The header after login is not correct");

    }

    @Test(description = "Unsuccessful creation of a user with empty name on Sign up")
    @Description("Unsuccessful creation of a user with empty name on Sign up")
    @Story("Create New User on Sign Up")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserEmptyNameFieldSignUpTest() {

        LoginPage loginPage = loadPhotoPixelsApp();

        SignUpUserPage signUpUserPage = loginPage.openSignUpUserPage();

        signUpUserPage.fillCredentials(null, randomEmail, PASSWORD);

        Assert.assertFalse(signUpUserPage.isSignUpButtonEnabled(), "Sign Up button is enabled!");
    }
    @Test(description = "Unsuccessful creation of a user with empty email on Sign up")
    @Description("Unsuccessful creation of a user with empty email on Sign up")
    @Story("Create New User on Sign Up")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserEmptyEmailFieldSignUpTest() {

        LoginPage loginPage = loadPhotoPixelsApp();

        SignUpUserPage signUpUserPage = loginPage.openSignUpUserPage();

        signUpUserPage.fillCredentials(randomName, null, PASSWORD);

        Assert.assertFalse(signUpUserPage.isSignUpButtonEnabled(), "Sign Up button is enabled!");
    }

}
