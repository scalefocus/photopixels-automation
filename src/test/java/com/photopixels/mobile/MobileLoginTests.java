package com.photopixels.mobile;

import com.photopixels.base.MobileBaseTest;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.mobile.pages.ErrorPopUpPage;
import com.photopixels.mobile.pages.HomePage;
import com.photopixels.mobile.pages.MobileLoginPage;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.photopixels.constants.Constants.LOGIN_ERROR_TEXT;
import static com.photopixels.constants.Constants.LOGIN_ERROR_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Mobile")
public class MobileLoginTests extends MobileBaseTest {

    private String email;
    private String password;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        email = inputData.getUsername();
        password = inputData.getPassword();
    }

    @Test(description = "Successful login in mobile app")
    @Description("Successful login in mobile app with valid credentials")
    @Story("Login User")
    @Severity(SeverityLevel.CRITICAL)
    public void loginUserMobileTest() {
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        HomePage homePage = loginPage.login(email, password);

        Assert.assertTrue(homePage.isHomeButtonDisplayed(), "Home button is not displayed");
        Assert.assertTrue(homePage.isSettingsButtonDisplayed(), "Settings button is not displayed");
    }

    @Test(description = "Login wrong password in mobile app")
    @Description("Login in mobile app with existing user and wrong password")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginWrongPasswordMobileTest() {
        String password = "12345!Test";

        MobileLoginPage loginPage = loadPhotoPixelsApp();
        ErrorPopUpPage errorPopUpPage = loginPage.loginWithError(email, password);

        Assert.assertEquals(errorPopUpPage.getErrorTitle(), LOGIN_ERROR_TITLE,
                "Error title is not correct!");
        Assert.assertEquals(errorPopUpPage.getErrorText(), LOGIN_ERROR_TEXT,
                "Error text is not correct!");
    }

    @Test(description = "Login in mobile app missing email")
    @Description("Login in mobile app with missing email")
    @Story("Login User")
    @Severity(SeverityLevel.MINOR)
    public void loginMissingEmailMobileTest() {
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        HomePage homePage = loginPage.login(null, password);

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button is not displayed!");
    }

    @Test(description = "Login in mobile app missing password")
    @Description("Login in mobile app with missing password")
    @Story("Login User")
    @Severity(SeverityLevel.MINOR)
    public void loginMissingPasswordMobileTest() {
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        loginPage.fillCredentialsMobile(email, null);

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button is not displayed!");
    }

    @Test(description = "Login in mobile app not registered user")
    @Description("Login in mobile app with not registered")
    @Story("Login User")
    @Severity(SeverityLevel.NORMAL)
    public void loginNotRegisteredUserMobileTest() {
        String email = "test" + RandomStringUtils.randomNumeric(6) + "@test.com";

        MobileLoginPage loginPage = loadPhotoPixelsApp();
        ErrorPopUpPage errorPopUpPage = loginPage.loginWithError(email, password);

        Assert.assertEquals(errorPopUpPage.getErrorTitle(), LOGIN_ERROR_TITLE,
                "Error title is not correct!");
        Assert.assertEquals(errorPopUpPage.getErrorText(), LOGIN_ERROR_TEXT,
                "Error text is not correct!");
    }

}
