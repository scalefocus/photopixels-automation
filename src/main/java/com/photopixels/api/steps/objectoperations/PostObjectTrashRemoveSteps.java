package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.dtos.objectoperations.UpdateObjectTrashRemoveRequestDto;
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

    public PostObjectTrashRemoveSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_TRASH_OBJECT_REMOVE);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Remove object from trash with ID: {objectId}")
    public ObjectVersioningResponseDto removeTrashedObject(String objectId) {
        UpdateObjectTrashRemoveRequestDto requestDto = new UpdateObjectTrashRemoveRequestDto();
        requestDto.setId(objectId);

        requestSpecification.addBodyToRequest(requestDto);

        Response response = requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(ObjectVersioningResponseDto.class);
    }

    @Step("Try to remove trashed object with invalid ID: {objectId} and expect 400 Bad Request")
    public Response removeTrashedObjectExpectingBadRequest(String objectId) {
        UpdateObjectTrashRemoveRequestDto requestDto = new UpdateObjectTrashRemoveRequestDto();
        requestDto.setId(objectId);

        requestSpecification.addBodyToRequest(requestDto);

        Response response = requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response;
    }
}