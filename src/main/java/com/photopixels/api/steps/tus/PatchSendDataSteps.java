package com.photopixels.api.steps.tus;

import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.PATCH_SEND_DATA;
import static io.restassured.RestAssured.config;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.apache.hc.core5.http.HttpStatus.SC_NO_CONTENT;

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

    public void sendDataLocationId(String locationId) {
        requestSpecification.addPathParams(Map.of("locationId", locationId));
    }

    private Response sendDataHeaders(String locationId,
                                     long uploadOffset,
                                     String uploadMetadata,
                                     long contentLength,
                                     File filePart) {
        // Add the location ID to the request path
        requestSpecification.addPathParams(Map.of("locationId", locationId)); // Ensure this is set

        config = config().encoderConfig(encoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false));

        // Add the required headers for the request
        requestSpecification.addCustomHeader("Tus-Resumable", "1.0.0");
        requestSpecification.addCustomHeader("Upload-Offset", String.valueOf(uploadOffset));
        requestSpecification.addCustomHeader("Upload-Metadata", uploadMetadata);
        requestSpecification.addCustomHeader("Content-Type", "application/offset+octet-stream");

        // Send the PATCH request and return the response
        return requestOperationsHelper
                .sendPatchRequest(requestSpecification.getFilterableRequestSpecification());
    }

    @Step("Send file chunk to Upload ID: {locationId} at offset: {uploadOffset}")
    public Response sendFileChunk(String locationId,
                                  long uploadOffset,
                                  String uploadMetadata,
                                  long contentLength,
                                  File filePart) {
        sendDataLocationId(locationId); // Add location ID path param

        // Send the file chunk and capture the response
        Response response = sendDataHeaders(locationId, uploadOffset, uploadMetadata, contentLength, filePart);
        System.out.println(response);
        response.then().statusCode(SC_NO_CONTENT);

        return response;
    }
}




