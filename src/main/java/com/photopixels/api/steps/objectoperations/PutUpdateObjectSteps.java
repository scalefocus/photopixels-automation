package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.UpdateObjectRequestDto;
import com.photopixels.api.dtos.objectoperations.UpdateObjectResponseDto;
import com.photopixels.api.factories.objectoperations.UpdateObjectFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.PUT_UPDATE_OBJECT;

public class PutUpdateObjectSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PutUpdateObjectSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(PUT_UPDATE_OBJECT);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Update object")
    public UpdateObjectResponseDto updateObject(String objectId, String appleCloudId, String androidCloudId) {
        Response response = updateObjectResponse(objectId, appleCloudId, androidCloudId);

        response.then().statusCode(HttpStatus.SC_OK);
        return response.as(UpdateObjectResponseDto.class);
    }

    @Step("Update object with error response")
    public ErrorResponseDto updateObjectError(String objectId, String appleCloudId, String androidCloudId) {
        Response response = updateObjectResponse(objectId, appleCloudId, androidCloudId);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);

        return response.as(ErrorResponseDto.class);
    }

    private Response updateObjectResponse(String objectId, String appleCloudId, String androidCloudId) {
        UpdateObjectFactory updateObjectFactory = new UpdateObjectFactory();
        UpdateObjectRequestDto updateObjectRequestDto = updateObjectFactory.
                updateObjectRequestDto(appleCloudId, androidCloudId);

        requestSpecification.addBodyToRequest(updateObjectRequestDto);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("Id", objectId);

        requestSpecification.addPathParams(pathParams);

        return requestOperationsHelper
                .sendPutRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
