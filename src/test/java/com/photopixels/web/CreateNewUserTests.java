    package com.photopixels.web;

    import com.photopixels.base.IApiBaseTest;
    import com.photopixels.base.WebBaseTest;
    import com.photopixels.constants.Constants;
    import com.photopixels.enums.UserRolesEnum;
    import com.photopixels.listeners.StatusTestListener;
    import com.photopixels.web.pages.CreateUserPage;
    import com.photopixels.web.pages.LoginPage;
    import com.photopixels.web.pages.OverviewPage;
    import com.photopixels.web.pages.UsersPage;
    import io.qameta.allure.*;
    import net.bytebuddy.utility.RandomString;
    import org.testng.Assert;
    import org.testng.annotations.*;

    import java.util.ArrayList;
    import java.util.List;

    import static com.photopixels.constants.Constants.CREATE_NEW_USER;
    import static com.photopixels.constants.Constants.USER_CREATED;
    import static com.photopixels.enums.ErrorMessagesEnum.*;

    @Listeners(StatusTestListener.class)
    @Feature("Web")
    public class CreateNewUserTests extends WebBaseTest implements IApiBaseTest {

        private String adminEmail;
        private String adminPassword;
        private String randomName;
        private String randomEmail;
        private final int expectedNumberOfErrors = 1;

        private List<String> registeredUsers;

        @BeforeClass(alwaysRun = true)
        public void setup() {
            adminEmail = IApiBaseTest.inputData.getUsernameAdmin();
            adminPassword = IApiBaseTest.inputData.getPasswordAdmin();

            registeredUsers = new ArrayList<>();
        }

        @BeforeMethod
        public void generateRandoms() {
            String random = RandomString.make(5);
            randomName = "User_" + random;
            randomEmail = "test_" + random + "@test.com";
        }

        @AfterClass
        public void deleteUsers() {
            IApiBaseTest.super.deleteRegisteredUsersAdmin(registeredUsers);
        }

        @Test(description = "Successful creation of a user with correct login credential rules")
        @Description("Successful creation of a user with correct login credential rules")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void createUserSuccessfullyTest() {
            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();

            Assert.assertEquals(createUserPage.getCreateUserHeader(), CREATE_NEW_USER,
                    "The header is not correct.");

            createUserPage.createUser(randomName, randomEmail, Constants.PASSWORD);

            registeredUsers.add(randomEmail);

            Assert.assertEquals(createUserPage.getUserCreatedMsg(), USER_CREATED,
                    "The message is not correct.");

            UsersPage usersPage = overviewPage.goToUserTab();
            usersPage.searchUser(randomEmail);

            Assert.assertTrue(usersPage.hasSearchResultEmail(randomEmail),
                    "Expected email " + randomEmail + " not found in search results. Found: "
                            + usersPage.getEmailsFromResults());
            Assert.assertTrue(usersPage.hasSearchResultRole(UserRolesEnum.USER.getText()),
                    "User role is not the expected one. Expected 'User', but found: "
                            + usersPage.getRolesFromResults());
        }

        @Test(description = "Successful creation of admin user with correct login rules")
        @Description("Successful creation of admin user with correct login rules")
        @Story("Create New User")
        @Severity(SeverityLevel.CRITICAL)
        public void createAdminSuccessfullyTest() {
            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.selectAdminUserRole();
            createUserPage.createUser(randomName, randomEmail, Constants.PASSWORD);

            registeredUsers.add(randomEmail);

            Assert.assertEquals(createUserPage.getUserCreatedMsg(), USER_CREATED,
                    "User is successfully created" );

            UsersPage usersPage = overviewPage.goToUserTab();

            usersPage.searchUser(randomEmail);

            Assert.assertTrue(usersPage.hasSearchResultEmail(randomEmail),
                    "Expected email " + randomEmail + " not found in search results. Found: "
                            + usersPage.getEmailsFromResults());
            Assert.assertTrue(usersPage.hasSearchResultRole(UserRolesEnum.ADMIN.getText()),
                    "User role is not the expected one. Expected 'Admin', but found: "
                            + usersPage.getRolesFromResults());
    }

        @Test(description = "Unsuccessful user creation with empty name")
        @Description("Unsuccessful creation of a user invoking name error message")
        @Story("Create New User")
        @Severity(SeverityLevel.MINOR)
        public void createUserEmptyNameFieldTest() {
            String emptyName = "";

            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(emptyName, randomEmail, Constants.PASSWORD);
            createUserPage.waitMs();

            String message = createUserPage.getNewNameUserValidationMessage();

            Assert.assertEquals(message, EMPTY_FIELD_ERROR.getErrorMessage(),
                    "Error message for empty name field is not correct. " +
                            "Expected 'Please fill out this field.', but found:" + message);
        }

        @Test(description = "Unsuccessful user creation with empty email address")
        @Description("Unsuccessful creation of a user invoking email address error message")
        @Story("Create New User")
        @Severity(SeverityLevel.MINOR)
        public void createUserEmptyEmailAddressFieldTest() {
            String emptyEmail = "";

            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, emptyEmail, Constants.PASSWORD);

            String message = createUserPage.getEmailAddressValidationMessage();

            Assert.assertEquals(message, EMPTY_FIELD_ERROR.getErrorMessage(),
                    "Error message for empty email address field is not correct. " +
                            "Expected 'Please fill out this field.', but found:" + message);
        }

        @Test(description = "Unsuccessful user creation with invalid email format")
        @Description("Unsuccessful creation of a user invoking email format error message for missing @ sign")
        @Story("Create New User")
        @Severity(SeverityLevel.NORMAL)
        public void createUserInvalidEmailFormatErrorTest() {
            String invalidEmail = "eqw2ASKD.COM";

            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();

            createUserPage.createUser(randomName, invalidEmail, Constants.PASSWORD);

            String message = createUserPage.getEmailAddressValidationMessage();

            Assert.assertEquals(message, "Please include an '@' in the email address. '"
                            + invalidEmail + "' is missing an '@'.",
                    "Error message for invalid email format field is not correct. " +
                            "Expected 'Please include an '@' in the email address. '"
                            + invalidEmail + "' is missing an '@'.', but found:" + message);
        }

        @Test(description = "Unsuccessful user creation with empty password")
        @Description("Unsuccessful creation of a user invoking empty password error message")
        @Story("Create New User")
        @Severity(SeverityLevel.MINOR)
        public void createUserEmptyPasswordFieldTest() {
            String emptyPassword = "";

            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, randomEmail, emptyPassword);

            String message = createUserPage.getPasswordValidationMessage();

            Assert.assertEquals(message, EMPTY_FIELD_ERROR.getErrorMessage(),
                    "Error message for empty password field is not correct. " +
                            "Expected 'Please fill out this field.', but found:" + message);
        }

        @Test(description = "Unsuccessful user creation with invalid password format")
        @Description("Unsuccessful creation of a user due to invalid password format")
        @Story("Create New User")
        @Severity(SeverityLevel.NORMAL)
        public void createUserInvalidPasswordFormatErrorTest() {
            String invalidPassword = "abc";

            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, randomEmail, invalidPassword);

            List<String> errorMessages = createUserPage.getErrorMessages();

            Assert.assertTrue(errorMessages.contains(PASSWORD_TOO_SHORT.getErrorMessage()),
                    "Expected error message '" + PASSWORD_TOO_SHORT.getErrorMessage() + "' not found. "
                            + "Actual errors: " + errorMessages);
            Assert.assertTrue(errorMessages.contains(PASSWORD_REQUIRES_NON_ALPHANUMERIC.getErrorMessage()),
                    "Expected error message '" + PASSWORD_REQUIRES_NON_ALPHANUMERIC.getErrorMessage()
                            + "' not found. Actual errors: " + errorMessages);
            Assert.assertTrue(errorMessages.contains(PASSWORD_REQUIRES_DIGIT.getErrorMessage()),
                    "Expected error message '" + PASSWORD_REQUIRES_DIGIT.getErrorMessage()
                            + "' not found. Actual errors: " + errorMessages);
            Assert.assertTrue(errorMessages.contains(PASSWORD_REQUIRES_UPPER.getErrorMessage()),
                    "Expected error message '" + PASSWORD_REQUIRES_UPPER.getErrorMessage()
                            + "' not found. Actual errors: " + errorMessages);
        }

        @Test(description = "Unsuccessful user creation with duplicate email")
        @Description("Unsuccessful creation of a user due to duplicate email")
        @Story("Create New User")
        @Severity(SeverityLevel.NORMAL)
        public void createUserDuplicateEmailErrorTest() {
            String newRandomName = "User_" + RandomString.make(5);

            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, randomEmail, Constants.PASSWORD);

            registeredUsers.add(randomEmail);

            //No other way to handle the revisit of the site, due to the state and the way webdriver interacts with the page.
            createUserPage.waitMs();

            createUserPage.createUser(newRandomName, randomEmail, Constants.PASSWORD);

            List<String> errorMessages = createUserPage.getErrorMessages();
            String expectedEmailError = String.format(DUPLICATE_EMAIL.getErrorMessage(), randomEmail);

            Assert.assertTrue(errorMessages.contains(expectedEmailError),
                    "Expected email error message '" + expectedEmailError
                            + "' not found. Actual error: " + errorMessages);
            Assert.assertEquals( errorMessages.size(), expectedNumberOfErrors,
                    "Expected exactly 1 error message (email), but found "
                            + errorMessages.size() + ": " + errorMessages);
        }

        @Test(description = "Unsuccessful creation of a user due to duplicate username")
        @Description("Unsuccessful creation of a user due to duplicate username")
        @Story("Create New User")
        @Severity(SeverityLevel.NORMAL)
        public void createUserDuplicateNameError() {
            String newSecondEmail = "test" + RandomString.make(5) + "@test.com";

            LoginPage loginPage = loadPhotoPixelsApp();

            OverviewPage overviewPage = loginPage.login(adminEmail, adminPassword);

            CreateUserPage createUserPage = overviewPage.goToCreateNewUser();
            createUserPage.createUser(randomName, randomEmail, Constants.PASSWORD);

            registeredUsers.add(randomEmail);

            //No other way to handle the revisit of the site, due to the state and the way webdriver interacts with the page.
            createUserPage.waitMs();

            createUserPage.createUser(randomName, newSecondEmail, Constants.PASSWORD);

            List<String> errorMessages = createUserPage.getErrorMessages();
            String expectedUsernameError = String.format(DUPLICATE_USER_NAME.getErrorMessage(), randomName);

            Assert.assertTrue(errorMessages.contains(expectedUsernameError),
                    "Expected username error message '" + expectedUsernameError
                            + "' not found. Actual error: " + errorMessages);
            Assert.assertEquals(errorMessages.size(), expectedNumberOfErrors,
                    "Expected exactly 1 error message (username), but found "
                            + errorMessages.size() + ": " + errorMessages);
        }
    }