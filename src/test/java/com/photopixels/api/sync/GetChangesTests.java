package com.photopixels.api.sync;

import com.photopixels.api.dtos.sync.GetChangesResponseDto;
import com.photopixels.api.steps.sync.GetChangesSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.Constants.PASSWORD;

@Listeners(StatusTestListener.class)
@Feature("Sync")
public class GetChangesTests extends ApiBaseTest {

    private String token;
    private Map<String, String> registeredUsersList = new HashMap<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsers(registeredUsersList);
    }

    @Test(description = "Get changes")
    @Description("Get changes")
    @Story("Get Changes")
    @Severity(SeverityLevel.NORMAL)
    public void getChangesTest() {
        String revisionId = "0";

        GetChangesSteps getChangesSteps = new GetChangesSteps(token);
        GetChangesResponseDto getChangesResponseDto = getChangesSteps.getChanges(revisionId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertFalse(getChangesResponseDto.getId().isEmpty(), "Id is not returned");
        softAssert.assertTrue(getChangesResponseDto.getVersion() > 0, "Version is not returned");
        softAssert.assertNotNull(getChangesResponseDto.getAdded(), "Added changes are not returned");

        softAssert.assertAll();
    }

    @Test(description = "Get changes not existing revision id")
    @Description("Get changes with not existing revision id")
    @Story("Get Changes")
    @Severity(SeverityLevel.MINOR)
    public void getChangesNotExistingRevisionIdTest() {
        String revisionId = RandomStringUtils.randomNumeric(6);

        GetChangesSteps getChangesSteps = new GetChangesSteps(token);
        GetChangesResponseDto getChangesResponseDto = getChangesSteps.getChanges(revisionId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(getChangesResponseDto.getAdded().isEmpty(), "Added changes are returned");
        softAssert.assertTrue(getChangesResponseDto.getDeleted().isEmpty(), "Deleted changes are returned");

        softAssert.assertAll();
    }

    @Test(description = "Get changes no uploads")
    @Description("Get changes for user without any uploads")
    @Story("Get Changes")
    @Severity(SeverityLevel.MINOR)
    public void getChangesNoUploadsTest() {
        String revisionId = "0";

        String random = RandomStringUtils.randomNumeric(6);
        String name = "TestUser" + random;
        String email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, PASSWORD);

        String token = getToken(email, PASSWORD);

        GetChangesSteps getChangesSteps = new GetChangesSteps(token);
        GetChangesResponseDto getChangesResponseDto = getChangesSteps.getChanges(revisionId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(getChangesResponseDto.getAdded().isEmpty(), "Added changes are returned");
        softAssert.assertTrue(getChangesResponseDto.getDeleted().isEmpty(), "Deleted changes are returned");

        softAssert.assertAll();
    }
}
