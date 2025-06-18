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

import static com.photopixels.constants.BasePathsConstants.GET_OBJECT;

public class GetObjectSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetObjectSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_OBJECT);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get object")
    public String getObject(String objectId, String format) {
        Response response = getObjectResponse(objectId, format);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.asString();
    }

    @Step("Get object with error response")
    public ErrorResponseDto getObjectError(String objectId, String format) {
        Response response = getObjectResponse(objectId, format);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);

        return response.as(ErrorResponseDto.class);
    }

    private Response getObjectResponse(String objectId, String format) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("Id", objectId);

        requestSpecification.addPathParams(pathParams);

        if (format != null) {
            Map<String, String> queryParams = new HashMap<>();
            queryParams.put("Format", format);

            requestSpecification.addQueryParams(queryParams);
        }

        return requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
