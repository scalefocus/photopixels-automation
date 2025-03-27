package com.photopixels.api.steps.admin;

import com.photopixels.api.dtos.admin.DisableRegistrationRequestDto;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.factories.admin.DisableRegistrationFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.POST_DISABLE_REGISTRATION;

public class PostDisableRegistrationSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostDisableRegistrationSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_DISABLE_REGISTRATION);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Disable registration")
    public void disableRegistration(boolean value) {
        Response response = disableRegistrationResponse(value);

        response.then().statusCode(HttpStatus.SC_OK);

        // No response body is returned
    }

    @Step("Disable registration with error response")
    public ErrorResponseDto disableRegistrationError(Boolean value) {
        Response response = disableRegistrationResponse(value);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    private Response disableRegistrationResponse(Boolean value) {
        DisableRegistrationFactory disableRegistrationFactory = new DisableRegistrationFactory();
        DisableRegistrationRequestDto disableRegistrationRequestDto = disableRegistrationFactory
                .createDisableRegistrationRequestDto(value);

        requestSpecification.addBodyToRequest(disableRegistrationRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
