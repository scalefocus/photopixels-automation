package com.photopixels.api.steps.tus;

import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.DELETE_SEND_DATA;
import static io.restassured.RestAssured.config;
import static io.restassured.config.EncoderConfig.encoderConfig;


public class DeleteSendDataSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public DeleteSendDataSteps(String token) {
        this.requestOperationsHelper = new RequestOperationsHelper();
        this.requestSpecification = new CustomRequestSpecification();
        requestSpecification.addBasePath(DELETE_SEND_DATA);
        requestSpecification.setRelaxedHttpsValidation();
        requestSpecification.addCustomHeader("Authorization", token);
    }

    private Response sendFileIdWithHeaders(String fileId) {
        config = config().encoderConfig(encoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false));

        String basePath = DELETE_SEND_DATA;

        if (fileId == null || fileId.isBlank()) {
            basePath = basePath.replace("/{fileId}", "");
        }

        requestSpecification.addBasePath(basePath);

        if (fileId != null && !fileId.isBlank()) {
            requestSpecification.addPathParams(Map.of("fileId", fileId));
        }

        requestSpecification.addCustomHeader("Tus-Resumable", "1.0.0");

        return requestOperationsHelper.sendDeleteRequest(requestSpecification.getFilterableRequestSpecification());
    }

    @Step("Delete file with ID: {fileId}")
    public void deleteFileById(String fileId) {
        Response response = sendFileIdWithHeaders(fileId);
        response.then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Step("Attempt to delete file with file ID: {fileId}")
    public void deleteFileExpectingError(String fileId) {
        Response response = sendFileIdWithHeaders(fileId);

        if (fileId == null || fileId.isBlank()) {
            response.then().statusCode(HttpStatus.SC_NOT_FOUND);
        } else {
            response.then().statusCode(HttpStatus.SC_NOT_FOUND);
        }
    }

}
