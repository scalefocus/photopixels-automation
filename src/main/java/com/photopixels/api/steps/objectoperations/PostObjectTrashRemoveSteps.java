package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.dtos.objectoperations.UpdateObjectTrashRemoveRequestDto;
import com.photopixels.api.factories.objectoperations.UpdateObjectTrashRemoveFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.POST_TRASH_OBJECT_REMOVE;

public class PostObjectTrashRemoveSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    private final UpdateObjectTrashRemoveFactory factory;

    public PostObjectTrashRemoveSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();
        factory = new UpdateObjectTrashRemoveFactory();

        requestSpecification.addBasePath(POST_TRASH_OBJECT_REMOVE);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Remove object from trash with ID: {objectId}")
    public ObjectVersioningResponseDto removeTrashedObject(String objectId) {
        Response response = sendRemoveTrashedObjectRequest(objectId);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(ObjectVersioningResponseDto.class);
    }

    @Step("Remove trashed object with invalid ID: {objectId}")
    public ErrorResponseDto removeTrashedObjectExpectingBadRequest(String objectId, int statusCode) {
        Response response = sendRemoveTrashedObjectRequest(objectId);

        response.then().statusCode(statusCode);

        return response.as(ErrorResponseDto.class);
    }

    private Response sendRemoveTrashedObjectRequest(String objectId) {
        UpdateObjectTrashRemoveRequestDto requestDto = factory.createUpdateObjectTrashRemoveRequestDto(objectId);
        requestSpecification.addBodyToRequest(requestDto);

        return requestOperationsHelper.sendPostRequest(
                requestSpecification.getFilterableRequestSpecification()
        );
    }
}