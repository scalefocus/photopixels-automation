package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.api.steps.users.PostRegisterUserSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.Constants.*;
import static com.photopixels.constants.ErrorMessageConstants.CONFLICT_ERROR;
import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class PostUploadObjectTests extends ApiBaseTest {

    private String token;
    private String objectHash;
    private String filePath = UNNAMED_FILE;

    private Map<String, String> registeredUsersList = new HashMap<>();

    @BeforeClass(alwaysRun = true)
    public void setup() {
        objectHash = getObjectHash(filePath);

        String random = RandomStringUtils.randomNumeric(6);
        String name = "TestUser" + random;
        String email = "testuser" + random + "@test.com";

        PostRegisterUserSteps postRegisterUserSteps = new PostRegisterUserSteps();
        postRegisterUserSteps.registerUser(name, email, PASSWORD);

        registeredUsersList.put(email, PASSWORD);

        token = getToken(email, PASSWORD);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsers(registeredUsersList);
    }

    @Test(dataProvider = "files", description = "Upload photo object")
    @Description("Successful upload of photo object")
    @Story("Upload Object")
    @Severity(SeverityLevel.CRITICAL)
    public void uploadObjectTest(String filePath) {
        String photoObjectHash = getObjectHash(filePath);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                .uploadObject(filePath, photoObjectHash);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(uploadObjectResponseDto.getId(), "Id is missing");
        softAssert.assertTrue(uploadObjectResponseDto.getRevision() > 0, "Revision is missing");
        softAssert.assertNotNull(uploadObjectResponseDto.getQuota(), "Quota is missing");
        softAssert.assertNotNull(uploadObjectResponseDto.getUsedQuota(), "Used quota is missing");

        softAssert.assertAll();
    }

    @Test(description = "Upload object duplicate file")
    @Description("Upload object with duplicate file")
    @Story("Upload Object")
    @Severity(SeverityLevel.NORMAL)
    public void uploadObjectDuplicateFileTest() {
        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        postUploadObjectSteps.uploadObject(filePath, objectHash);

        // Upload the same file
        ErrorResponseDto errorResponseDto = postUploadObjectSteps
                .uploadObjectError(filePath, objectHash, HttpStatus.SC_CONFLICT);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), CONFLICT_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_CONFLICT, "Error status is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Upload object no file")
    @Description("Upload object with missing file")
    @Story("Upload Object")
    @Severity(SeverityLevel.MINOR)
    public void uploadObjectNoFileTest() {
        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        ErrorResponseDto errorResponseDto = postUploadObjectSteps.uploadObjectError(null, objectHash, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.FILE.getKey()),
                ErrorMessagesEnum.FILE.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Upload object no object hash")
    @Description("Upload object with missing object hash")
    @Story("Upload Object")
    @Severity(SeverityLevel.MINOR)
    public void uploadObjectNoObjectHashTest() {
        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        ErrorResponseDto errorResponseDto = postUploadObjectSteps.uploadObjectError(filePath, null, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.OBJECT_HASH.getKey()),
                ErrorMessagesEnum.OBJECT_HASH.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Upload object with invalid object hash")
    @Description("Upload image or video with invalid object hash - should return 400 Bad Request")
    @Story("Upload Object")
    @Severity(SeverityLevel.MINOR)
    public void uploadObjectInvalidObjectHashTest() {

        String invalidObjectHash = "invalidHash";
        String expectedErrorMessage = ErrorMessagesEnum.OBJECT_HASH_NOT_MATCH.getErrorMessage();

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        ErrorResponseDto errorResponseDto = postUploadObjectSteps
                .uploadObjectError(filePath, invalidObjectHash, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        // TODO Missing fields in the error response
//        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
//        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.OBJECT_HASH_NOT_MATCH.getKey()), expectedErrorMessage,
                "Error message is not correct");

        softAssert.assertAll();
    }

}
