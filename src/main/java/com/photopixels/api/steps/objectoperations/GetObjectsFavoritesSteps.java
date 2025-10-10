package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.objectoperations.GetObjectsFavoritesResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.constants.BasePathsConstants.GET_OBJECTS_FAVORITES;

public class GetObjectsFavoritesSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetObjectsFavoritesSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_OBJECTS_FAVORITES);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get objects favorites")
    public GetObjectsFavoritesResponseDto getObjectsFavorites(String lastId, Integer pageSize) {
        Response response = getObjectsFavoritesResponse(lastId, pageSize);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetObjectsFavoritesResponseDto.class);
    }

    @Step("Get objects favorites with no content response")
    public void getEmptyObjectsFavorites(String lastId, Integer pageSize) {
        Response response = getObjectsFavoritesResponse(lastId, pageSize);

        response.then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private Response getObjectsFavoritesResponse(String lastId, Integer pageSize) {
        Map<String, String> queryParams = new HashMap<>();

        if (lastId != null) queryParams.put("LastId", lastId);
        if (pageSize != null) queryParams.put("PageSize", pageSize.toString());

        requestSpecification.addQueryParams(queryParams);
        return requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
