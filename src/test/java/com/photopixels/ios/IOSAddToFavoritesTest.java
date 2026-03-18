package com.photopixels.ios;

import com.photopixels.helpers.IOSAddToFavoriteHelper;
import com.photopixels.ios.pages.IOSLoginPage;
import com.photopixels.ios.pages.IOSNavbarPage;
import com.photopixels.ios.pages.IOSPhotosPage;
import com.photopixels.listeners.StatusTestListener;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Feature;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;
import com.photopixels.helpers.IOSDriverUtils;

import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;



@Listeners(StatusTestListener.class)
@Feature("iOS")
public class IOSAddToFavoritesTest {

    private  AppiumDriver driver;
    private IOSDriverUtils iosDriverUtils;
    private  IOSLoginPage loginPage;
    private  IOSPhotosPage photosPage;
    private  IOSNavbarPage navbarPage;
    private  IOSAddToFavoriteHelper favoritesHelper;
    private  WebDriverWait wait;

    // Test data
    private final String username = "sole2@tst.com";
    private final String password = "@Mouse24";

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
        photosPage = new IOSPhotosPage(driver);
        navbarPage = new IOSNavbarPage(driver);
        favoritesHelper = new IOSAddToFavoriteHelper(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }


    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        // Stop Appium service
        iosDriverUtils.stopService();
    }

    @Test
    public void shouldAddImageToFavorites() throws Exception {

        //loginPage.login(username, password);

        // Sync Photos
        try {
            if (photosPage.getSyncPhotosButton().isDisplayed()) {
                photosPage.clickSyncPhotos();
            }
        } catch (Exception ignored) {}

        wait.until(ExpectedConditions.visibilityOf(photosPage.getPhotoByIndex(1)));

        // Go to Favorites tab and count the number of photos
        navbarPage.goToFavorites();
        int numberOfFavoritePhotos = photosPage.getPhotoCount();
        System.out.println("Number of favorite photos: " + numberOfFavoritePhotos);

        // Go to the Photos tab, open the first photo and find a photo that is not added in favorites
        navbarPage.goToPhotos();
        int totalNumberOfPhotos = photosPage.getPhotoCount();
        photosPage.openPhoto(1);
        favoritesHelper.addFirstAvailablePhotoToFavorites(totalNumberOfPhotos);
        photosPage.clickBackButton();

        // Go to Favorites tab and count the number of photos again
        navbarPage.goToFavorites();
        int newNumberOfFavoritePhotos = photosPage.getPhotoCount();
        System.out.println("Number of favorite photos after adding one: " + newNumberOfFavoritePhotos);

        assertEquals(newNumberOfFavoritePhotos, numberOfFavoritePhotos + 1,
                "Favorites count should have increased by 1");


    }
}