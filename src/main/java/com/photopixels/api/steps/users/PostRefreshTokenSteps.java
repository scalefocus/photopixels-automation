package com.photopixels.api.steps.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.dtos.users.RefreshTokenRequestDto;
import com.photopixels.api.factories.users.RefreshTokenFactory;
import com.photopixels.api.helpers.CustomRequestSpecification;
import com.photopixels.api.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.POST_REFRESH_TOKEN;

public class PostRefreshTokenSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostRefreshTokenSteps() {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_REFRESH_TOKEN);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();
    }

    @Step("Refresh token")
    public LoginResponseDto refreshToken(String refreshToken) {
        Response response = refreshTokenResponse(refreshToken);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(LoginResponseDto.class);
    }

    @Step("Refresh token with error response")
    public ErrorResponseDto refreshTokenError(String refreshToken) {
        Response response = refreshTokenResponse(refreshToken);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    @Step("Refresh token with forbidden response")
    public void refreshTokenErrorForbidden(String refreshToken) {
        Response response = refreshTokenResponse(refreshToken);

        response.then().statusCode(HttpStatus.SC_FORBIDDEN);
    }

    private Response refreshTokenResponse(String refreshToken) {
        RefreshTokenFactory refreshTokensFactory = new RefreshTokenFactory();
        RefreshTokenRequestDto refreshTokenRequestDto = refreshTokensFactory.createRefreshTokenRequestDto(refreshToken);

        requestSpecification.addBodyToRequest(refreshTokenRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFiltarableRequestSpecification());
    }
}
