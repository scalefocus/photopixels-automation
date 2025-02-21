package com.photopixels.api.steps.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.DeleteUserRequestDto;
import com.photopixels.api.factories.users.DeleteUserFactory;
import com.photopixels.api.helpers.CustomRequestSpecification;
import com.photopixels.api.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.DELETE_USER;

public class DeleteUserSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public DeleteUserSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(DELETE_USER);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Delete user")
    public void deleteUser(String password) {
        Response response = deleteUserResponse(password);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response is returned
    }

    @Step("Delete user with error response")
    public ErrorResponseDto deleteUserError(String password) {
        Response response = deleteUserResponse(password);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    @Step("Delete user with forbidden response")
    public void deleteUserErrorForbidden(String password) {
        Response response = deleteUserResponse(password);

        response.then().statusCode(HttpStatus.SC_FORBIDDEN);

        // No response body is returned
    }

    private Response deleteUserResponse(String password) {
        DeleteUserFactory deleteUserFactory = new DeleteUserFactory();
        DeleteUserRequestDto deleteUserRequestDto = deleteUserFactory.createDeleteUserDto(password);

        requestSpecification.addBodyToRequest(deleteUserRequestDto);

        Response response = requestOperationsHelper
                .sendDeleteRequest(requestSpecification.getFiltarableRequestSpecification());

        return response;
    }
}
