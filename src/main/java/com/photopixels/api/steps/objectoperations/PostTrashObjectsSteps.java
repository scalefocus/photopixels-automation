package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectIdsRequestDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.factories.objectoperations.ObjectIdsFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.List;

import static com.photopixels.constants.BasePathsConstants.POST_TRASH_OBJECTS;

public class PostTrashObjectsSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostTrashObjectsSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_TRASH_OBJECTS);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Trash objects from trash")
    public ObjectVersioningResponseDto trashObjects(List<String> objectIds) {
        Response response = trashObjectsResponse(objectIds);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(ObjectVersioningResponseDto.class);
    }

    @Step("Trash objects with error")
    public ErrorResponseDto trashObjectsError(List<String> objectIds, int statusCode) {
        Response response = trashObjectsResponse(objectIds);

        response.then().statusCode(statusCode);

        return response.as(ErrorResponseDto.class);
    }

    private Response trashObjectsResponse(List<String> objectIds) {
        ObjectIdsFactory objectIdsFactory = new ObjectIdsFactory();
        ObjectIdsRequestDto objectIdsRequestDto = objectIdsFactory
                .createDeletePermanentRequestDto(objectIds);

        requestSpecification.addBodyToRequest(objectIdsRequestDto);

        return requestOperationsHelper.sendPostRequest(
                requestSpecification.getFilterableRequestSpecification()
        );
    }
}