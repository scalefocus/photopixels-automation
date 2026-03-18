package com.photopixels.helpers;

import com.photopixels.helpers.IOSSwippingHelper;
import com.photopixels.ios.pages.IOSPhotosPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

import java.time.Duration;

public class IOSAddToFavoriteHelper {

    private final IOSPhotosPage photosPage;
    private final IOSSwippingHelper swippingImages;
    private final AppiumDriver driver;

    public IOSAddToFavoriteHelper(AppiumDriver driver) {
        this.driver = driver;
        this.photosPage = new IOSPhotosPage(driver);
        this.swippingImages = new IOSSwippingHelper(driver);
    }

    public boolean findFirstNonFavoritePhoto(int maxSwipes) throws Exception {
        for (int i = 0; i < maxSwipes; i++) {
            // If heart (empty) exists → photo can be added
            if (photosPage.canBeAddedToFavorites()) {
                System.out.println("Found photo that is not favorite");
                return true;
            }
            // Otherwise swipe to next photo
            swippingImages.swipeLeft();
            Thread.sleep(500);
        }
        throw new Exception("No non-favorite photo found");
    }

    public boolean findFirstNonFavoritePhoto() throws Exception {
        return findFirstNonFavoritePhoto(10); // default maxSwipes = 10
    }

    public void addFirstAvailablePhotoToFavorites(int totalNumberOfPhotos) throws Exception {
        if (photosPage.canBeAddedToFavorites()) {
            photosPage.addPhotoToFavorites();
            System.out.println("Photo added to favorites");
            return;
        }
        System.out.println("Photo already favorite. Searching for next...");
        findFirstNonFavoritePhoto(totalNumberOfPhotos);
        photosPage.addPhotoToFavorites();
    }

    public void addFirstAvailablePhotoToFavorites() throws Exception {
        addFirstAvailablePhotoToFavorites(10); // default totalNumberOfPhotos = 10
    }
}