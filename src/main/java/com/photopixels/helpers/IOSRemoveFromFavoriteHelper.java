package com.photopixels.helpers;

import com.photopixels.ios.pages.IOSPhotosPage;
import io.appium.java_client.AppiumDriver;

public class IOSRemoveFromFavoriteHelper {

    private final IOSPhotosPage photosPage;
    private final IOSSwippingHelper swippingImages;
    private final AppiumDriver driver;

    public IOSRemoveFromFavoriteHelper(AppiumDriver driver) {
        this.driver = driver;
        this.photosPage = new IOSPhotosPage(driver);
        this.swippingImages = new IOSSwippingHelper(driver);
    }

    public boolean findFirstFavoritePhoto(int maxSwipes) throws Exception {
        for (int i = 0; i < maxSwipes; i++) {
            // If heart.fill exists → photo is a favorite
            if (photosPage.isPhotoFavorite()) {
                System.out.println("Found photo that is favorite");
                return true;
            }
            // Otherwise swipe to next photo
            swippingImages.swipeLeft();
            Thread.sleep(500);
        }
        throw new Exception("No favorite photo found");
    }

    public void removeFirstAvailablePhotoFromFavorites(int totalNumberOfPhotos) throws Exception {
        if (photosPage.isPhotoFavorite()) {
            photosPage.removePhotoFromFavorites();
            System.out.println("Photo removed from favorites");
            return;
        }
        System.out.println("Photo is not favorite. Searching for next...");
        findFirstFavoritePhoto(totalNumberOfPhotos);
        photosPage.removePhotoFromFavorites();
    }
}
