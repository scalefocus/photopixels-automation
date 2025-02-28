package com.photopixels.api.steps.admin;

import com.photopixels.api.dtos.admin.ResetPasswordAdminRequestDto;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.factories.admin.ResetPasswordAdminFactory;
import com.photopixels.api.helpers.CustomRequestSpecification;
import com.photopixels.api.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.POST_RESET_PASSWORD_ADMIN;

public class PostResetPasswordAdminSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostResetPasswordAdminSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_RESET_PASSWORD_ADMIN);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Reset password by admin")
    public void resetPasswordAdmin(String password, String email) {
        Response response = resetPasswordAdminResponse(password, email);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response is returned
    }

    @Step("Reset password by admin with error response")
    public ErrorResponseDto resetPasswordAdminError(String password, String email) {
        Response response = resetPasswordAdminResponse(password, email);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    private Response resetPasswordAdminResponse(String password, String email) {
        ResetPasswordAdminFactory resetPasswordAdminFactory = new ResetPasswordAdminFactory();
        ResetPasswordAdminRequestDto resetUserPasswordRequestDto = resetPasswordAdminFactory
                .createResetPasswordAdminRequestDto(password, email);

        requestSpecification.addBodyToRequest(resetUserPasswordRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFiltarableRequestSpecification());
    }
}
