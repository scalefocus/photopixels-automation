package com.photopixels.mobile;

import com.photopixels.base.MobileBaseTest;
import com.photopixels.helpers.DriverUtils;
import com.photopixels.mobile.pages.HomePage;
import com.photopixels.mobile.pages.MobileLoginPage;
import com.photopixels.mobile.pages.SettingsPage;
import com.photopixels.web.pages.LoginPage;
import com.photopixels.web.pages.OverviewPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.Constants.FRENCH_FRIES_FILE;
import static com.photopixels.constants.Constants.MEDIA_ADDED_TO_FAVORITES;

public class MobileAutoSyncTests extends MobileBaseTest {

    private String email;
    private String password;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        email = inputData.getUsername();
        password = inputData.getPassword();
        try {
            pushImageToGallery(FRENCH_FRIES_FILE, "french-fries.jpg");
        } catch (Exception e) {
            System.out.println("Image could not be pushed to device!" + e);
        }
    }

    private void checkImageIsUploadOnWeb() {
        WebDriver driver = null;

        // Sleep is needed so the image is uploaded
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        try {
            driver = new DriverUtils().getDriver();

            driver.get(configProperties.getProperty("webUrl"));

            LoginPage loginPage = new LoginPage(driver);
            OverviewPage overviewPage = loginPage.login(email, password);
            overviewPage.selectMedia(0);

            overviewPage.addToFavoritesMedia();

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(overviewPage.getFavoriteMediaMessage(), MEDIA_ADDED_TO_FAVORITES,
                    "The message is not correct.");

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test(description = "Auto Sync photos only through Wi-Fi")
    @Description("Auto Sync photos only through Wi-Fi")
    @Story("Auto Sync Through Mobile Data")
    @Severity(SeverityLevel.CRITICAL)
    public void autoSyncThroughWiFi() throws InterruptedException {
        ((AndroidDriver) mobileDriver).setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        HomePage homePage = loginPage.login(email, password);
        homePage.navigateToSettings();
        SettingsPage settingsPage = new SettingsPage(mobileDriver);
        settingsPage.turnRequireWiFiForSyncOnOff(true);
        homePage.navigateToHome();
        homePage.clickSyncMediaButton();
        homePage.clickAllowNotificationsButton();
        homePage.clickAllowAllPhotosAccessButton();
        homePage.waitForUploadToFinish(mobileDriver);
        checkImageIsUploadOnWeb();
    }

}
