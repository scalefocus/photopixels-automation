    package com.photopixels.web;

    import com.photopixels.base.WebBaseTest;
    import com.photopixels.helpers.WaitOperationHelper;
    import com.photopixels.listeners.StatusTestListener;
    import com.photopixels.web.pages.*;
    import io.qameta.allure.*;
    import net.bytebuddy.utility.RandomString;
    import org.apache.commons.lang3.RandomStringUtils;
    import org.testng.Assert;
    import org.testng.annotations.*;

    import static com.photopixels.constants.Constants.*;
    import static com.photopixels.constants.ErrorMessageConstants.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class EditUserTests extends WebBaseTest {

        private String newEmail;
        private String adminEmail;
        private String adminPassword;
        private String randomName;
        private UsersPage usersPage;
        private EditUserPage editUserPage;
        private OverviewPage overviewPage;

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
        }

        @AfterMethod(alwaysRun = true)
        public void cleanupUser() {
            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.clickEditUser();
            editUserPage.deleteUser();
        }

        @Test(description = "Changing active user quota, setting to minimal value")
        @Description("Changing active user quota, setting to minimal value")
        @Story("Edit a user")
        @Severity(SeverityLevel.NORMAL)
        public void editUserQuotaSuccessfully() {
            String quotaValue = "1";
            String expectedQuota = "1.00 GB";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, PASSWORD);

            //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.
            createUserPage.waitMs();

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);

            EditUserPage editUserPage = usersPage.clickEditUser();
            editUserPage.editUserQuota(quotaValue);

            Assert.assertEquals(editUserPage.getUserQuotaChangedMessage(), QUOTA_CHANGED,
                    "The message is not correct.");

            String quotaText = editUserPage.getQuotaParagraphText();
            Assert.assertTrue(quotaText.contains(expectedQuota),
                    "Quota text should contain " + expectedQuota);
        }

        @Test(description = "Changing active user quota, setting below minimal value")
        @Description("Changing active user quota, setting below minimal value")
        @Story("Edit a user")
        @Severity(SeverityLevel.NORMAL)
        public void editUserQuotaBelowThreshold() {
            String quotaValue = "0.5";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, PASSWORD);

            createUserPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            EditUserPage editUserPage = usersPage.clickEditUser();
            editUserPage.editUserQuota(quotaValue);

            String message = editUserPage.getQuotaValueValidationMessage();
            Assert.assertEquals(message, INVALID_QUOTA,
                    "Error message for minimal quota value is not correct.");
        }

        @Test(description = "Changing active user password, setting expected value")
        @Description("Changing active user password, setting expected value")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserPasswordCorrectlyEntered() {
            String newRandomPassword = "NewTest12345!";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, PASSWORD);
            createUserPage.waitMs();

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            EditUserPage editUserPage = usersPage.clickEditUser();
            editUserPage.userPasswordReset();
            editUserPage.enterNewUserPassword(newRandomPassword, newRandomPassword);
            editUserPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            editUserPage.clickPasswordReset();

            String actualMessage = editUserPage.getPasswordChangedMessage();
            Assert.assertEquals(actualMessage, PASSWORD_CHANGED, "The message is not correct.");
        }

        @Test(description = "Changing active user password, setting faulty value")
        @Description("Changing active user password, setting faulty value")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserPasswordIncorrectlyEntered() {
            String faultyPassword = "abcABC";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, PASSWORD);
            createUserPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            EditUserPage editUserPage = usersPage.clickEditUser();
            editUserPage.userPasswordReset();
            editUserPage.enterNewUserPassword(faultyPassword, faultyPassword);
            editUserPage.clickPasswordReset();

            String actualMessage = editUserPage.getPasswordChangedErrorMessage();
            
            // TODO: Remove when issue is fixed
            addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/93");

            Assert.assertEquals(actualMessage, PASSWORD_CHANGED_INCORRECTLY, "The message is not correct.");

        }

        @Test(description = "Changing active user password, with missmatch in the confirm password field")
        @Description("Changing active user password, with missmatch in the confirm password field")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserConfirmPasswordMismatch() {
            String faultyPassword = "abcABC";
            String newRandomPassword = "NewTest12345!";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, PASSWORD);
            createUserPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            EditUserPage editUserPage = usersPage.clickEditUser();
            editUserPage.userPasswordReset();
            editUserPage.enterNewUserPassword(newRandomPassword, faultyPassword);
            editUserPage.clickPasswordReset();

            String actualMessage = editUserPage.getPasswordChangedErrorMessage();
            Assert.assertEquals(actualMessage, PASSWORD_MISMATCH, "The message is not correct.");
        }

    }