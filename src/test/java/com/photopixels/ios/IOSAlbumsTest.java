package com.photopixels.ios;

import com.photopixels.ios.pages.IOSAlbumPage;
import com.photopixels.helpers.IOSDriverUtils;
import com.photopixels.ios.pages.IOSLoginPage;
import com.photopixels.ios.pages.IOSNavbarPage;
import com.photopixels.ios.pages.IOSSettingsPage;
import com.photopixels.listeners.StatusTestListener;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Feature;
import net.bytebuddy.utility.RandomString;
import org.testng.annotations.*;

import static org.testng.Assert.*;

@Listeners(StatusTestListener.class)
@Feature("iOS - Albums")
public class IOSAlbumsTest {

    private AppiumDriver driver;
    private IOSDriverUtils iosDriverUtils;
    private IOSAlbumPage albumPage;
    private IOSNavbarPage navbarPage;
    private IOSLoginPage loginPage;
    private IOSSettingsPage settingsPage;

    // Test data
    private final String username = "sole2@tst.com";
    private final String password = "@Mouse24";

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

    }

    private void initializePages() {
        albumPage = new IOSAlbumPage(driver);
        navbarPage = new IOSNavbarPage(driver);
        loginPage = new IOSLoginPage(driver);
        settingsPage = new IOSSettingsPage(driver);
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
    public void shouldDisplayAddNewAlbumPlaceholder() {

        // Click on Albums tab
        navbarPage.goToAlbums();

        // Assert "Add new album" button is visible
        assertTrue(albumPage.isAddNewAlbumButtonVisible(),
                "'Add new album' button should be visible");
        // Assert "+" icon is visible
        assertTrue(albumPage.isAddAlbumIconVisible(),
                "Plus icon should be visible");
    }

    @Test
    public void shouldCreateNewAlbum() {
        String albumName = RandomString.make();

        navbarPage.goToAlbums();

        int initialCount = albumPage.getAlbumCount();
        System.out.println("The initial album count is " + initialCount);

        albumPage.clickAddNewAlbum();
        albumPage.enterAlbumName(albumName);
        albumPage.createAlbum();
        albumPage.waitForAlbumVisible(albumName);
        int finalCount = albumPage.getAlbumCount();
        System.out.println("The final album count is " + finalCount);
        assertEquals(finalCount, initialCount+1, "The number of albums has increased");
        albumPage.clickAlbumByName(albumName);
        albumPage.deleteAlbumAction();
    }


    @Test
    public void newAlbumEmptyState() {
        String albumName = RandomString.make();

        navbarPage.goToAlbums();
        albumPage.clickAddNewAlbum();
        albumPage.enterAlbumName(albumName);
        albumPage.createAlbum();
        albumPage.waitForAlbumVisible(albumName);
        albumPage.clickAlbumByName(albumName);
        assertTrue(albumPage.isNoPhotosIndicatorVisible(), "There are no photos initially on album creation");
        albumPage.deleteAlbumAction();


    }

    @Test
    public void addItemsInTheAlbumAddPhotosButton() {
        String albumName = RandomString.make();

        navbarPage.goToAlbums();
        albumPage.clickAddNewAlbum();
        albumPage.enterAlbumName(albumName);
        albumPage.createAlbum();
        albumPage.waitForAlbumVisible(albumName);
        albumPage.clickAlbumByName(albumName);
        albumPage.addPhotos();
        albumPage.selectPhoto(0);
        albumPage.addSelectedPhotos();
        int imageCount = albumPage.getAlbumImageCount();
        System.out.println("Number of images " + imageCount);
        albumPage.deleteAlbumAction();
        assertEquals(imageCount, 1);


    }

    @Test
    public void addMultipleItemInTheAlbum() {
        String albumName = RandomString.make();

        navbarPage.goToAlbums();
        albumPage.clickAddNewAlbum();
        albumPage.enterAlbumName(albumName);
        albumPage.createAlbum();
        albumPage.waitForAlbumVisible(albumName);
        albumPage.clickAlbumByName(albumName);
        albumPage.addPhotos();
        for(int i=0;i<5;i++){
            albumPage.selectPhoto(i);
        }
        albumPage.addSelectedPhotos();
        int imageCount = albumPage.getAlbumImageCount();
        System.out.println("Number of images " + imageCount);
        albumPage.deleteAlbumAction();
        assertEquals(imageCount, 5);


    }
    @Test
    public void addItemsInTheAlbumAddItemsButton() {
        String albumName = RandomString.make();

        navbarPage.goToAlbums();
        albumPage.clickAddNewAlbum();
        albumPage.enterAlbumName(albumName);
        albumPage.createAlbum();
        albumPage.waitForAlbumVisible(albumName);
        albumPage.clickAlbumByName(albumName);
        albumPage.openOptions();
        albumPage.addItems();
        albumPage.selectPhoto(0);
        albumPage.addSelectedPhotos();
        int imageCount = albumPage.getAlbumImageCount();
        System.out.println("Number of images " + imageCount);
        albumPage.deleteAlbumAction();
        assertEquals(imageCount, 1);

    }

    @Test
    public void cancelAlbumCreation () {
        String albumName = RandomString.make();

        navbarPage.goToAlbums();

        int initialCount = albumPage.getAlbumCount();
        System.out.println("The initial album count is " + initialCount);

        albumPage.clickAddNewAlbum();
        albumPage.enterAlbumName(albumName);
        albumPage.cancelAlbumCreation();

        int finalCount = albumPage.getAlbumCount();
        assertEquals(initialCount, finalCount, "No new album was created.");
    }
    @Test
    public void cancelAddingImageIntoFolder () {
        String albumName = RandomString.make();
        System.out.println(albumName);
        System.out.println(driver.getPageSource());
        navbarPage.goToAlbums();
        albumPage.clickAddNewAlbum();
        albumPage.enterAlbumName(albumName);
        albumPage.createAlbum();
        albumPage.waitForAlbumVisible(albumName);
        albumPage.clickAlbumByName(albumName);
        albumPage.addPhotos();
        albumPage.selectPhoto(0);
        albumPage.cancelSelectedPhotos();
        assertTrue(albumPage.isNoPhotosIndicatorVisible());
        albumPage.deleteAlbumAction();

    }
  }