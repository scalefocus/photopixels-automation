package com.photopixels.api.steps.tus;

import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.config;
import static io.restassured.config.EncoderConfig.encoderConfig;


public class DeleteSendDataSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public DeleteSendDataSteps(String token) {
        this.requestOperationsHelper = new RequestOperationsHelper();
        this.requestSpecification = new CustomRequestSpecification();
        requestSpecification.setRelaxedHttpsValidation();
        requestSpecification.addCustomHeader("Authorization", token);
    }

    private Response deleteUploadFileIdResponse(String fileId) {
        config = config().encoderConfig(encoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false));
        // Dynamically build base path
        String basePath = (fileId == null || fileId.isEmpty())
                ? "/send_data"
                : "/send_data/" + fileId;

        requestSpecification.addBasePath(basePath);
        requestSpecification.addCustomHeader("Tus-Resumable", "1.0.0");

        return requestOperationsHelper.sendDeleteRequest(requestSpecification.getFilterableRequestSpecification());
    }

    @Step("Delete file with ID: {fileId}")
    public void deleteFileById(String fileId) {
        Response response = deleteUploadFileIdResponse(fileId);
        response.then().statusCode(HttpStatus.SC_OK);
    }

    @Step("Delete Upload FileId Error")
    //TODO once issue https://github.com/scalefocus/photopixels-backend-net/issues/92 is fixed with delete we need to refactor and include the error message in the response
    public void deleteFileExpectingError(String fileId, int expectedStatusCode) {
        Response response = deleteUploadFileIdResponse(fileId);
        response.then().statusCode(expectedStatusCode);
    }

}
