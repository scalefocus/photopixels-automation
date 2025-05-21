    package com.photopixels.web;

    import com.photopixels.base.WebBaseTest;
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

    import java.io.File;

    import static com.photopixels.constants.Constants.*;
    import static com.photopixels.constants.ErrorMessageConstants.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class EditUserTests extends WebBaseTest {

        private String newEmail;
        private String password;
        private String adminEmail;
        private String adminPassword;
        private String randomName;
        private String newRandomPassword;
        private WaitOperationHelper waitHelper;
        private static final String LARGE_IMAGE_PATH = "target/upload_files/test-large-image.png";

        @BeforeClass(alwaysRun = true)
        public void setup() throws Exception {
            randomName = "User_" + RandomString.make(5);
            password = inputData.getPassword();
            newRandomPassword = inputData.getNewPassword();
            adminEmail = inputData.getUsernameAdmin();
            adminPassword = inputData.getPasswordAdmin();
            waitHelper = new WaitOperationHelper(driver);
            newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";
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
            Assert.assertTrue(quotaText.contains("1.00 GB"), "Quota text should contain '1.00 GB'");

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

        //TO DO: The following test and entire upload logic will resume after all media operations are within the system.

//        @Test(description = "Uploading a file over the quota threshold")
//        @Description("Uploading a file over the quota threshold")
//        @Story("Edit a user")
//        @Severity(SeverityLevel.CRITICAL)
//        public void uploadFileAboveThreshold() {
//            String imagePath = "src/test/resources/images/sample-image.jpg";
//            File imageFile = new File(imagePath);
//            String absoluteImagePath = imageFile.getAbsolutePath();
//
//            LoginPage loginPage = loadPhotoPixelsApp();
//            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
//            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
//            createUserPage.createUser(randomName, newEmail, password);
//            createUserPage.waitMs();
//            UsersPage usersPage = overviewPage.goToUserTab();
//            usersPage.searchUser(newEmail);
//            usersPage.waitMs();
//            usersPage.editUser();
//            usersPage.editUserQuota("1");
//            usersPage.logOut();
//            loginPage.login(newEmail, password);
//            overviewPage.uploadMedia(absoluteImagePath);
//            String errorMessage = overviewPage.getUploadErrorMessage();
//            Assert.assertEquals(errorMessage, "File size exceeds 1 GB quota.",
//                    "Expected quota exceedance error. Found: " + errorMessage);
//            overviewPage.logOut();
//            loginPage.login(adminEmail, adminPassword);
//            usersPage.goToUserTab();
//            usersPage.searchUser(newEmail);
//            usersPage.editUser();
//            usersPage.deleteUser();
//        }

        @Test(description = "Changing active user password, setting expected value")
        @Description("Changing active user password, setting expected value")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserPasswordCorrectlyEntered() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            createUserPage.waitMs();
            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs();
            usersPage.editUser();
            usersPage.userPasswordReset();
            usersPage.newUserPassword(newRandomPassword, newRandomPassword);
            waitHelper.waitMs();
            usersPage.clickPasswordReset();

            String actualMessage = usersPage.getPasswordChangedMessage();
            Assert.assertEquals(actualMessage, PASSWORD_CHANGED,
                    "The message is not correct. Expected: " + PASSWORD_CHANGED + ", but found: " + actualMessage);


            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.editUser();
            usersPage.deleteUser();
        }

        @Test(description = "Changing active user password, setting faulty value")
        @Description("Changing active user password, setting faulty value")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserPasswordIncorrectlyEntered() {
            String faultyPassword = "abcABC";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            createUserPage.waitMs();
            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs();
            usersPage.editUser();
            usersPage.userPasswordReset();
            usersPage.newUserPassword(faultyPassword, faultyPassword);
            usersPage.clickPasswordReset();

            String actualMessage = usersPage.getPasswordChangedErrorMessage();
            Assert.assertEquals(actualMessage, PASSWORD_CHANGED_INCORRECTLY,
                    "The message is not correct. Expected: " + PASSWORD_CHANGED_INCORRECTLY + ", but found: " + actualMessage);
            //test fails due to reported bug https://github.com/scalefocus/photopixels/issues/93

            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.editUser();
            usersPage.deleteUser();
        }

        @Test(description = "Changing active user password, with missmatch in the confirm password field")
        @Description("Changing active user password, with missmatch in the confirm password field")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserConfirmPasswordMismatch() {
            String faultyPassword = "abcABC";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            createUserPage.waitMs();
            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs();
            usersPage.editUser();
            usersPage.userPasswordReset();
            usersPage.newUserPassword(newRandomPassword, faultyPassword);
            usersPage.clickPasswordReset();

            String actualMessage = usersPage.getPasswordChangedErrorMessage();
            Assert.assertEquals(actualMessage, PASSWORD_MISMATCH,
                    "The message is not correct. Expected: " + PASSWORD_MISMATCH + ", but found: " + actualMessage);


            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.editUser();
            usersPage.deleteUser();
        }
    }