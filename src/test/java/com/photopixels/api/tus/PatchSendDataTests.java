package com.photopixels.api.tus;

import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.tus.PatchSendDataSteps;
import com.photopixels.api.steps.tus.PostCreateUploadSteps;
import com.photopixels.api.steps.users.PostLoginSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.helpers.FileInfoExtractor;
import com.photopixels.helpers.SplitBinaryImage;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
    private String imagefilePath = COCTAIL_FILE;
    private Path part1Image;
    private Path part2Image;
    private String uploadMetadata;
    private String uploadLength; // Changed to String
    private long part1ImageSize;
    private long part2ImageSize;
    private long uploadOffset = 0;
    private String uploadLocationId;


    @BeforeClass(alwaysRun = true)
    public void setup() {
        // 1. Create a random photopixel user
        String random = RandomStringUtils.randomNumeric(6);
        name = "Test User" + random;
        email = "testuser" + random + "@test.com";
        password = "12345Qa!";

        // 1.2 Use admin token to create the random user
        String adminToken = getAdminToken();
        System.out.println("Using Admin token: " + adminToken);
        PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(adminToken);
        postRegisterUserAdminSteps.registerUserAdmin(name, email, password, ADMIN);
        registeredUsersList.add(email);

        // 1.3 Login with the newly created user to get the token
        PostLoginSteps loginSteps = new PostLoginSteps();
        LoginResponseDto loginResponseDto = loginSteps.login(email, password);
        token = "Bearer " + loginResponseDto.getAccessToken();

        // 2. Set the original image path
        Path originalImage = UPLOAD_FILES_DIR.resolve("coctail.jpg");

        // 2.1 Generate dynamic split file names
        String baseName = removeExtension(originalImage.getFileName().toString());
        part1Image = UPLOAD_FILES_DIR.resolve(baseName + "_part1.jpg");
        part2Image = UPLOAD_FILES_DIR.resolve(baseName + "_part2.jpg");

        // 2.2. Split the image into two parts
        try {
            SplitBinaryImage.splitBinaryFile(
                    originalImage.toString(),
                    part1Image.toString(),
                    part2Image.toString()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to split image: " + e.getMessage(), e);
        }

        // 3. Extract file info
        fileInfo = FileInfoExtractor.extractFileInfo(originalImage);
        part1ImageSize = getFileSize(part1Image);
        part2ImageSize = getFileSize(part2Image);

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
        String fullUploadLocation = steps.tus_CreateUpload_GetLocationId_Successfully(uploadMetadata, uploadLength);
        uploadLocationId = fullUploadLocation.substring(fullUploadLocation.lastIndexOf('/') + 1);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteRegisteredUsersAdmin(registeredUsersList);
    }

    @Test
    public void sendDataFileIdSuccessfully() {
        PatchSendDataSteps sendDataSteps = new PatchSendDataSteps(token);
        Response response = sendDataSteps.sendFileChunk(
                uploadLocationId,    // Pass the upload location ID (locationId)
                uploadOffset,        // Pass the current offset (uploadOffset)
                uploadMetadata,      // Pass the metadata for this chunk (uploadMetadata)
                part1ImageSize,      // Pass the size of the chunk (contentLength)
                part1Image.toFile()          // Pass the file chunk (filePart)
        );
        //note this is for the second image part!!!
//        sendDataSteps.sendFileChunk(
//                uploadLocationId,
//                part1ImageSize,
//                uploadMetadata,
//                part2ImageSize,
//                part2Image.toFile()
//        );
    }
}
