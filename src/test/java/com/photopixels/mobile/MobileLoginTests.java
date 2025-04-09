package com.photopixels.mobile;

import com.photopixels.base.MobileBaseTest;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.mobile.pages.HomePage;
import com.photopixels.mobile.pages.MobileLoginPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

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
}
