package com.photopixels.mobile;

import com.photopixels.base.MobileBaseTest;
import com.photopixels.helpers.DriverUtils;
import com.photopixels.mobile.pages.HomePage;
import com.photopixels.mobile.pages.MobileLoginPage;
import com.photopixels.mobile.pages.SettingsPage;
import com.photopixels.web.pages.LoginPage;
import com.photopixels.web.pages.OverviewPage;
import com.photopixels.web.pages.TrashPage;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

            // Remove media
            overviewPage.selectMedia(0);
            overviewPage.deleteMedia();
            TrashPage trashPage = overviewPage.goToTrashTab();
            trashPage.selectMedia(0);
            trashPage.deleteMediaPermanently();

        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    @Test(description = "Auto Sync photos only through Wi-Fi")
    @Description("Auto Sync photos only through Wi-Fi")
    @Story("Auto Sync")
    @Severity(SeverityLevel.CRITICAL)
    public void autoSyncThroughWiFiTest() {
        ((AndroidDriver) mobileDriver).setConnection(new ConnectionStateBuilder().withWiFiEnabled().withDataEnabled().build());
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        HomePage homePage = loginPage.login(email, password);
        SettingsPage settingsPage = homePage.navigateToSettings();
        settingsPage.turnRequireWiFiForSyncOnOff(true);
        homePage.clickHomeButton();
        homePage.clickSyncMediaButton();
        homePage.clickAllowNotificationsButton();
        homePage.clickAllowAllPhotosAccessButton();
        homePage.waitForUploadToFinish();
        checkImageIsUploadOnWeb();
    }

    @Test(description = "Auto Sync photos only through Mobile Data Only")
    @Description("Auto Sync photos only through Mobile Data Only")
    @Story("Auto Sync")
    @Severity(SeverityLevel.CRITICAL)
    public void autoSyncThroughMobileDataTest() {
        ((AndroidDriver) mobileDriver).setConnection(new ConnectionStateBuilder().withWiFiDisabled().withDataEnabled().build());
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        HomePage homePage = loginPage.login(email, password);
        SettingsPage settingsPage = homePage.navigateToSettings();
        settingsPage.turnRequireWiFiForSyncOnOff(false);
        homePage.clickHomeButton();
        homePage.clickSyncMediaButton();
        homePage.clickAllowNotificationsButton();
        homePage.clickAllowAllPhotosAccessButton();
        homePage.waitForUploadToFinish();
        checkImageIsUploadOnWeb();
    }
}
