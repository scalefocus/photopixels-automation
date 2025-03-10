package com.photopixels.api.steps.admin;

import com.photopixels.api.dtos.admin.ChangeUserRoleRequestDto;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.enums.UserRolesEnum;
import com.photopixels.api.factories.admin.ChangeUserRoleFactory;
import com.photopixels.api.helpers.CustomRequestSpecification;
import com.photopixels.api.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.POST_CHANGE_USER_ROLE;

public class PostChangeUserRoleSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostChangeUserRoleSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_CHANGE_USER_ROLE);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Change user role")
    public void changeUserRole(String userId, UserRolesEnum role) {
                Response response = changeUserRoleResponse(userId, role);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response body is returned
    }

    @Step("Change user role with error response")
    public ErrorResponseDto changeUserRoleError(String userId, UserRolesEnum role, int status) {
        Response response = changeUserRoleResponse(userId, role);

        response.then().statusCode(status);

        return response.as(ErrorResponseDto.class);
    }

    private Response changeUserRoleResponse(String userId, UserRolesEnum role) {
        Integer userRole;

        if (role != null) {
            userRole = role.getValue();
        } else {
            userRole = null;
        }

        ChangeUserRoleFactory changeUserRoleFactory = new ChangeUserRoleFactory();
        ChangeUserRoleRequestDto changeUserRoleRequestDto = changeUserRoleFactory
                .createChangeUserRoleRequestDto(userId, userRole);

        requestSpecification.addBodyToRequest(changeUserRoleRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFiltarableRequestSpecification());
    }
}
