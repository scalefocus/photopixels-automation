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
    import org.testng.annotations.BeforeClass;
    import org.testng.annotations.Listeners;
    import org.testng.annotations.Test;

    import java.util.List;

    import static com.photopixels.constants.Constants.*;
    import static com.photopixels.enums.ErrorMessagesEnum.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class CreateNewUserTests extends WebBaseTest {

        private String newEmail;
        private String password;
        private String adminEmail;
        private String adminPassword;
        private String randomName;
        private String invalidEmail;
        private String invalidPassword;
        private WaitOperationHelper waitHelper;

        @BeforeClass(alwaysRun = true)
        public void setup() {
            randomName = "User_" + RandomString.make(5);
            password = inputData.getPassword();
            adminEmail = inputData.getUsernameAdmin();
            adminPassword = inputData.getPasswordAdmin();
            invalidEmail = inputData.getInvalidEmail();
            invalidPassword = inputData.getInvalidPassword();
            waitHelper = new WaitOperationHelper(driver);
        }

        @Test(description = "Successful user creation")
        @Description("Successful creation of a user with correct login rules")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void createUserSuccessfullyTest() {
            newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();

            Assert.assertEquals(createUserPage.getCreateUserHeader(), CREATE_NEW_USER,
                    "The header is not correct.");

            createUserPage.createUser(randomName, newEmail, password);

            Assert.assertEquals(createUserPage.getUserCreatedMsg(), USER_CREATED,
                    "The message is not correct.");

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            Assert.assertTrue(usersPage.hasSearchResultEmail(newEmail),
                    "Expected email " + newEmail + " not found in search results. Found: " + usersPage.getEmailsFromResults());
            Assert.assertTrue(usersPage.hasSearchResultRole("User"),
                    "User role is not the expected one. Expected 'User', but found: " + usersPage.getRolesFromResults());
        }

        @Test(description = "Successful user creation")
        @Description("Successful creation of admin user with correct login rules")
        @Story("Create New user")
        @Severity(SeverityLevel.CRITICAL)
        public void createAdminSuccessfullyTest() {
            newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.selectAdminUserRole();
            createUserPage.createUser(randomName, newEmail, password);

            Assert.assertEquals(createUserPage.getUserCreatedMsg(), USER_CREATED,
                    "User is successfully created" );

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(newEmail);
            Assert.assertTrue(usersPage.hasSearchResultEmail(newEmail),
                    "Expected email " + newEmail + " not found in search results. Found: " + usersPage.getEmailsFromResults());
            Assert.assertTrue(usersPage.hasSearchResultRole("Admin"),
                    "User role is not the expected one. Expected 'Admin', but found: " + usersPage.getRolesFromResults());
        }

        @Test(description = "Unsuccessful user creation")
        @Description("Unsuccessful creation of a user invoking name error message")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void emptyNameField() {
            String emptyName = "";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(emptyName, newEmail, password);
            createUserPage.waitMs();

            String message = createUserPage.getNewNameUserValidationMessage();
            Assert.assertEquals(message, "Please fill out this field.",
                    "Error message for empty name field is not correct. Expected 'Please fill out this field.', but found:" + message);
        }

        @Test(description = "Unsuccessful user creation")
        @Description("Unsuccessful creation of a user invoking email address error message")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void emptyEmailAddressField() {
            String emptyEmail = "";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, emptyEmail, password);

            String message = createUserPage.getEmailAddressValidationMessage();
            Assert.assertEquals(message, "Please fill out this field.",
                    "Error message for empty email address field is not correct. Expected 'Please fill out this field.', but found:" + message);
        }

        @Test(description = "Unsuccessful user creation")
        @Description("Unsuccessful creation of a user invoking email format error message for missing @ sign")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void invalidEmailFormatError() {

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, invalidEmail, password);

            String message = createUserPage.getEmailAddressValidationMessage();
            Assert.assertEquals(message, "Please include an '@' in the email address. '" + invalidEmail + "' is missing an '@'.",
                    "Error message for invalid email format field is not correct. Expected 'Please include an '@' in the email address. '" + invalidEmail + "' is missing an '@'.', but found:" + message);
        }

        @Test(description = "Unsuccessful user creation")
        @Description("Unsuccessful creation of a user invoking empty password error message")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void emptyPasswordField() {
            String emptyPassword = "";
            newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";


            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, emptyPassword);

            String message = createUserPage.getPasswordValidationMessage();
            Assert.assertEquals(message, "Please fill out this field.",
                    "Error message for empty password field is not correct. Expected 'Please fill out this field.', but found:" + message);
        }

        @Test(description = "Unsuccessful user creation")
        @Description("Unsuccessful creation of a user due to invalid password format")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void invalidPasswordFormatError() {
            newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, invalidPassword);

            List<String> errorMessages = createUserPage.getErrorMessages();
            Assert.assertTrue(errorMessages.contains(PASSWORD_TOO_SHORT.getErrorMessage()),
                    "Expected error message '" + PASSWORD_TOO_SHORT.getErrorMessage() + "' not found. Actual errors: " + errorMessages);
            Assert.assertTrue(errorMessages.contains(PASSWORD_REQUIRES_NON_ALPHANUMERIC.getErrorMessage()),
                    "Expected error message '" + PASSWORD_REQUIRES_NON_ALPHANUMERIC.getErrorMessage() + "' not found. Actual errors: " + errorMessages);
            Assert.assertTrue(errorMessages.contains(PASSWORD_REQUIRES_DIGIT.getErrorMessage()),
                    "Expected error message '" + PASSWORD_REQUIRES_DIGIT.getErrorMessage() + "' not found. Actual errors: " + errorMessages);
            Assert.assertTrue(errorMessages.contains(PASSWORD_REQUIRES_UPPER.getErrorMessage()),
                    "Expected error message '" + PASSWORD_REQUIRES_UPPER.getErrorMessage() + "' not found. Actual errors: " + errorMessages);
        }

        @Test(description = "Unsuccessful user creation")
        @Description("Unsuccessful creation of a user due to duplicate email")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void duplicateEmailError() {
            newEmail = "test" + RandomStringUtils.randomNumeric(9) + "@test.com";

            LoginPage loginPage = loadPhotoPixelsApp();
            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);
            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, newEmail, password);
            createUserPage.goToCreateNewUser();

            //No other way to handle the revisit of the site, due to the state and the way webdriver interacts with the page.
            waitHelper.waitMs();
            createUserPage.createUser(randomName, newEmail, password);

            List<String> errorMessages = createUserPage.getErrorMessages();
            String expectedEmailError = String.format(DUPLICATE_EMAIL.getErrorMessage(), newEmail);
            Assert.assertTrue(errorMessages.contains(expectedEmailError),
                    "Expected email error message '" + expectedEmailError + "' not found. Actual error: " + errorMessages);
            Assert.assertEquals(1, errorMessages.size(),
                    "Expected exactly 1 error message (email), but found " + errorMessages.size() + ": " + errorMessages);
        }
    }