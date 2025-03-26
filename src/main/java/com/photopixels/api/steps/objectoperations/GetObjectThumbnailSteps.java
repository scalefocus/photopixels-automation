package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.api.constants.BasePathsConstants.GET_OBJECT_THUMBNAIL;

public class GetObjectThumbnailSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetObjectThumbnailSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_OBJECT_THUMBNAIL);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get object thumbnail")
    public String getObjectThumbnail(String objectId) {
        Response response = getObjectThumbnailResponse(objectId);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.asString();
    }

    @Step("Get object thumbnail with error response")
    public ErrorResponseDto getObjectThumbnailError(String objectId) {
        Response response = getObjectThumbnailResponse(objectId);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);

        return response.as(ErrorResponseDto.class);
    }

    private Response getObjectThumbnailResponse(String objectId) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("Id", objectId);

        requestSpecification.addPathParams(pathParams);

        return requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
