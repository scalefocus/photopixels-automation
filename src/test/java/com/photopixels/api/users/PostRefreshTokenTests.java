package com.photopixels.api.users;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.steps.users.PostLoginSteps;
import com.photopixels.api.steps.users.PostRefreshTokenSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.helpers.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.apache.http.HttpStatus;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Users")
public class PostRefreshTokenTests extends ApiBaseTest {

    @Test(description = "Successful token refresh")
    @Description("Successful refresh of token")
    @Story("Refresh Token")
    @Severity(SeverityLevel.NORMAL)
    public void refreshTokenTest() {
        String email = inputData.getUsername();
        String password = inputData.getPassword();

        PostLoginSteps postLoginSteps = new PostLoginSteps();
        LoginResponseDto loginResponseDto = postLoginSteps.login(email, password);
        String refreshToken = loginResponseDto.getRefreshToken();

        PostRefreshTokenSteps postRefreshTokenSteps = new PostRefreshTokenSteps();
        LoginResponseDto refreshResponseDto = postRefreshTokenSteps.refreshToken(refreshToken);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(refreshResponseDto.getTokenType(), "Bearer", "Token type is not correct");
        softAssert.assertNotNull(refreshResponseDto.getAccessToken(), "Access token is missing");
        softAssert.assertNotNull(refreshResponseDto.getRefreshToken(), "Refresh token is missing");
        softAssert.assertNotNull(refreshResponseDto.getUserId(), "User id is missing");

        softAssert.assertNotEquals(refreshResponseDto.getAccessToken(), loginResponseDto.getAccessToken(),
                "Access token is not refreshed");
        softAssert.assertNotEquals(refreshResponseDto.getRefreshToken(), loginResponseDto.getRefreshToken(),
                "Refresh token is not refreshed");

        softAssert.assertAll();
    }

    @Test(description = "Refresh token not valid token")
    @Description("Refresh token with not valid token")
    @Story("Refresh Token")
    @Severity(SeverityLevel.MINOR)
    public void refreshTokenNotValidTokenTest() {
        String token = "TempToken";

        PostRefreshTokenSteps postRefreshTokenSteps = new PostRefreshTokenSteps();
        postRefreshTokenSteps.refreshTokenErrorForbidden(token);

        // No response is returned
    }

    @Test(description = "Refresh token no token")
    @Description("Refresh token with missing token")
    @Story("Refresh Token")
    @Severity(SeverityLevel.MINOR)
    public void refreshTokenNoTokenTest() {
        PostRefreshTokenSteps postRefreshTokenSteps = new PostRefreshTokenSteps();
        ErrorResponseDto errorResponseDto = postRefreshTokenSteps.refreshTokenError(null);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.REFRESH_TOKEN.getKey()),
                ErrorMessagesEnum.REFRESH_TOKEN.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

}
