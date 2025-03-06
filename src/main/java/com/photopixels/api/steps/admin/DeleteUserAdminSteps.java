package com.photopixels.api.steps.admin;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.helpers.CustomRequestSpecification;
import com.photopixels.api.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.api.constants.BasePathsConstants.DELETE_USER_ADMIN;

public class DeleteUserAdminSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public DeleteUserAdminSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(DELETE_USER_ADMIN);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Delete user by admin")
    public void deleteUserAdmin(String userId) {
        Response response = deleteUserAdminResponse(userId);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response is returned
    }

    @Step("Delete user by admin with error response")
    public ErrorResponseDto deleteUserAdminError(String userId) {
        Response response = deleteUserAdminResponse(userId);

        response.then().statusCode(HttpStatus.SC_NOT_FOUND);

        return response.as(ErrorResponseDto.class);
    }

    private Response deleteUserAdminResponse(String userId) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("Id", userId);

        requestSpecification.addPathParams(pathParams);

        return requestOperationsHelper
                .sendDeleteRequest(requestSpecification.getFiltarableRequestSpecification());
    }
}
