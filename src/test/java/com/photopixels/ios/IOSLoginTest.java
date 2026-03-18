package com.photopixels.ios;

import com.photopixels.base.MobileBaseTest;
import com.photopixels.helpers.IOSDriverUtils;
import com.photopixels.listeners.StatusTestListener;
import com.photopixels.ios.pages.IOSLoginPage;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(StatusTestListener.class)
@Feature("iOS")
public class IOSLoginTest {
    private IOSDriverUtils iosDriverUtils;
    private AppiumDriver driver;
    private IOSLoginPage loginPage;

    // Test data
    private final String username = "";
    private final String password = "";

    @BeforeClass
    public void setup() {
        // Initialize IOSDriverUtils (reads ios.properties)
        iosDriverUtils = new IOSDriverUtils();

        // Start Appium service if needed
        iosDriverUtils.startService();

        // Create driver session
        driver = iosDriverUtils.getIOSDriver();

        // Initialize Page Object
        loginPage = new IOSLoginPage(driver);
    }

    @Test(description = "Standalone iOS login test")
    public void loginUserIOSTest() {
        // Perform login
        loginPage.login(username, password);
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        // Stop Appium service
        iosDriverUtils.stopService();
    }
}