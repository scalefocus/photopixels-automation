package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.DELETE_OBJECT;

public class DeleteObjectSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public DeleteObjectSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(DELETE_OBJECT);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Delete object")
    public ObjectVersioningResponseDto deleteObject(String objectId) {
        Response response = deleteObjectResponse(objectId);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(ObjectVersioningResponseDto.class);
    }

    @Step("Delete object with error response")
    public ErrorResponseDto deleteObjectError(String objectId) {
        Response response = deleteObjectResponse(objectId);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);

        return response.as(ErrorResponseDto.class);
    }

    private Response deleteObjectResponse(String objectId) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("Id", objectId);

        requestSpecification.addPathParams(pathParams);

        return requestOperationsHelper
                .sendDeleteRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
