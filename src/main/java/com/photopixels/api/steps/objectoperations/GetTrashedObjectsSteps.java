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

import static com.photopixels.constants.BasePathsConstants.GET_TRASHED_OBJECTS;

public class GetTrashedObjectsSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetTrashedObjectsSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_TRASHED_OBJECTS);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();
        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get trashed objects with optional lastId: {lastId} and pageSize: {pageSize}")
    public GetTrashedObjectsResponseDto getTrashedObjects(String lastId, int pageSize) {
        Response response = getRawTrashedObjectsResponse(lastId, String.valueOf(pageSize));

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetTrashedObjectsResponseDto.class);
    }

    @Step("Get trashed objects with invalid pageSize: {pageSize}")
    public ErrorResponseDto getTrashedObjectsWithInvalidPageSize(String pageSize) {
        Response response = getRawTrashedObjectsResponse(null, pageSize);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    @Step("Get trashed objects and expect 204 No Content with lastId: {lastId} and pageSize: {pageSize}")
    public void getTrashedObjectsExpectingNoContent(String lastId, int pageSize) {
        getRawTrashedObjectsResponse(lastId, String.valueOf(pageSize))

                .then().statusCode(HttpStatus.SC_NO_CONTENT);
    }


    //Retrieves trashed objects while allowing both 200 OK and 204 No Content responses
    //The API may return either a list of newer trashed objects (200 OK) or indicate there are no further results (204 No Content).
    // Returning null in the 204 case allows the test to handle both flows.
    @Step("Get trashed objects allowing both 200 OK and 204 No Content for lastId: {lastId}")
    public GetTrashedObjectsResponseDto getTrashedObjectsAllowingNoContent(String lastId, int pageSize) {
        Response response = getRawTrashedObjectsResponse(lastId, String.valueOf(pageSize));

        int statusCode = response.getStatusCode();
        if (statusCode == HttpStatus.SC_NO_CONTENT) {
            return null;
        }

        response.then().statusCode(HttpStatus.SC_OK);
        return response.as(GetTrashedObjectsResponseDto.class);
    }

    private Response getRawTrashedObjectsResponse(String lastId, String pageSize) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("PageSize", pageSize);

        if (lastId != null && !lastId.isEmpty()) {
            queryParams.put("lastId", lastId);
        }

        requestSpecification.addQueryParams(queryParams);

        return requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}