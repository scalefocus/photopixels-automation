package com.photopixels.api.steps.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.ForgotUserPasswordRequestDto;
import com.photopixels.api.factories.users.ForgotUserPasswordFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.POST_FORGOT_USER_PASSWORD;

public class PostForgotUserPasswordSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostForgotUserPasswordSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_FORGOT_USER_PASSWORD);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();
    }

    @Step("Forgot user password")
    public void forgotUserPassword(String email) {
        Response response = forgotUserPasswordResponse(email);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response is returned
    }

    @Step("Forgot user password with error response")
    public ErrorResponseDto forgotUserPasswordError(String email, int status) {
        Response response = forgotUserPasswordResponse(email);

        response.then().statusCode(status);

        return response.as(ErrorResponseDto.class);
    }

    private Response forgotUserPasswordResponse(String email) {
        ForgotUserPasswordFactory forgotUserPasswordFactory = new ForgotUserPasswordFactory();
        ForgotUserPasswordRequestDto forgotUserPasswordRequestDto = forgotUserPasswordFactory
                .createForgotUserPasswordRequestDto(email);

        requestSpecification.addBodyToRequest(forgotUserPasswordRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
