package com.photopixels.api.tus;

import com.photopixels.api.dtos.tus.ResumableUploadsResponseDto;
import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.tus.DeleteSendDataSteps;
import com.photopixels.api.steps.tus.GetResumableUploadsSteps;
import com.photopixels.api.steps.tus.PostCreateUploadSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.helpers.FileInfoExtractor;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.photopixels.constants.Constants.*;
import static com.photopixels.enums.UserRolesEnum.ADMIN;

@Listeners(StatusTestListener.class)
@Feature("Tus")
public class DeleteSendDataTests implements IApiBaseTest {
    private String name;
    private String email;
    private String password;
    private String token;
    private Map<String, String> fileCoctailImageInfo, fileFrenchImageInfo;
    private List<String> registeredUsersList = new ArrayList<>();
    private String uploadCoctailMetadata;
    private String uploadFrenchMetadata;
    private String uploadCoctailLength;
    private String uploadFrenchLength;// Changed to String
    private long uploadOffset = 0;
    private String invalidFileId = "d32dbc6c-9fac-000c-b00b-eb2ae237b00";
    private String uploadCoctailLocationId;
    private String uploadFrenchLocationId;
    private String userTokenB;
    private String nameB = "Mike";
    private String emailB = "testphotopixels@gmail.com";
    private String passwordB = "Test!@#123";

    @BeforeClass(alwaysRun = true)
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
            Path originaCoctailImage = Path.of(COCTAIL_FILE);
            Path originalFrenchFriesImage = Path.of(FRENCH_FRIES_FILE);
            FileInfoExtractor extractor = new FileInfoExtractor();
            fileCoctailImageInfo = extractor.extractFileInfo(originaCoctailImage);
            fileFrenchImageInfo = extractor.extractFileInfo(originalFrenchFriesImage);

            // 2.1 Build dynamic Upload-Metadata
            uploadCoctailMetadata = String.format(
                    "fileExtension %s,fileName %s,fileHash %s,fileSize %s,appleId ,androidId",
                    fileCoctailImageInfo.get("fileExtension"),
                    fileCoctailImageInfo.get("fileName"),
                    fileCoctailImageInfo.get("fileHashBase64"),
                    fileCoctailImageInfo.get("fileSize")
            );
            uploadFrenchMetadata = String.format(
                    "fileExtension %s,fileName %s,fileHash %s,fileSize %s,appleId ,androidId",
                    fileFrenchImageInfo.get("fileExtension"),
                    fileFrenchImageInfo.get("fileName"),
                    fileFrenchImageInfo.get("fileHashBase64"),
                    fileFrenchImageInfo.get("fileSize")
            );

            // 3.2 Set Upload-Length using fileSizeBytes from fileInfo
            uploadCoctailLength = fileCoctailImageInfo.get("fileSizeBytes");
            uploadFrenchLength = fileFrenchImageInfo.get("fileSizeBytes");
            // 4. Create Upload Tus
            PostCreateUploadSteps steps = new PostCreateUploadSteps(token);
            String fullUploadLocation = steps.createUploadAndGetLocationFileId(uploadCoctailMetadata, uploadCoctailLength);
            Assert.assertNotNull(fullUploadLocation, "'Location' header is missing in the upload response.");
            uploadCoctailLocationId = fullUploadLocation.substring(fullUploadLocation.lastIndexOf('/') + 1);
            String fullUploadLocationFrench = steps.createUploadAndGetLocationFileId(uploadFrenchMetadata, uploadFrenchLength);
            Assert.assertNotNull(fullUploadLocationFrench, "'Location' header is missing in the upload response.");
            uploadFrenchLocationId = fullUploadLocationFrench.substring(fullUploadLocation.lastIndexOf('/') + 1);

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
        GetResumableUploadsSteps getResumableUploadsSteps = new GetResumableUploadsSteps(token);
        ResumableUploadsResponseDto responseBefore = getResumableUploadsSteps.getResumableUploads();
        // Step 1: Verify file ID is present before deletion
        boolean isPresentBefore = responseBefore.getUserUploads().stream()
                .anyMatch(upload -> uploadCoctailLocationId.equals(upload.getFileId()));

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(isPresentBefore, "Expected file ID to be present before deletion");

        // Step 2: Perform file deletion
        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(token);

        deleteFileById.deleteFileById(uploadCoctailLocationId);

        // Step 3: Verify file ID is no longer present after deletion
        ResumableUploadsResponseDto responseAfter = getResumableUploadsSteps.getResumableUploads();
        boolean isStillPresent = responseAfter.getUserUploads().stream()
                .anyMatch(upload -> uploadCoctailLocationId.equals(upload.getFileId()));

        softAssert.assertFalse(isStillPresent, "Expected file ID to be not present after deletion");

        softAssert.assertAll();
    }

    @Test(description = "Delete file with an invalid/nonexistent file ID")
    @Description("Negative Test: Verify that API returns error when file ID does not exist")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.NORMAL)
    public void deleteSendDataFileIdInvalidId() {
        GetResumableUploadsSteps getResumableUploadsSteps = new GetResumableUploadsSteps(token);
        ResumableUploadsResponseDto responseBefore = getResumableUploadsSteps.getResumableUploads();
        // Step 1: Verify file ID Does Not Exist for the user
        boolean isPresentBefore = responseBefore.getUserUploads().stream()
                .anyMatch(upload -> invalidFileId.equals(upload.getFileId()));

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertFalse(isPresentBefore, "Expected file ID to not be present");

        // TODO: Remove when issue is fixed
        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/170");

        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(token);
        String responseBody = deleteFileById.deleteFileExpectingError(
                invalidFileId,
                HttpStatus.SC_NOT_FOUND
        );

        softAssert.assertEquals(responseBody, "[\"The object has already been deleted\"]", "Unexpected error message");

        softAssert.assertAll();
    }

    @Test(description = "Delete a file created by a different user")
    @Description("Negative Test: Verify that API returns 403 Forbidden when trying to delete a file uploaded by another user")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteSendDataUnauthorizedFileDeletion() {

        // Step 1: create user B
        String adminToken = getAdminToken();
        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(adminToken);
        postRegisterUserAdminSteps.registerUserAdmin(nameB, emailB, passwordB, ADMIN);
        registeredUsersList.add(emailB);
        // Login with the newly created user B to get the token
        userTokenB = getToken(emailB, passwordB);

        GetResumableUploadsSteps getResumableUploadsSteps = new GetResumableUploadsSteps(token);
        ResumableUploadsResponseDto responseBefore = getResumableUploadsSteps.getResumableUploads();
        // Step 2: Verify file ID is present before deletion
        boolean isPresentBefore = responseBefore.getUserUploads().stream()
                .anyMatch(upload -> uploadFrenchLocationId.equals(upload.getFileId()));

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(isPresentBefore, "Expected file ID to be present before deletion");

        // Step 3: Attempt deletion with User B (who does not own the file)
        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(userTokenB);

        deleteFileById.deleteFileExpectingError( uploadFrenchLocationId, HttpStatus.SC_FORBIDDEN);

        // Step 4: Verify the file is still present for the original (authorized) user after the unauthorized deletion attempt
        boolean isStillPresent = responseBefore.getUserUploads().stream()
                .anyMatch(upload -> uploadFrenchLocationId.equals(upload.getFileId()));

        softAssert.assertTrue(isStillPresent, "Expected file ID to be present");

        softAssert.assertAll();
    }
}
