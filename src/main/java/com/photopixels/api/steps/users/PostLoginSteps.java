package com.photopixels.api.steps.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.LoginRequestDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.factories.users.LoginFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.POST_LOGIN;

public class PostLoginSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostLoginSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_LOGIN);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();
    }

    @Step("Login")
    public LoginResponseDto login(String email, String password) {
        Response response = loginResponse(email, password);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(LoginResponseDto.class);
    }

    @Step("Login with error response")
    public ErrorResponseDto loginError(String email, String password) {
        Response response = loginResponse(email, password);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    @Step("Login with forbidden response")
    public void loginErrorForbidden(String email, String password) {
        Response response = loginResponse(email, password);

        response.then().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    private Response loginResponse(String email, String password) {
        LoginFactory loginFactory = new LoginFactory();
        LoginRequestDto loginRequestDto = loginFactory.createLoginDto(email, password);

        requestSpecification.addBodyToRequest(loginRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
