package com.photopixels.ios;

import com.photopixels.helpers.IOSAddToFavoriteHelper;
import com.photopixels.helpers.IOSDriverUtils;
import com.photopixels.helpers.IOSRemoveFromFavoriteHelper;
import com.photopixels.ios.pages.IOSLoginPage;
import com.photopixels.ios.pages.IOSNavbarPage;
import com.photopixels.ios.pages.IOSPhotosPage;
import com.photopixels.listeners.StatusTestListener;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Feature;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;


@Listeners(StatusTestListener.class)
@Feature("iOS")
public class IOSTrashTest {

    private  AppiumDriver driver;
    private IOSDriverUtils iosDriverUtils;
    private  IOSLoginPage loginPage;
    private  IOSPhotosPage photosPage;
    private  IOSNavbarPage navbarPage;
    private  IOSAddToFavoriteHelper addToFavoriteHelper;
    private IOSRemoveFromFavoriteHelper  removeFromFavoriteHelper;
    private  WebDriverWait wait;

    // Test data
    private final String username = "sole2@tst.com";
    private final String password = "@Mouse24";
    private static final Duration DEFAULT_WAIT_TIMEOUT = Duration.ofSeconds(10);


    @BeforeClass
    public void setup() {
        // Initialize IOSDriverUtils (reads ios.properties)
        iosDriverUtils = new IOSDriverUtils();

        // Start Appium service if needed
        iosDriverUtils.startService();

        // Create driver session
        driver = iosDriverUtils.getIOSDriver();
        wait = new WebDriverWait(driver, DEFAULT_WAIT_TIMEOUT);
        initializePages();

    }
    private void initializePages() {
        loginPage = new IOSLoginPage(driver);
        photosPage = new IOSPhotosPage(driver);
        navbarPage = new IOSNavbarPage(driver);
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
    // Common helper to move photo to trash
    private void movePhotoToTrash(int photoIndex) {
        photosPage.openPhoto(photoIndex);
        photosPage.deletePhoto();
        photosPage.confirmDelete();
        photosPage.clickBackButton();
    }

    // Common helper to restore photo from trash
    private void restorePhotoFromTrash(int photoIndex) {
        photosPage.openPhoto2(photoIndex);
        photosPage.restorePhoto();
        photosPage.clickBackButton();
    }
    @Test
    public void shouldAddImageToTrash() throws Exception {

        //loginPage.login(username, password);

        synchronizePhotosIfNeeded();
        int initialPhotosCount = photosPage.getPhotoCount();
        System.out.println("Number of photos: " + initialPhotosCount);

        // Go to Trash tab and count the number of photos
        navbarPage.goToTrash();
        int initialTrashCount = photosPage.getPhotoCount();
        System.out.println("Number of photos in trash: " + initialTrashCount);

        // Go to the Photos tab, open the first photo and delete it
        navbarPage.goToPhotos();
        movePhotoToTrash(1);


        int finalPhotosCount = photosPage.getPhotoCount();
        System.out.println("Number of photos: " + finalPhotosCount);
        assertEquals(finalPhotosCount, initialPhotosCount - 1,
                "Photos count should have decreased by 1");


        // Go to Trash tab and count the number of photos again
        navbarPage.goToTrash();
        int finalTrashCount = photosPage.getPhotoCount();
        System.out.println("Number of photos in trash: " + finalTrashCount);

        assertEquals(finalTrashCount, initialTrashCount + 1,
                "Trash count should have increased by 1");

    }

    @Test
    public void shouldRestoreImageToTrash() throws Exception {

        //loginPage.login(username, password);

        synchronizePhotosIfNeeded();


        // Go to the Photos tab, open the first photo and delete it
        navbarPage.goToPhotos();
        movePhotoToTrash(1);

        int initialPhotosCount = photosPage.getPhotoCount();
        System.out.println("Number of photos: " + initialPhotosCount);

        // Go to Trash tab and count the number of photos
        navbarPage.goToTrash();
        int initialTrashCount = photosPage.getPhotoCount();
        System.out.println("Number of photos in trash: " + initialTrashCount);

        restorePhotoFromTrash(1);
        int finalTrashCount = photosPage.getPhotoCount();
        System.out.println("Number of photos in trash: " + finalTrashCount);

        assertEquals(finalTrashCount, initialTrashCount - 1,
                "Trash count should have decreased by 1");

        navbarPage.goToPhotos();
        int finalPhotosCount = photosPage.getPhotoCount();
        System.out.println("Number of photos: " + finalPhotosCount);
        assertEquals(finalPhotosCount, initialPhotosCount + 1,
                "Photos count should have increased by 1");

    }

}