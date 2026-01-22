    package com.photopixels.web;

    import com.photopixels.base.WebBaseTest;
    import com.photopixels.listeners.StatusTestListener;
    import com.photopixels.web.pages.*;
    import io.qameta.allure.*;
    import org.testng.Assert;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Listeners;
    import org.testng.annotations.Test;

    import static com.photopixels.constants.Constants.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class AlbumsTests extends WebBaseTest {

        private String username;
        private String password;

        @BeforeClass(alwaysRun = true)
        public void setup() throws Exception {
            username = inputData.getUsername();
            password = inputData.getPassword();
        }

        @Test(description = "Successful add selected media to Favorites album")
        @Description("Successful add selected media to Favorites album")
        @Story("Favorites")
        @Severity(SeverityLevel.NORMAL)
        public void addMediaToFavoritesTest() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);
            overviewPage.uploadMedia(TRAINING_FILE);
            overviewPage.selectMedia(0);

            overviewPage.addToFavoritesMedia();

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(overviewPage.getFavoriteMediaMessage(), MEDIA_ADDED_TO_FAVORITES,
                    "The message is not correct.");

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            FavoritesPage favoritesPage = overviewPage.goToFavoritesTab();

            Assert.assertEquals(favoritesPage.getFavoritesHeader(), FAVORITES_PAGE,
                    "The header is not correct.");

            Assert.assertTrue(favoritesPage.allMediaHasFavoriteIcon(),
                    "Not all media items have a favorite icon!");

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            favoritesPage.goToOverview();
            overviewPage.selectMedia(0);
            overviewPage.deleteMedia();
            TrashPage trashPage = overviewPage.goToTrashTab();
            trashPage.selectMedia(0);
            trashPage.deleteMediaPermanently();
        }

        @Test(description = "Successful remove selected media to Favorites album")
        @Description("Successful remove selected media to Favorites album")
        @Story("Favorites")
        @Severity(SeverityLevel.NORMAL)
        public void removeMediaToFavoritesTest() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);
            overviewPage.uploadMedia(FRENCH_FRIES_FILE);
            overviewPage.selectMedia(0);

            overviewPage.addToFavoritesMedia();

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(overviewPage.getFavoriteMediaMessage(), MEDIA_ADDED_TO_FAVORITES,
                    "The message is not correct.");

            FavoritesPage favoritesPage = overviewPage.goToFavoritesTab();

            Assert.assertEquals(favoritesPage.getFavoritesHeader(), FAVORITES_PAGE,
                    "The header is not correct.");

            Assert.assertTrue(favoritesPage.allMediaHasFavoriteIcon(),
                    "Not all media items have a favorite icon!");

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            favoritesPage.selectMedia(0);

            favoritesPage.removeMediaFromFavorites();

            Assert.assertEquals(overviewPage.getFavoriteMediaMessage(), MEDIA_REMOVED_FROM_FAVORITES,
                    "The message is not correct.");

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            favoritesPage.goToOverview();

            favoritesPage.refreshPage();

            Assert.assertTrue(favoritesPage.noMediaHasFavoriteIcon(),
                    "Favorite icons are present when none should be displayed.");

            overviewPage.selectMedia(0);
            overviewPage.deleteMedia();
            TrashPage trashPage = overviewPage.goToTrashTab();
            trashPage.selectMedia(0);
            trashPage.deleteMediaPermanently();
        }

        @Test(description = "Successful creation of album and deleting from Albums pane")
        @Description("Successful creation of album and deleting from Albums pane")
        @Story("Albums")
        @Severity(SeverityLevel.NORMAL)
        public void createDeleteAlbumTest() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            AlbumsPage albumsPage = overviewPage.goToAlbumsTab();

            albumsPage.createAlbum();
            albumsPage.enterAlbumsName("Test Album");

            Assert.assertEquals(albumsPage.getAlbumCreatedMessage(), ALBUM_CREATED_MESSAGE,
                    "The message is not correct.");

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            albumsPage.goToAlbumsTab();
            albumsPage.deleteAlbum();

            Assert.assertEquals(albumsPage.getToastDeletedMessage(), ALBUM_DELETED_MESSAGE,
                    "The message is not correct." );
        }

        @Test(description = "Successful creation of album and deleting from Album detail")
        @Description("Successful creation of album and deleting from Album detail")
        @Story("Albums")
        @Severity(SeverityLevel.NORMAL)
        public void deleteAlbumFromDetailsTest() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            AlbumsPage albumsPage = overviewPage.goToAlbumsTab();

            albumsPage.createAlbum();
            albumsPage.enterAlbumsName("Test Album");

            Assert.assertEquals(albumsPage.getAlbumCreatedMessage(), ALBUM_CREATED_MESSAGE,
                    "The message is not correct.");

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            albumsPage.goToAlbumsTab();
            albumsPage.clickAlbumName();
            albumsPage.deleteAlbumFromDetails();

            Assert.assertEquals(albumsPage.getToastDeletedMessage(), ALBUM_DELETED_MESSAGE_SHORT,
                    "The message is not correct." );
        }

        @Test(description = "Successful creation of album and deleting from Album detail")
        @Description("Successful creation of album and deleting from Album detail")
        @Story("Albums")
        @Severity(SeverityLevel.NORMAL)
        public void deleteAlbumWithSelectionTest() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            AlbumsPage albumsPage = overviewPage.goToAlbumsTab();

            albumsPage.createAlbum();
            albumsPage.enterAlbumsName("Test Album");

            Assert.assertEquals(albumsPage.getAlbumCreatedMessage(), ALBUM_CREATED_MESSAGE,
                    "The message is not correct.");

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            albumsPage.goToAlbumsTab();
            albumsPage.deleteAlbumWithSelection();

            Assert.assertEquals(albumsPage.getSelectedAlbumDeletedMessage(), SELECTED_ALBUM_DELETED_MESSAGE,
                    "The message is not correct." );
        }

        @Test(description = "Successful creation of album and adding media to an Album")
        @Description("Successful creation of album and adding media to an Album")
        @Story("Albums")
        @Severity(SeverityLevel.NORMAL)
        public void addMediaToAlbum() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);

            AlbumsPage albumsPage = overviewPage.goToAlbumsTab();
            albumsPage.createAlbum();
            albumsPage.enterAlbumsName("Test Album");

            albumsPage.goToOverview();

            overviewPage.uploadMedia(TRAINING_FILE);
            overviewPage.selectMedia(0);
            overviewPage.addMediaToAlbum();

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(overviewPage.getMediaToAlbumMessage(), MEDIA_ADDED_TO_ALBUM_MESSAGE,
                    "The message is not correct.");

            overviewPage.goToAlbumsTab();
            albumsPage.openAlbum();

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertTrue(
                    albumsPage.isImagePresentInAlbum(),
                    "Image is not present"
            );

            albumsPage.goToAlbumsTab();
            albumsPage.deleteAlbum();
            albumsPage.goToOverview();
            overviewPage.selectMedia(0);
            overviewPage.deleteMedia();
            TrashPage trashPage = overviewPage.goToTrashTab();
            trashPage.selectMedia(0);
            trashPage.deleteMediaPermanently();
        }

        @Test(description = "Successful creation of album and deleting from Album detail")
        @Description("Successful creation of album and deleting from Album detail")
        @Story("Albums")
        @Severity(SeverityLevel.NORMAL)
        public void addMediaFromAlbum() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);

            AlbumsPage albumsPage = overviewPage.goToAlbumsTab();
            albumsPage.createAlbum();
            albumsPage.enterAlbumsName("Test Album");
            albumsPage.uploadMediaFromAlbum(TRAINING_FILE);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(albumsPage.getUploadSuccessMessage(), FILE_UPLOADED,
                    "The message is not correct.");

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            albumsPage.goToOverview();

            Assert.assertTrue(
                    overviewPage.isImagePresentInGallery(),
                    "Image is not present"
            );

            overviewPage.selectMedia(0);
            overviewPage.deleteMedia();
            TrashPage trashPage = overviewPage.goToTrashTab();
            trashPage.selectMedia(0);
            trashPage.deleteMediaPermanently();
            trashPage.goToAlbumsTab();
            albumsPage.deleteAlbum();
            albumsPage.goToOverview();
        }
    }