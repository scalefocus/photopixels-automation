    package com.photopixels.web;

    import com.photopixels.base.WebBaseTest;
    import com.photopixels.listeners.StatusTestListener;
    import com.photopixels.web.pages.*;
    import io.qameta.allure.*;
    import org.testng.Assert;
    import org.testng.annotations.*;

    import static com.photopixels.constants.Constants.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class DeleteMediaTests extends WebBaseTest {

        private String username;
        private String password;

        @BeforeClass(alwaysRun = true)
        public void setup() throws Exception {
            username = inputData.getUsername();
            password = inputData.getPassword();
        }

        @Test(description = "Successful upload, selection and moving to trash for selected media")
        @Description("Successful upload and deletion of media")
        @Story("Delete Media")
        @Severity(SeverityLevel.NORMAL)
        public void deleteMedia() {
            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);
            overviewPage.uploadMedia(TRAINING_FILE);

            Assert.assertEquals(overviewPage.getUploadSuccessMessage(), FILE_UPLOADED,
                    "The message is not correct.");

            overviewPage.selectMedia(0);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            overviewPage.deleteMedia();

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(overviewPage.getDeleteMediaMessage(), FILE_DELETED,
                    "The message is not correct.");
        }

        @Test(description = "Successful deleting the selection made in trash page")
        @Description("Successful deleting the selection made in trash page")
        @Story("Delete Media")
        @Severity(SeverityLevel.NORMAL)
        public void emptyTrashWithSelection() {
            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);
            overviewPage.uploadMedia(FRENCH_FRIES_FILE);

            overviewPage.selectMedia(0);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            overviewPage.deleteMedia();

            TrashPage trashPage = overviewPage.goToTrashTab();

            Assert.assertEquals(trashPage.getTrashHeader(), TRASH_PAGE,
                    "The header is not correct.");

            trashPage.selectMedia(0);

            trashPage.deleteMediaPermanently();

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(overviewPage.getDeleteMediaMessage(), FILE_PERMANENTLY_DELETED,
                    "The message is not correct.");
        }

        @Test(description = "Successful emptying the trash through Empty Trash button")
        @Description("Successful emptying the trash through Empty Trash button")
        @Story("Delete Media")
        @Severity(SeverityLevel.NORMAL)
        public void emptyTrashWithEmptyTrashButton() {
            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(username, password);
            overviewPage.uploadMedia(FRENCH_FRIES_FILE);

            overviewPage.selectMedia(0);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            overviewPage.deleteMedia();

            TrashPage trashPage = overviewPage.goToTrashTab();

            Assert.assertEquals(trashPage.getTrashHeader(), TRASH_PAGE,
                    "The header is not correct.");

            trashPage.emptyTrash();

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(trashPage.getDeleteMediaMessage(), FILE_DELETED,
                    "The message is not correct.");
        }
    }