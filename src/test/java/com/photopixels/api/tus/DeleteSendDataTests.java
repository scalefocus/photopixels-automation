package com.photopixels.api.tus;

import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.tus.DeleteSendDataSteps;
import com.photopixels.api.steps.tus.PostCreateUploadSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.helpers.FileInfoExtractor;
import com.photopixels.helpers.SplitBinaryImage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.photopixels.constants.Constants.*;
import static com.photopixels.constants.Constants.PART2_SUFFIX;
import static com.photopixels.enums.UserRolesEnum.ADMIN;

public class DeleteSendDataTests extends ApiBaseTest {
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
    private String invalidFileId = "d32dbc6c-9fac-000c-b00b-eb2ae237b00";
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

    @Test(description = "Successfully delete send data file id")
    @Description("Positive Test: Verify that file is deleted successfully")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteSendDataFileIdSuccessfully() {
        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(token);
        // the response returns HTTP 204 No Content, meaning the delete is succeeded.
        // Therefore, no additional assertions are needed in this test method.
        deleteFileById.deleteFileById(uploadLocationId);
    }

    @Test(description = "Attempt to delete file without providing a file ID (null)")
    @Description("Negative Test: Verify that API returns error when file ID is missing in the DELETE request")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.NORMAL)
    public void deleteSendDataFileIdNotFound() {
        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(token);
        // the response returns HTTP 404 FileId Not Found
        // Therefore, no additional assertions are needed in this test method.
        deleteFileById.deleteFileExpectingError(null);
    }

    @Test(description = "Attempt to delete file with an invalid/nonexistent file ID")
    @Description("Negative Test: Verify that API returns error when file ID does not exist")
    @Story("Delete Send Data")
    @Severity(SeverityLevel.NORMAL)
    public void deleteSendDataInvalidFileId() {
        DeleteSendDataSteps deleteFileById = new DeleteSendDataSteps(token);
        // the response returns HTTP 404 FileId Not Found
        // Therefore, no additional assertions are needed in this test method.

        // Currently we have a bug task https://github.com/scalefocus/photopixels/issues/112 !!!
        deleteFileById.deleteFileExpectingError(invalidFileId);
    }
}
