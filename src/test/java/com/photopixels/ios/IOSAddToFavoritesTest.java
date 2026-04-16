package com.photopixels.ios;

import com.photopixels.helpers.IOSAddToFavoriteHelper;
import com.photopixels.helpers.IOSRemoveFromFavoriteHelper;
import com.photopixels.ios.pages.IOSLoginPage;
import com.photopixels.ios.pages.IOSNavbarPage;
import com.photopixels.ios.pages.IOSPhotosPage;
import com.photopixels.ios.pages.IOSSettingsPage;
import com.photopixels.listeners.StatusTestListener;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Feature;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Listeners;
import com.photopixels.helpers.IOSDriverUtils;

import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.*;


@Listeners(StatusTestListener.class)
@Feature("iOS")
public class IOSAddToFavoritesTest {

    private  AppiumDriver driver;
    private IOSDriverUtils iosDriverUtils;
    private  IOSLoginPage loginPage;
    private  IOSPhotosPage photosPage;
    private  IOSNavbarPage navbarPage;
    private IOSSettingsPage settingsPage;
    private  IOSAddToFavoriteHelper addToFavoriteHelper;
    private IOSRemoveFromFavoriteHelper  removeFromFavoriteHelper;
    private  WebDriverWait wait;

    // Test data
    private final String username = "sole2@tst.com";
    private final String password = "@Mouse24";
    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(10);


    @BeforeClass
    public void setup() throws InterruptedException {
        // Initialize IOSDriverUtils (reads ios.properties)
        iosDriverUtils = new IOSDriverUtils();

        // Start Appium service if needed
        iosDriverUtils.startService();

        // Create driver session
        driver = iosDriverUtils.getIOSDriver();
        initializePages();

        System.out.println("Pages initialized");
        // Check if already logged in or on login page
        if (navbarPage.isLoggedIn()) {
            System.out.println("Already logged in, logging out first...");
            navbarPage.goToSettings();
            settingsPage.logout();
        }

        System.out.println("Proceeding to login...");
        loginPage.login(username, password);
        synchronizePhotosIfNeeded();

    }

    private void initializePages() {
        loginPage = new IOSLoginPage(driver);
        photosPage = new IOSPhotosPage(driver);
        navbarPage = new IOSNavbarPage(driver);
        settingsPage = new IOSSettingsPage(driver);
        addToFavoriteHelper = new IOSAddToFavoriteHelper(driver);
        removeFromFavoriteHelper = new IOSRemoveFromFavoriteHelper(driver);
    }


    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        // Stop Appium service
        iosDriverUtils.stopService();
    }

    // Helper method to avoid duplication
    private void synchronizePhotosIfNeeded() {
        try {
            var syncButton = photosPage.getSyncPhotosButton();
            if (syncButton != null && syncButton.isDisplayed()) {
                photosPage.clickSyncPhotos();
                wait.until(ExpectedConditions.visibilityOf(photosPage.getPhotoByIndex(1)));
            }
        } catch (Exception e) {
            // Sync button not present - photos already synced
        }
    }

    @Test
    public void shouldAddImageToFavorites() throws Exception {

        // Go to Favorites tab and count the number of photos
        navbarPage.goToFavorites();

        int initialFavoriteCount = photosPage.getPhotoCount();
        System.out.println("Number of favorite photos: " + initialFavoriteCount);

        // Go to the Photos tab, open the first photo and find a photo that is not added in favorites
        navbarPage.goToPhotos();
        photosPage.openPhoto(1);
        addToFavoriteHelper.addFirstAvailablePhotoToFavorites(photosPage.getPhotoCount());
        photosPage.clickBackButton();

        // Go to Favorites tab and count the number of photos again
        navbarPage.goToFavorites();
        int finalFavoriteCount = photosPage.getPhotoCount();
        System.out.println("Number of favorite photos after adding one: " + finalFavoriteCount);

        assertEquals(finalFavoriteCount, initialFavoriteCount + 1,
                "Favorites count should have increased by 1");

    }

    @Test
    public void shouldRemoveImageFromFavorites() throws Exception {
        navbarPage.goToPhotos();

        //Open the first photo and find a photo that is not added in favorites
        photosPage.openPhoto(1);
        addToFavoriteHelper.addFirstAvailablePhotoToFavorites(photosPage.getPhotoCount());
        photosPage.clickBackButton();

        // Go to Favorites tab and count the number of photos
        navbarPage.goToFavorites();
        int initialFavoriteCount = photosPage.getPhotoCount();
        System.out.println("Number of favorite photos after adding one: " + initialFavoriteCount);

        //Go to Photos tab and remove a photo from favorites
        navbarPage.goToPhotos();
        photosPage.openPhoto(1);
        removeFromFavoriteHelper.removeFirstAvailablePhotoFromFavorites(initialFavoriteCount);
       //Thread.sleep(2000);
        photosPage.clickBackButton();

        // Go to Favorites tab and count the number of photos
        navbarPage.goToFavorites();
        int newNumberOfFavoritePhotos = photosPage.getPhotoCount();
        System.out.println("Number of favorite photos after removing one: " + newNumberOfFavoritePhotos);
        assertEquals(newNumberOfFavoritePhotos, initialFavoriteCount - 1,
                "Favorites count should have decreased by 1");

    }
    @Test
    public void favoritedPhotoShouldShowHeartIconInGrid() {
        navbarPage.goToFavorites();
        assertTrue(photosPage.isHeartIconVisibleOnPhotoInGrid(0),
                "Heart icon should be visible on favorited photo in grid");
    }
    @Test
    public void favoritesShouldBeGroupedByDate() {
        navbarPage.goToFavorites();
        List<WebElement> dateHeaders = photosPage.getDateSectionHeaders();
        assertFalse(dateHeaders.isEmpty(), "Favorites should show date section headers");
        // Optionally assert ordering is descending (most recent first)
    }
    @Test
    public void openingFavoritePhotoShouldShowActionButtons() throws InterruptedException {
        navbarPage.goToFavorites();
        photosPage.openPhoto(1);
        assertTrue(photosPage.isShareButtonVisible(), "Share button should be visible");
        assertTrue(photosPage.isLoveButtonVisible(), "Love/heart button should be visible");
        assertTrue(photosPage.isDeleteButtonVisible(), "Bin button should be visible");
        photosPage.clickBackButton();
    }
    @Test
    public void backFromOpenPhotoShouldReturnToFavorites() {
        navbarPage.goToFavorites();
        photosPage.openPhoto(1);
        photosPage.clickBackButton();
        assertTrue(photosPage.isFavoritesHeaderVisible(),
                "Should return to Favorites screen after pressing Back");
    }
}