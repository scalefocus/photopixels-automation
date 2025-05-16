package com.photopixels.api.steps.tus;

import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.PATCH_SEND_DATA;
import static io.restassured.RestAssured.config;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class PatchSendDataSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PatchSendDataSteps(String token) {
        this.requestOperationsHelper = new RequestOperationsHelper();
        this.requestSpecification = new CustomRequestSpecification();
        requestSpecification.addBasePath(PATCH_SEND_DATA);
        requestSpecification.setRelaxedHttpsValidation();
        requestSpecification.addCustomHeader("Authorization", token);
    }

    public void sendDataLocationFileId(String fileId) {
        requestSpecification.addPathParams(Map.of("fileId", fileId));
    }

    private Response sendFileChunkWithHeaders(String fileId,
                                     long uploadOffset,
                                     String uploadMetadata,
                                     File filePart) {

        // Configure encoder to avoid appending charset to content type
        config = config().encoderConfig(encoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false));

        // Set required TUS headers
        requestSpecification.addPathParams(Map.of("fileId", fileId));
        requestSpecification.addCustomHeader("Tus-Resumable", "1.0.0");
        requestSpecification.addCustomHeader("Upload-Offset", String.valueOf(uploadOffset));
        requestSpecification.addCustomHeader("Upload-Metadata", uploadMetadata);
        requestSpecification.addCustomHeader("Content-Type", "application/offset+octet-stream");

        // Set binary file part as raw stream in body
        try {
            requestSpecification.setRequestBodyStream(new FileInputStream(filePart));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + filePart.getAbsolutePath(), e);
        }

        // Send PATCH request
        return requestOperationsHelper.sendPatchRequest(requestSpecification.getFilterableRequestSpecification());
    }

    @Step("Send file chunk to Upload ID: {locationFileId} at offset: {uploadOffset}")
    public void sendFileChunk(String fileId,
                                  long uploadOffset,
                                  String uploadMetadata,
                                  File filePart) {
        sendDataLocationFileId(fileId);

        Response response = sendFileChunkWithHeaders(fileId, uploadOffset, uploadMetadata, filePart);
        response.then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Step("Send file chunk with error response")
    public Response sendFileChunkError(String fileId,
                                               long uploadOffset,
                                               String uploadMetadata,
                                               File filePart) {
        sendDataLocationFileId(fileId);

        Response response = sendFileChunkWithHeaders(fileId, uploadOffset, uploadMetadata, filePart);
        response.then().statusCode(HttpStatus.SC_CONFLICT);

        return response;
    }

}
