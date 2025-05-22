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
        private WaitOperationHelper waitHelper;
        private UsersPage usersPage;
        private OverviewPage overviewPage;

        @BeforeClass(alwaysRun = true)
        public void setup() throws Exception {
            randomName = "User_" + RandomString.make(5);
            password = inputData.getPassword();
            adminEmail = inputData.getUsernameAdmin();
            adminPassword = inputData.getPasswordAdmin();
            waitHelper = new WaitOperationHelper(driver);
            newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";
        }

        @BeforeMethod(alwaysRun = true)
        public void initializePages() {
            LoginPage loginPage = loadPhotoPixelsApp();
            overviewPage = loginPage.login(adminEmail, adminPassword);
            usersPage = overviewPage.goToUserTab();
        }

        @Test(description = "Changing active user quota, setting to minimal value")
        @Description("Changing active user quota, setting to minimal value")
        @Story("Edit a user")
        @Severity(SeverityLevel.NORMAL)
        public void editUserQuotaSuccessfully() {
            String quotaValue = "1";
            String expectedQuota = "1.00 GB";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.clickEditUser();
            usersPage.editUserQuota(quotaValue);

            String actualMessage = usersPage.getUserQuotaChangedMessage();
            Assert.assertEquals(usersPage.getUserQuotaChangedMessage(), QUOTA_CHANGED,
                    "The message is not correct. Expected: " + QUOTA_CHANGED + ", but found: " + actualMessage);

            String quotaText = usersPage.getQuotaParagraphText();
            Assert.assertTrue(quotaText.contains(expectedQuota), "Quota text should contain " + expectedQuota);
        }

        @Test(description = "Changing active user quota, setting below minimal value")
        @Description("Changing active user quota, setting below minimal value")
        @Story("Edit a user")
        @Severity(SeverityLevel.NORMAL)
        public void editUserQuotaBelowThreshold() {
            String quotaValue = "0.5";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            usersPage.clickEditUser();
            usersPage.editUserQuota(quotaValue);

            String message = usersPage.getQuotaValueValidationMessage();
            Assert.assertEquals(message, "Please enter a valid value. The two nearest valid values are 0 and 1.",
                    "Error message for minimal quota value is not correct. Expected 'Please enter a valid value. " + "The two nearest valid values are 0 and 1.', but found:" + message);
        }

        @Test(description = "Changing active user password, setting expected value")
        @Description("Changing active user password, setting expected value")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserPasswordCorrectlyEntered() {
            String newRandomPassword = "NewTest12345!";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            createUserPage.waitMs();
            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            usersPage.clickEditUser();
            usersPage.userPasswordReset();
            usersPage.enterNewUserPassword(newRandomPassword, newRandomPassword);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            usersPage.clickPasswordReset();

            String actualMessage = usersPage.getPasswordChangedMessage();
            Assert.assertEquals(actualMessage, PASSWORD_CHANGED,
                    "The message is not correct. Expected: " + PASSWORD_CHANGED + ", but found: " + actualMessage);
        }

        @Test(description = "Changing active user password, setting faulty value")
        @Description("Changing active user password, setting faulty value")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserPasswordIncorrectlyEntered() {
            String faultyPassword = "abcABC";

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            usersPage.clickEditUser();
            usersPage.userPasswordReset();
            usersPage.enterNewUserPassword(faultyPassword, faultyPassword);
            usersPage.clickPasswordReset();

            String actualMessage = usersPage.getPasswordChangedErrorMessage();
            Assert.assertEquals(actualMessage, PASSWORD_CHANGED_INCORRECTLY,
                    "The message is not correct. Expected: " + PASSWORD_CHANGED_INCORRECTLY + ", but found: " + actualMessage);
            // TODO: Remove when issue is fixed addIssueLinkToAllureReport("93");
        }

        @Test(description = "Changing active user password, with missmatch in the confirm password field")
        @Description("Changing active user password, with missmatch in the confirm password field")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserConfirmPasswordMismatch() {
            String faultyPassword = "abcABC";
            String newRandomPassword = "NewTest12345!";


            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            waitHelper.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            usersPage.clickEditUser();
            usersPage.userPasswordReset();
            usersPage.enterNewUserPassword(newRandomPassword, faultyPassword);
            usersPage.clickPasswordReset();

            String actualMessage = usersPage.getPasswordChangedErrorMessage();
            Assert.assertEquals(actualMessage, PASSWORD_MISMATCH,
                    "The message is not correct. Expected: " + PASSWORD_MISMATCH + ", but found: " + actualMessage);
        }

        @AfterMethod(alwaysRun = true)
        public void cleanupUser() {
            usersPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.clickEditUser();
            usersPage.deleteUser();
        }
    }