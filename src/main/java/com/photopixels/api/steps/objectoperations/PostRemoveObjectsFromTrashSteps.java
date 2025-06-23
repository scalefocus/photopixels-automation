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

import static com.photopixels.constants.BasePathsConstants.POST_REMOVE_OBJECTS_FROM_TRASH;

public class PostRemoveObjectsFromTrashSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostRemoveObjectsFromTrashSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_REMOVE_OBJECTS_FROM_TRASH);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Remove objects from trash")
    public ObjectVersioningResponseDto removeObjectsFromTrash(List<String> objectIds) {
        Response response = removeObjectsFromTrashResponse(objectIds);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(ObjectVersioningResponseDto.class);
    }

    @Step("Remove objects from trash with error")
    public ErrorResponseDto removeObjectsFromTrashError(List<String> objectIds, int statusCode) {
        Response response = removeObjectsFromTrashResponse(objectIds);

        response.then().statusCode(statusCode);

        return response.as(ErrorResponseDto.class);
    }

    private Response removeObjectsFromTrashResponse(List<String> objectIds) {
        ObjectIdsFactory objectIdsFactory = new ObjectIdsFactory();
        ObjectIdsRequestDto objectIdsRequestDto = objectIdsFactory
                .createDeletePermanentRequestDto(objectIds);

        requestSpecification.addBodyToRequest(objectIdsRequestDto);

        return requestOperationsHelper.sendPostRequest(
                requestSpecification.getFilterableRequestSpecification()
        );
    }
}