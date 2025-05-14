    package com.photopixels.web;

    import com.photopixels.base.WebBaseTest;
    import com.photopixels.helpers.MediaGeneratorHelper;
    import com.photopixels.helpers.WaitOperationHelper;
    import com.photopixels.listeners.StatusTestListener;
    import com.photopixels.web.pages.CreateUserPage;
    import com.photopixels.web.pages.LoginPage;
    import com.photopixels.web.pages.OverviewPage;
    import com.photopixels.web.pages.UsersPage;
    import io.qameta.allure.*;
    import net.bytebuddy.utility.RandomString;
    import org.apache.commons.lang3.RandomStringUtils;
    import org.testng.Assert;
    import org.testng.annotations.*;

    import static com.photopixels.constants.Constants.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class EditUserTests extends WebBaseTest {

        private String newEmail;
        private String password;
        private String adminEmail;
        private String adminPassword;
        private String randomName;
        private WaitOperationHelper waitHelper;
        private MediaGeneratorHelper mediaGenerator;
        private static final String LARGE_IMAGE_PATH = "target/upload_files/test-large-image.png";

        @BeforeClass(alwaysRun = true)
        public void setup() throws Exception {
            randomName = "User_" + RandomString.make(5);
            password = inputData.getPassword();
            adminEmail = inputData.getUsernameAdmin();
            adminPassword = inputData.getPasswordAdmin();
            waitHelper = new WaitOperationHelper(driver);
            newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";
            mediaGenerator = new MediaGeneratorHelper(LARGE_IMAGE_PATH, 18560, 18560);
            mediaGenerator.createLargeImage();
        }

        @Test(description = "Changing active user quota, setting to minimal value")
        @Description("Changing active user quota, setting to minimal value")
        @Story("Edit a user")
        @Severity(SeverityLevel.NORMAL)
        public void editUserQuotaSuccessfully() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            createUserPage.waitMs();
            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.editUser();
            usersPage.editUserQuota("1");

            String actualMessage = usersPage.getUserQuotaChangedMessage();
            Assert.assertEquals(usersPage.getUserQuotaChangedMessage(), QUOTA_CHANGED,
                    "The message is not correct. Expected: " + QUOTA_CHANGED + ", but found: " + actualMessage);

            String quotaText = usersPage.getQuotaParagraphText();
            Assert.assertTrue(quotaText.contains("1.00 GB"), "Quota text should contain '1.00 GB'"); // Test fails die to bug with system unable to update the quote per user

            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.editUser();
            usersPage.deleteUser();
        }

        @Test(description = "Changing active user quota, setting below minimal value")
        @Description("Changing active user quota, setting below minimal value")
        @Story("Edit a user")
        @Severity(SeverityLevel.NORMAL)
        public void editUserQuotaBelowThreshold() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            createUserPage.waitMs();
            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs();
            usersPage.editUser();
            usersPage.editUserQuota("0.5");

            String message = usersPage.getQuotaValueValidationMessage();
            Assert.assertEquals(message, "Please enter a valid value. The two nearest valid values are 0 and 1.",
                    "Error message for minimal quota value is not correct. Expected 'Please enter a valid value. The two nearest valid values are 0 and 1.', but found:" + message);

            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.editUser();
            usersPage.deleteUser();
        }

        @Test(description = "Uploading a file over the quota threshold")
        @Description("Uploading a file over the quota threshold")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void uploadFileAboveThreshold() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            createUserPage.waitMs();
            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs();
            usersPage.editUser();
            usersPage.editUserQuota("1");
            usersPage.logOut();
            loginPage.login(newEmail, password);
            overviewPage.uploadMedia(mediaGenerator.getFilePath());
            String errorMessage = overviewPage.getUploadErrorMessage();
            Assert.assertEquals(errorMessage, "File size exceeds 1 GB quota.",
                    "Expected quota exceedance error. Found: " + errorMessage);
            overviewPage.logOut();
            loginPage.login(adminEmail, adminPassword);
            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.editUser();
            usersPage.deleteUser();
        }

        @AfterMethod(alwaysRun = true)
        public void tearDownTest() {
            if (mediaGenerator != null) {
                mediaGenerator.deleteLargeImage();
            }
        }
    }