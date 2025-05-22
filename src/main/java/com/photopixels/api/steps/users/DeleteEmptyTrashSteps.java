package com.photopixels.api.steps.users;

import com.photopixels.api.dtos.users.EmptyTrashResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.DELETE_EMPTY_TRASH;

public class DeleteEmptyTrashSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public DeleteEmptyTrashSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(DELETE_EMPTY_TRASH);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();
        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Empty user's trash bin")
    public EmptyTrashResponseDto emptyTrash() {
        Response response = requestOperationsHelper
                .sendDeleteRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(EmptyTrashResponseDto.class);
    }
}