package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.objectoperations.FavoritesRequestDto;
import com.photopixels.api.dtos.objectoperations.FavoritesResponseDto;
import com.photopixels.api.factories.objectoperations.FavoritesFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.List;

import static com.photopixels.constants.BasePathsConstants.POST_ADD_FAVORITES;
import static com.photopixels.constants.BasePathsConstants.POST_REMOVE_FAVORITES;

public class FavoritesObjectSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public FavoritesObjectSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Add object to favorites album")
    public FavoritesResponseDto addObjectToFavorites(List<String> objectIds, int favoriteActionType) {
        Response response = addObjectToFavoritesResponse(objectIds, favoriteActionType);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(FavoritesResponseDto.class);
    }

    @Step("Remove object from favorites album")
    public FavoritesResponseDto removeObjectFromFavorites(List<String> objectIds, int favoriteActionType) {
        Response response = removeObjectToFavoritesResponse(objectIds, favoriteActionType);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(FavoritesResponseDto.class);
    }

    private Response addObjectToFavoritesResponse(List<String> objectIds, int favoriteActionType) {
        FavoritesFactory favoritesFactory = new FavoritesFactory();
        FavoritesRequestDto favoritesRequestDto = favoritesFactory.createFavoritesRequestDto(objectIds, favoriteActionType);

        requestSpecification.addBasePath(POST_ADD_FAVORITES);
        requestSpecification.addBodyToRequest(favoritesRequestDto);
        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }

    private Response removeObjectToFavoritesResponse(List<String> objectIds, int favoriteActionType) {
        FavoritesFactory favoritesFactory = new FavoritesFactory();
        FavoritesRequestDto favoritesRequestDto = favoritesFactory.createFavoritesRequestDto(objectIds, favoriteActionType);

        requestSpecification.addBasePath(POST_REMOVE_FAVORITES);
        requestSpecification.addBodyToRequest(favoritesRequestDto);
        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
