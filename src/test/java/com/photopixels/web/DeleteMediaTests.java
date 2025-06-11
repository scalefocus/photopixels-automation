    package com.photopixels.web;

    import com.photopixels.base.WebBaseTest;
    import com.photopixels.listeners.StatusTestListener;
    import com.photopixels.web.pages.*;
    import io.qameta.allure.*;
    import net.bytebuddy.utility.RandomString;
    import org.testng.Assert;
    import org.testng.annotations.*;

    import static com.photopixels.constants.Constants.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class DeleteMediaTests extends WebBaseTest {

        private String username;
        private String password;
        private UsersPage usersPage;
        private EditUserPage editUserPage;
        private OverviewPage overviewPage;
        private TrashPage trashPage;

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
            overviewPage = loginPage.login(username, password);
            overviewPage.uploadMedia(TRAINING_FILE);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            Assert.assertEquals(overviewPage.getUploadSuccessMessage(), FILE_UPLOADED,
                    "The message is not correct.");



            overviewPage.selectMedia(0);

            overviewPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            overviewPage.deleteMedia();

            Assert.assertEquals(overviewPage.getDeleteMediaMessage(), FILE_DELETED,
                    "The message is not correct.");

            overviewPage.logOut();
        }
    }