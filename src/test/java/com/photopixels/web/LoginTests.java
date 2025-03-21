package com.photopixels.web;

import com.photopixels.base.WebBaseTest;
import com.photopixels.helpers.listeners.StatusTestListener;
import com.photopixels.web.pages.LoginPage;
import com.photopixels.web.pages.OverviewPage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(StatusTestListener.class)
@Feature("Web")
public class LoginTests extends WebBaseTest {

    private static final String OVERVIEW_HEADER = "Overview";

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

}
