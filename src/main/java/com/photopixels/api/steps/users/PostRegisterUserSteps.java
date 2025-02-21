package com.photopixels.api.steps.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.RegisterUserRequestDto;
import com.photopixels.api.factories.users.RegisterUserFactory;
import com.photopixels.api.helpers.CustomRequestSpecification;
import com.photopixels.api.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.POST_REGISTER_USER;

public class PostRegisterUserSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostRegisterUserSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_REGISTER_USER);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();
    }

    @Step("Register user")
    public void registerUser(String name, String email, String password) {
        Response response = registerUserResponse(name, email, password);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response body is returned
    }

    @Step("Register user with error response")
    public ErrorResponseDto registerUserError(String name, String email, String password) {
        Response response = registerUserResponse(name, email, password);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    private Response registerUserResponse(String name, String email, String password) {
        RegisterUserFactory registerUserFactory = new RegisterUserFactory();
        RegisterUserRequestDto registerUserRequestDto = registerUserFactory.createRegisterNewUserDto(name, email, password);

        requestSpecification.addBodyToRequest(registerUserRequestDto);

        Response response = requestOperationsHelper
                .sendPostRequest(requestSpecification.getFiltarableRequestSpecification());

        return response;
    }
}
