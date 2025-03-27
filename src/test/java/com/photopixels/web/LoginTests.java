package com.photopixels.web;

import com.photopixels.base.WebBaseTest;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.web.pages.LoginPage;
import com.photopixels.web.pages.OverviewPage;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.photopixels.constants.Constants.OVERVIEW_HEADER;
import static com.photopixels.constants.Constants.WRONG_CREDENTIALS_MESSAGE;

@Listeners(StatusTestListener.class)
@Feature("Web")
public class LoginTests extends WebBaseTest {

    private String name;
    private String email;
    private String password;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        name = inputData.getUserFullName();
        email = inputData.getUsername();
        password = inputData.getPassword();
    }

    @Test(description = "Successful login via web")
    @Description("Successful login via web with valid credentials")
    @Story("Login User")
    @Severity(SeverityLevel.CRITICAL)
    public void loginUserSuccessfullyTest() {
        LoginPage loginPage = loadPhotoPixelsApp();
        OverviewPage overviewPage = loginPage.login(email, password);

        Assert.assertEquals(overviewPage.getUserName(), name, "The user name is not correct");
        Assert.assertEquals(overviewPage.getOverviewHeader(), OVERVIEW_HEADER,
                "The header after login is not correct");
    }

    @Test(description = "Login wrong password via web")
    @Description("Login via web with existing user and wrong password")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginWrongPasswordWebTest() {
        String password = "12345!Test";

        LoginPage loginPage = loadPhotoPixelsApp();
        loginPage.login(email, password);

        Assert.assertEquals(loginPage.getErrorMessage(), WRONG_CREDENTIALS_MESSAGE,
                "Error message is not displayed!");
    }

    @Test(description = "Login via web missing email")
    @Description("Login via web with missing email")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginMissingEmailTest() {
        LoginPage loginPage = loadPhotoPixelsApp();
        loginPage.fillCredentials(null, password);

        Assert.assertFalse(loginPage.isLoginButtonEnabled(), "Login button is enabled!");
    }

    @Test(description = "Login via web missing password")
    @Description("Login via web with missing password")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginMissingPasswordTest() {
        LoginPage loginPage = loadPhotoPixelsApp();
        loginPage.fillCredentials(email, null);

        Assert.assertFalse(loginPage.isLoginButtonEnabled(), "Login button is enabled!");
    }

    @Test(description = "Login via web not registered user")
    @Description("Login via web with not registered")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginNotRegisteredUserWebTest() {
        String email = "test" + RandomStringUtils.randomNumeric(6) + "@test.com";

        LoginPage loginPage = loadPhotoPixelsApp();
        loginPage.login(email, password);

        // TODO: Bug? - Check if more descriptive message should be returned in this case
        Assert.assertEquals(loginPage.getErrorMessage(), WRONG_CREDENTIALS_MESSAGE,
                "Error message is not displayed!");
    }

}
