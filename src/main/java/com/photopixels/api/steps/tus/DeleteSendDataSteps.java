package com.photopixels.api.steps.tus;

import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.DELETE_SEND_DATA;

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

    private Response deleteUploadFileIdResponse(String fileId) {
        if (fileId != null && !fileId.isBlank()) {
            requestSpecification.addPathParams(Map.of("fileId", fileId));
        } else {
            // Remove the {fileId} segment manually
            requestSpecification.addBasePath("send_data");
        }
        requestSpecification.addCustomHeader("Tus-Resumable", "1.0.0");

        return requestOperationsHelper.sendDeleteRequest(requestSpecification.getFilterableRequestSpecification());
    }

    @Step("Delete file with ID: {fileId}")
    public void deleteFileById(String fileId) {
        Response response = deleteUploadFileIdResponse(fileId);
        response.then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Step("Delete Upload FileId Error")
    public String deleteFileExpectingError(String fileId, int expectedStatusCode) {
        Response response = deleteUploadFileIdResponse(fileId);
        response.then().statusCode(expectedStatusCode);

        return response.asString();
    }

}
