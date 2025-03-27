package com.photopixels.api.steps.admin;

import com.photopixels.api.dtos.admin.RegisterUserAdminRequestDto;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.factories.admin.RegisterUserAdminFactory;
import com.photopixels.enums.UserRolesEnum;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.POST_REGISTER_USER_ADMIN;

public class PostRegisterUserAdminSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostRegisterUserAdminSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_REGISTER_USER_ADMIN);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Register user by admin")
    public void registerUserAdmin(String name, String email, String password, UserRolesEnum role) {
        Response response = registerUserAdminResponse(name, email, password, role);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response body is returned
    }

    @Step("Register user by admin with error response")
    public ErrorResponseDto registerUserAdminError(String name, String email, String password, UserRolesEnum role) {
        Response response = registerUserAdminResponse(name, email, password, role);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    private Response registerUserAdminResponse(String name, String email, String password, UserRolesEnum role) {
        RegisterUserAdminFactory registerUserAdminFactory = new RegisterUserAdminFactory();
        RegisterUserAdminRequestDto registerUserAdminRequestDto = registerUserAdminFactory
                .createRegisterUserAdminDto(name, email, password, role.getValue());

        requestSpecification.addBodyToRequest(registerUserAdminRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
