package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.objectoperations.GetObjectsResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.GET_OBJECTS;

public class GetObjectsSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetObjectsSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_OBJECTS);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get objects")
    public GetObjectsResponseDto getObjects(String lastId, Integer pageSize) {
        Response response = getObjectsResponse(lastId, pageSize);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetObjectsResponseDto.class);
    }

    @Step("Get objects with no content response")
    public void getObjectsNoContent(String lastId, Integer pageSize) {
        Response response = getObjectsResponse(lastId, pageSize);

        response.then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private Response getObjectsResponse(String lastId, Integer pageSize) {
        Map<String, String> queryParams = new HashMap<>();

        queryParams.put("LastId", lastId);

        if (pageSize != null) {
            queryParams.put("PageSize", String.valueOf(pageSize));
        }

        requestSpecification.addQueryParams(queryParams);

        return requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
