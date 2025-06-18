package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.DeletePermanentRequestDto;
import com.photopixels.api.dtos.objectoperations.ObjectVersioningResponseDto;
import com.photopixels.api.factories.objectoperations.DeletePermanentFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.List;

import static com.photopixels.constants.BasePathsConstants.POST_TRASH_DELETE_PERMANENT;

public class PostTrashDeletePermanentSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostTrashDeletePermanentSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_TRASH_DELETE_PERMANENT);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Delete permanent object from trash")
    public ObjectVersioningResponseDto deletePermanentFromTrash(List<String> objectIds) {
        Response response = deletePermanentFromTrashResponse(objectIds);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(ObjectVersioningResponseDto.class);
    }

    @Step("Delete permanent from trash with error")
    public ErrorResponseDto deletePermanentFromTrashError(List<String> objectIds, int statusCode) {
        Response response = deletePermanentFromTrashResponse(objectIds);

        response.then().statusCode(statusCode);

        return response.as(ErrorResponseDto.class);
    }

    private Response deletePermanentFromTrashResponse(List<String> objectIds) {
        DeletePermanentFactory deletePermanentFactory = new DeletePermanentFactory();
        DeletePermanentRequestDto deletePermanentRequestDto = deletePermanentFactory
                .createDeletePermanentRequestDto(objectIds);

        requestSpecification.addBodyToRequest(deletePermanentRequestDto);

        return requestOperationsHelper.sendPostRequest(
                requestSpecification.getFilterableRequestSpecification()
        );
    }
}