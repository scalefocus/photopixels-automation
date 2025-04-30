package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetTrashedObjectsResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.GET_OBJECTS_TRASHED;

public class GetTrashedObjectsSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetTrashedObjectsSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_OBJECTS_TRASHED);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();
        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get trashed objects with optional lastId: {lastId} and pageSize: {pageSize}")
    public GetTrashedObjectsResponseDto getTrashedObjects(String lastId, int pageSize) {
        Map<String, String> queryParams = new HashMap<>();

        queryParams.put("PageSize", String.valueOf(pageSize));

        if (lastId != null && !lastId.isEmpty()) {
            queryParams.put("lastId", lastId);
        }

        requestSpecification.addQueryParams(queryParams);

        Response response = requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetTrashedObjectsResponseDto.class);
    }

    @Step("Get trashed objects with invalid pageSize: {pageSize}")
    public ErrorResponseDto getTrashedObjectsWithInvalidPageSize(String pageSize) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("PageSize", pageSize);

        requestSpecification.addQueryParams(queryParams);

        Response response = requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

}