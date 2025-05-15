package com.photopixels.api.tus;

import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.tus.PatchSendDataSteps;
import com.photopixels.api.steps.tus.PostCreateUploadSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.helpers.FileInfoExtractor;
import com.photopixels.helpers.SplitBinaryImage;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.photopixels.constants.Constants.*;
import static com.photopixels.enums.UserRolesEnum.ADMIN;


public class PatchSendDataTests extends ApiBaseTest {

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


    @BeforeClass(alwaysRun = true)
    public void setup() throws IOException {
        // 1. Create a random photopixel user
        String random = RandomStringUtils.randomNumeric(6);
        name = "Test User" + random;
        email = "testuser" + random + "@test.com";
        password = PASSWORD;

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
        part1Image = UPLOAD_FILES_DIR.resolve(baseName + "_part1.jpg");
        part2Image = UPLOAD_FILES_DIR.resolve(baseName + "_part2.jpg");

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

        // 3.2 Set Upload-Length using fileSizeBytes from fileInfo, and keep it as a String
        uploadLength = fileInfo.get("fileSizeBytes");

        // 4. Create Upload Tus
        PostCreateUploadSteps steps = new PostCreateUploadSteps(token);
        String fullUploadLocation = steps.createUploadAndGetLocationFileId(uploadMetadata, uploadLength);
        uploadLocationId = fullUploadLocation.substring(fullUploadLocation.lastIndexOf('/') + 1);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsersAdmin(registeredUsersList);
    }

    @Test
    public void sendDataFileIdSuccessfully() {
        PatchSendDataSteps sendDataSteps = new PatchSendDataSteps(token);
        // send Image Part 1
        Response response = sendDataSteps.sendFileChunk(
                uploadLocationId,
                uploadOffset,
                uploadMetadata,
                part1Image.toFile()

        );
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Expected 204 No Content");
        // send Image part 2
        Response responsePart2  = sendDataSteps.sendFileChunk(
                uploadLocationId,
                part2ImageSize,
                uploadMetadata,
                part2Image.toFile()
        );
        softAssert.assertEquals(responsePart2.getStatusCode(), HttpStatus.SC_NO_CONTENT, "Expected 204 No Content");
        softAssert.assertAll();
    }

}
