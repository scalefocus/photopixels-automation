package com.photopixels.api.tus;

import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.tus.DeleteSendDataSteps;
import com.photopixels.api.steps.tus.PostCreateUploadSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.helpers.FileInfoExtractor;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.photopixels.constants.Constants.*;
import static com.photopixels.enums.UserRolesEnum.ADMIN;

public class DeleteSendDataTests extends ApiBaseTest {
    private String name;
    private String email;
    private String password;
    private String token;
    private Map<String, String> fileInfo;
    private List<String> registeredUsersList = new ArrayList<>();
    private String uploadMetadata;
    private String uploadLength; // Changed to String
    private long uploadOffset = 0;
    private String invalidFileId = "d32dbc6c-9fac-000c-b00b-eb2ae237b00";
    private String uploadLocationId;
    private String userTokenB;
    private String nameB = "Mike";
    private String emailB = "testphotopixels@gmail.com";
    private String passwordB = "Test!@#123";

    @BeforeMethod(alwaysRun = true)
    public void setup() {

        // 1. Create a random photopixel user
        String random = RandomStringUtils.randomNumeric(6);
        name = "Testuser" + random;
        email = "testuser" + random + "@test.com";
        password = PASSWORD;

        try {
            // 1.2 Use admin token to create the random user
            String adminToken = getAdminToken();
            PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(adminToken);
            postRegisterUserAdminSteps.registerUserAdmin(name, email, password, ADMIN);
            registeredUsersList.add(email);

            // 1.3 Login with the newly created user to get the token
            token = getToken(email, password);

            // 2. Extract file info
            Path originalImage = Path.of(COCTAIL_FILE);
            FileInfoExtractor extractor = new FileInfoExtractor();
            fileInfo = extractor.extractFileInfo(originalImage);

            // 2.1 Build dynamic Upload-Metadata
            uploadMetadata = String.format(
                    "fileExtension %s,fileName %s,fileHash %s,fileSize %s,appleId ,androidId",
                    fileInfo.get("fileExtension"),
                    fileInfo.get("fileName"),
                    fileInfo.get("fileHashBase64"),
                    fileInfo.get("fileSize")
            );

            // 3.2 Set Upload-Length using fileSizeBytes from fileInfo
            uploadLength = fileInfo.get("fileSizeBytes");

            // 4. Create Upload Tus
            PostCreateUploadSteps steps = new PostCreateUploadSteps(token);
            String fullUploadLocation = steps.createUploadAndGetLocationFileId(uploadMetadata, uploadLength);
            Assert.assertNotNull(fullUploadLocation, "'Location' header is missing in the upload response.");
            Assert.assertFalse(fullUploadLocation.isEmpty(), "'Location' header is empty in the upload response.");
            uploadLocationId = fullUploadLocation.substring(fullUploadLocation.lastIndexOf('/') + 1);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Setup failed due to unexpected exception: " + e.getMessage());
        }
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsersAdmin(registeredUsersList);
    }

    @Test(description = "Successfully delete send data file id")
    @Description("Positive Test: Verify that file is deleted successfully")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteSendDataFileIdSuccessfully() {
        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(token);
        // the response returns HTTP 204 No Content, but swagger shown that the response status should be 200

        // Therefore, no additional assertions are needed in this test method.
        deleteFileById.deleteFileById(uploadLocationId);
    }

    @Test(description = "Attempt to delete file without providing a file ID (null)")
    @Description("Negative Test: Verify that API returns error when file ID is missing in the DELETE request")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.NORMAL)
    public void deleteSendDataFileIdNotFound() {
        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(token);
        deleteFileById.deleteFileExpectingError(
                null,
                HttpStatus.SC_NO_CONTENT
        );
    }

    @Test(description = "Attempt to delete file with an invalid/nonexistent file ID")
    @Description("Negative Test: Verify that API returns error when file ID does not exist")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.NORMAL)
    public void deleteSendDataInvalidFileId() {
        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(token);
        // Currently we have a bug task https://github.com/scalefocus/photopixels/issues/112
        // //https://github.com/scalefocus/photopixels-backend-net/issues/92 !!!
        deleteFileById.deleteFileExpectingError(
                invalidFileId,
                HttpStatus.SC_NOT_FOUND
        );
    }

    @Test(description = "Attempt to delete a file created by a different user")
    @Description("Negative Test: Verify that API returns 403 Forbidden when trying to delete a file uploaded by another user")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteSendDataUnauthorizedFileDeletion() {

        String adminToken = getAdminToken();
        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(adminToken);
        postRegisterUserAdminSteps.registerUserAdmin(nameB, emailB, passwordB, ADMIN);
        registeredUsersList.add(emailB);
        // Login with the newly created user B to get the token
        userTokenB = getToken(emailB, passwordB);

        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(userTokenB);
        // Currently we have a bug task https://github.com/scalefocus/photopixels/issues/140 !!!
        deleteFileById.deleteFileExpectingError(
                uploadLocationId,
                HttpStatus.SC_FORBIDDEN
        );
    }
}
