package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;

import static com.photopixels.constants.BasePathsConstants.POST_UPLOAD_PHOTO;

public class PostUploadObjectSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostUploadObjectSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_UPLOAD_PHOTO);
        requestSpecification.setContentType(ContentType.MULTIPART);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Upload object")
    public UploadObjectResponseDto uploadObject(String fileName, String objectHash) {
        Response response = uploadObjectResponse(fileName, objectHash);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(UploadObjectResponseDto.class);
    }

    @Step("Upload object with error response")
    public ErrorResponseDto uploadObjectError(String fileName, String objectHash, int statusCode) {
        Response response = uploadObjectResponse(fileName, objectHash);

        response.then().statusCode(statusCode);

        return response.as(ErrorResponseDto.class);
    }

    private Response uploadObjectResponse(String fileName, String objectHash) {

        if(fileName != null) {
            File uploadFile = new File(fileName);

            requestSpecification.getFilterableRequestSpecification()
                    .multiPart("File", uploadFile, "multipart/form-data");
        }

        if (objectHash != null) {
            requestSpecification.getFilterableRequestSpecification()
                    .multiPart("ObjectHash", objectHash, "multipart/form-data");
        }

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
