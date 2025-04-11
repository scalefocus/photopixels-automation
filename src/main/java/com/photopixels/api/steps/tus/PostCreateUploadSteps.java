package com.photopixels.api.steps.tus;

import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.CREATE_UPLOAD;

public class PostCreateUploadSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;


    public PostCreateUploadSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(CREATE_UPLOAD);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Create upload resource")
    public Response createUpload(String uploadMetadata, String uploadLength) {
        Response response = createUploadResponse(uploadMetadata, uploadLength);

        response.then().statusCode(HttpStatus.SC_CREATED);

        return response;
    }

    @Step("Create upload resource with error response")
    public Response createUploadError(String uploadMetadata, String uploadLength, int expectedStatusCode) {
        Response response = createUploadResponse(uploadMetadata, uploadLength);

        response.then().statusCode(expectedStatusCode);

        return response;
    }

    private Response createUploadResponse(String uploadMetadata, String uploadLength) {
        requestSpecification.addCustomHeader("Upload-Metadata", uploadMetadata);
        requestSpecification.addCustomHeader("Upload-Length", uploadLength);
        requestSpecification.addCustomHeader("Tus-Resumable", "1.0.0");

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}

