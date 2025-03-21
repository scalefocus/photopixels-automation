package com.photopixels.api.steps.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.ResetUserPasswordRequestDto;
import com.photopixels.api.factories.users.ResetUserPasswordFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.POST_RESET_USER_PASSWORD;

public class PostResetUserPasswordSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostResetUserPasswordSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_RESET_USER_PASSWORD);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();
    }

    @Step("Reset user password")
    public void resetUserPassword(String code, String password, String email) {
        Response response = resetUserPasswordResponse(code, password, email);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response is returned
    }

    @Step("Reset user password with error response")
    public ErrorResponseDto resetUserPasswordError(String code, String password, String email) {
        Response response = resetUserPasswordResponse(code, password, email);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    private Response resetUserPasswordResponse(String code, String password, String email) {
        ResetUserPasswordFactory resetUserPasswordFactory = new ResetUserPasswordFactory();
        ResetUserPasswordRequestDto resetUserPasswordRequestDto = resetUserPasswordFactory
                .createResetUserPasswordRequestDto(code, password, email);

        requestSpecification.addBodyToRequest(resetUserPasswordRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
