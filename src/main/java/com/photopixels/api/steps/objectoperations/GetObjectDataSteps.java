package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectDataResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.GET_OBJECT_DATA;

public class GetObjectDataSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetObjectDataSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_OBJECT_DATA);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get object data")
    public GetObjectDataResponseDto getObjectData(String objectId) {
        Response response = getObjectDataResponse(objectId);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetObjectDataResponseDto.class);
    }

    @Step("Get object data with error response")
    public ErrorResponseDto getObjectDataError(String objectId) {
        Response response = getObjectDataResponse(objectId);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);

        return response.as(ErrorResponseDto.class);
    }

    private Response getObjectDataResponse(String objectId) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("ObjectId", objectId);

        requestSpecification.addPathParams(pathParams);

        return requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
