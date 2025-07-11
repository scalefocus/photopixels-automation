package com.photopixels.api.tus;

import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.tus.PatchSendDataSteps;
import com.photopixels.api.steps.tus.PostCreateUploadSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.helpers.FileInfoExtractor;
import com.photopixels.helpers.SplitBinaryImage;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.photopixels.constants.Constants.*;
import static com.photopixels.constants.ErrorMessageConstants.*;
import static com.photopixels.enums.UserRolesEnum.ADMIN;

@Listeners(StatusTestListener.class)
@Feature("Tus")
public class PatchSendDataTests implements IApiBaseTest {

    private String name;
    private String email;
    private String password;
    private String token;
    private Map<String, String> fileInfo;
    private List<String> registeredUsersList = new ArrayList<>();
    private Path part1Image;
    private Path part2Image;
    private String uploadMetadata;
    private String uploadLength; // Changed to String
    private long part1ImageSize;
    private long part2ImageSize;
    private long uploadOffset = 0;
    private String uploadLocationId;

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

            // 2 Generate dynamic split file names
            Path originalImage = Path.of(COCTAIL_FILE);
            String baseName = removeExtension(originalImage.getFileName().toString());
            part1Image = Path.of(FILE_LOCATION + baseName + PART1_SUFFIX);
            part2Image = Path.of(FILE_LOCATION + baseName + PART2_SUFFIX);


            // 2.1. Split the image into two parts
            SplitBinaryImage splitter = new SplitBinaryImage();
            splitter.splitBinaryFile(
                    originalImage.toString(),
                    part1Image.toString(),
                    part2Image.toString()
            );

            // 3. Extract file info
            FileInfoExtractor extractor = new FileInfoExtractor();
            fileInfo = extractor.extractFileInfo(originalImage);
            part1ImageSize = extractor.getFileSize(part1Image);
            part2ImageSize = extractor.getFileSize(part2Image);

            // 3.1 Build dynamic Upload-Metadata
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

    @Test(description = "Successfully uploads file in multiple chunks")
    @Description("Positive Test: Verify that file chunks are successfully uploaded")
    @Story("Upload Chunks")
    @Severity(SeverityLevel.CRITICAL)
    public void sendDataFileIdSuccessfully() {
        PatchSendDataSteps sendDataSteps = new PatchSendDataSteps(token);
        // Each call to sendFileChunk includes an internal assertion that verifies
        // the response returns HTTP 204 No Content, meaning the chunk upload succeeded.
        // Therefore, no additional assertions are needed in this test method.

        // send Image Part 1
        sendDataSteps.sendFileChunk(
                uploadLocationId,
                uploadOffset,
                uploadMetadata,
                part1Image.toFile()
        );
        // send Image part 2
        sendDataSteps.sendFileChunk(
                uploadLocationId,
                part2ImageSize,
                uploadMetadata,
                part2Image.toFile()
        );
    }

    @Test(description = "Fails to upload file chunk due to incorrect upload offset")
    @Description("Negative Test: Verify that the server responds with a conflict error when a chunk is uploaded with a mismatched byte offset")
    @Story("Upload Chunks")
    @Severity(SeverityLevel.NORMAL)
    public void sendDataFileIdSConflictOffsetError() {
        PatchSendDataSteps sendDataSteps = new PatchSendDataSteps(token);
        // send Image Part 1 with correct upload offset
        sendDataSteps.sendFileChunk(
                uploadLocationId,
                uploadOffset,
                uploadMetadata,
                part1Image.toFile()
        );
        // send Image part 2 with wrong upload offset
        String responseBody = sendDataSteps.sendFileChunkErrorWithExpectedStatus(
                uploadLocationId,
                5000L,
                uploadMetadata,
                part2Image.toFile(),
                HttpStatus.SC_CONFLICT
        );

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(responseBody.contains(CONFLICT_BYTE_OFFSET),
                "Expected conflict message not found in response body");
        softAssert.assertAll();
    }

    @Test(description = "Fails to upload file chunk due to Missing Upload-Offset header")
    @Description("Negative Test: Verify that the server responds with a Bad Request Missing Upload-Offset header")
    @Story("Upload Chunks")
    @Severity(SeverityLevel.NORMAL)
    public void sendDataFileIdSMissingUploadOffsetHeader() {
        PatchSendDataSteps sendDataSteps = new PatchSendDataSteps(token);

        // send Image part 1 without upload offset
        String responseBody = sendDataSteps.sendFileChunkErrorWithExpectedStatus(
                uploadLocationId,
                null,
                uploadMetadata,
                part1Image.toFile(),
                HttpStatus.SC_BAD_REQUEST
        );

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(responseBody, MISSING_UPLOAD_OFFSET_ERROR,
                "Expected conflict message not found in response body");
        softAssert.assertAll();
    }
}
