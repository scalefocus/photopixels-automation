    package com.photopixels.web;

    import com.photopixels.base.IApiBaseTest;
    import com.photopixels.base.WebBaseTest;
    import com.photopixels.enums.UserRolesEnum;
    import com.photopixels.listeners.StatusTestListener;
    import com.photopixels.web.pages.EditUserPage;
    import com.photopixels.web.pages.LoginPage;
    import com.photopixels.web.pages.OverviewPage;
    import com.photopixels.web.pages.UsersPage;
    import io.qameta.allure.*;
    import net.bytebuddy.utility.RandomString;
    import org.testng.Assert;
    import org.testng.annotations.AfterClass;
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Listeners;
    import org.testng.annotations.Test;

    import java.util.ArrayList;
    import java.util.List;

    import static com.photopixels.constants.Constants.*;
    import static com.photopixels.constants.Constants.PASSWORD;
    import static com.photopixels.constants.ErrorMessageConstants.INVALID_QUOTA;
    import static com.photopixels.constants.ErrorMessageConstants.PASSWORD_MISMATCH;
    import static com.photopixels.enums.ErrorMessagesEnum.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class EditUserTests extends WebBaseTest implements IApiBaseTest {

        private String newEmail;
        private String adminEmail;
        private String adminPassword;

        private List<String> registeredUsers;

        @BeforeClass(alwaysRun = true)
        public void setup() {
            String random = RandomString.make(5);
            String randomName = "User_" + random;
            newEmail = "test_" + random + "@test.com";

            registerUser(randomName, newEmail, PASSWORD, UserRolesEnum.USER);

            registeredUsers = new ArrayList<>();
            registeredUsers.add(newEmail);

            adminEmail = inputData.getUsernameAdmin();
            adminPassword = inputData.getPasswordAdmin();
        }

        @AfterClass(alwaysRun = true)
        public void deleteUsers() {
            IApiBaseTest.super.deleteRegisteredUsersAdmin(registeredUsers);
        }

        @Test(description = "Changing active user quota, setting to minimal value")
        @Description("Changing active user quota, setting to minimal value")
        @Story("Edit a user")
        @Severity(SeverityLevel.NORMAL)
        public void editUserQuotaSuccessfullyTest() {
            String quotaValue = "1";
            String expectedQuota = "1.00 GB";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

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
        public void editUserQuotaBelowThresholdTest() {
            String quotaValue = "0.5";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

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
        public void editUserPasswordCorrectlyEnteredTest() {
            String newRandomPassword = "NewTest12345!";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

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
        public void editUserPasswordIncorrectlyEnteredTest() {
            String faultyPassword = "abcABC";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            usersPage.waitMs(); //Necessary wait, in order to handle the speed of the execution, as no other
            // dynamic wait was executing properly.

            EditUserPage editUserPage = usersPage.clickEditUser();
            editUserPage.userPasswordReset();
            editUserPage.enterNewUserPassword(faultyPassword, faultyPassword);
            editUserPage.clickPasswordReset();

            List<String> errorMessages = editUserPage.getAllMessagesSeparated();

            Assert.assertEquals(errorMessages.get(0), PASSWORD_TOO_SHORT.getErrorMessage(),
                    "Expected error message '" + PASSWORD_TOO_SHORT.getErrorMessage() + "' not found. "
                            + "Actual errors: " + errorMessages);
            Assert.assertEquals(errorMessages.get(1) , PASSWORD_REQUIRES_NON_ALPHANUMERIC.getErrorMessage(),
                    "Expected error message '" + PASSWORD_REQUIRES_NON_ALPHANUMERIC.getErrorMessage()
                            + "' not found. Actual errors: " + errorMessages);
            Assert.assertEquals(errorMessages.get(2), PASSWORD_REQUIRES_DIGIT.getErrorMessage(),
                    "Expected error message '" + PASSWORD_REQUIRES_DIGIT.getErrorMessage()
                            + "' not found. Actual errors: " + errorMessages);
        }

        @Test(description = "Changing active user password, with missmatch in the confirm password field")
        @Description("Changing active user password, with missmatch in the confirm password field")
        @Story("Edit a user")
        @Severity(SeverityLevel.CRITICAL)
        public void editUserConfirmPasswordMismatchTest() {
            String faultyPassword = "abcABC";
            String newRandomPassword = "NewTest12345!";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

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