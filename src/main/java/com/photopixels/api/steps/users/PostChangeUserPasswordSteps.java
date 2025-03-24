package com.photopixels.api.steps.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.ChangeUserPasswordRequestDto;
import com.photopixels.api.factories.users.ChangeUserPasswordFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.POST_CHANGE_USER_PASSWORD;

public class PostChangeUserPasswordSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostChangeUserPasswordSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_CHANGE_USER_PASSWORD);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Change user password")
    public void changeUserPassword(String oldPassword, String newPassword) {
        Response response = changeUserPasswordResponse(oldPassword, newPassword);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response is returned
    }

    @Step("Change user password with error response")
    public ErrorResponseDto changeUserPasswordError(String oldPassword, String newPassword) {
        Response response = changeUserPasswordResponse(oldPassword, newPassword);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    private Response changeUserPasswordResponse(String oldPassword, String newPassword) {
        ChangeUserPasswordFactory changeUserPasswordFactory = new ChangeUserPasswordFactory();
        ChangeUserPasswordRequestDto changeUserPasswordRequestDto = changeUserPasswordFactory.createChangeUserPasswordRequestDto(oldPassword, newPassword);

        requestSpecification.addBodyToRequest(changeUserPasswordRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
