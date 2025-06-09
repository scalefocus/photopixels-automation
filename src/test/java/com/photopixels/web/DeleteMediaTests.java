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

        private String newEmail;
        private String adminEmail;
        private String adminPassword;
        private String randomName;
        private UsersPage usersPage;
        private EditUserPage editUserPage;
        private OverviewPage overviewPage;
        private TrashPage trashPage;

        @BeforeClass(alwaysRun = true)
        public void setup() throws Exception {
            String random = RandomString.make(5);
            randomName = "User_" + random;
            newEmail = "test_" + random + "@test.com";

            adminEmail = inputData.getUsernameAdmin();
            adminPassword = inputData.getPasswordAdmin();
        }

        @BeforeMethod(alwaysRun = true)
        public void initializePages() {
            LoginPage loginPage = loadPhotoPixelsApp();
            overviewPage = loginPage.login(adminEmail, adminPassword);
            usersPage = overviewPage.goToUserTab();
            editUserPage = new EditUserPage(driver);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, PASSWORD);
            createUserPage.waitMs();
            createUserPage.logOut();
        }

        @AfterMethod(alwaysRun = true)
        public void cleanupUser() {
            LoginPage loginPage = loadPhotoPixelsApp();
            overviewPage = loginPage.login(adminEmail, adminPassword);
            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.clickEditUser();
            editUserPage.deleteUser();
        }

        @Test(description = "Successful upload, selection and moving to trash for selected media")
        @Description("Successful upload and deletion of media")
        @Story("Delete Media")
        @Severity(SeverityLevel.NORMAL)
        public void deleteMedia() {
            String filePath = "C:/Users/nebojsha.stamenkov/Documents/photopixels-test-automation/upload_files/coctail.jpg";

            LoginPage loginPage = loadPhotoPixelsApp();
            overviewPage = loginPage.login(newEmail, PASSWORD);
            overviewPage.uploadMedia(filePath);

            Assert.assertEquals(overviewPage.getUploadSuccessMessage(), FILE_UPLOADED,
                    "The message is not correct.");

            overviewPage.selectMedia(0);
            overviewPage.deleteMedia();

            Assert.assertEquals(overviewPage.getDeleteMediaMessage(), FILE_DELETED,
                    "The message is not correct.");

            overviewPage.logOut();
        }
    }