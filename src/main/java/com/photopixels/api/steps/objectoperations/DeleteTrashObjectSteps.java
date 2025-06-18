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

import static com.photopixels.constants.BasePathsConstants.DELETE_TRASH_OBJECT;

public class DeleteTrashObjectSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public  DeleteTrashObjectSteps (String token) {

        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(DELETE_TRASH_OBJECT);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Trash object with valid id: {objectId}")
    public ObjectVersioningResponseDto deleteTrashObject(String objectId) {
        Response response = deleteTrashObjectResponse(objectId);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(ObjectVersioningResponseDto.class);
    }


    @Step("Trash object with expected error for id: {objectId}")
    public ErrorResponseDto deleteTrashObjectExpectingError(String objectId) {
        Response response = deleteTrashObjectResponse(objectId);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);

        return response.as(ErrorResponseDto.class);
    }

    private Response deleteTrashObjectResponse(String objectId) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("objectid", objectId);

        requestSpecification.addPathParams(pathParams);

        return requestOperationsHelper
                .sendDeleteRequest(requestSpecification.getFilterableRequestSpecification());
    }
}