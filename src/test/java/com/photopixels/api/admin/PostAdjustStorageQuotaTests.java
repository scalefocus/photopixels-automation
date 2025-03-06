package com.photopixels.api.admin;

import com.photopixels.api.dtos.admin.AdjustStorageQuotaResponseDto;
import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.enums.ErrorMessagesEnum;
import com.photopixels.api.helpers.listeners.StatusTestListener;
import com.photopixels.api.steps.admin.GetUserSteps;
import com.photopixels.api.steps.admin.PostAdjustStorageQuotaSteps;
import com.photopixels.base.ApiBaseTest;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.UUID;

import static com.photopixels.api.constants.ErrorMessageConstants.NOT_FOUND_ERROR;
import static com.photopixels.api.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Admin")
public class PostAdjustStorageQuotaTests extends ApiBaseTest {

    private String token;
    private String userId;
    private Long quota = Long.valueOf(RandomStringUtils.randomNumeric(11));

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getAdminToken();

        userId = getUserId(inputData.getUsername());
    }

    @Test(description = "Adjust storage quota")
    @Description("Adjust storage quota successfully")
    @Story("Adjust Storage Quota")
    @Severity(SeverityLevel.NORMAL)
    public void adjustStorageQuotaTest() {
        PostAdjustStorageQuotaSteps postAdjustStorageQuotaSteps = new PostAdjustStorageQuotaSteps(token);
        AdjustStorageQuotaResponseDto adjustStorageQuotaResponseDto = postAdjustStorageQuotaSteps
                .adjustStorageQuota(userId, quota);

        GetUserSteps getUserSteps = new GetUserSteps(token);
        GetUserResponseDto getUserResponseDto = getUserSteps.getUser(userId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(adjustStorageQuotaResponseDto.getQuota(), quota,
                "User quota is not changed!");
        softAssert.assertEquals(getUserResponseDto.getQuota(), quota,
                "User quota is not changed!");

        softAssert.assertAll();
    }

    @Test(description = "Adjust storage quota no user id")
    @Description("Adjust storage quota with missing user id")
    @Story("Adjust Storage Quota")
    @Severity(SeverityLevel.MINOR)
    public void adjustStorageQuotaNoUserIdTest() {
        PostAdjustStorageQuotaSteps postAdjustStorageQuotaSteps = new PostAdjustStorageQuotaSteps(token);
        ErrorResponseDto errorResponseDto = postAdjustStorageQuotaSteps
                .adjustStorageQuotaError(null, quota, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        // TODO: Check if this error message is the expected one
        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.REQUEST.getKey()),
                ErrorMessagesEnum.REQUEST.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Adjust storage quota not existing user id")
    @Description("Adjust storage quota with not existing user id")
    @Story("Adjust Storage Quota")
    @Severity(SeverityLevel.MINOR)
    public void adjustStorageQuotaNotExistingUserIdTest() {
        String notExistingId = UUID.randomUUID().toString();

        PostAdjustStorageQuotaSteps postAdjustStorageQuotaSteps = new PostAdjustStorageQuotaSteps(token);
        ErrorResponseDto errorResponseDto = postAdjustStorageQuotaSteps
                .adjustStorageQuotaError(notExistingId, quota, HttpStatus.SC_NOT_FOUND);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), NOT_FOUND_ERROR, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_NOT_FOUND, "Error status is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Adjust storage quota no quota")
    @Description("Adjust storage quota with missing quota")
    @Story("Adjust Storage Quota")
    @Severity(SeverityLevel.MINOR)
    public void adjustStorageQuotaNoQuotaTest() {
        PostAdjustStorageQuotaSteps postAdjustStorageQuotaSteps = new PostAdjustStorageQuotaSteps(token);
        ErrorResponseDto errorResponseDto = postAdjustStorageQuotaSteps
                .adjustStorageQuotaError(userId, null, HttpStatus.SC_BAD_REQUEST);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(errorResponseDto.getTitle(), VALIDATION_ERRORS_TITLE, "Error title is not correct");
        softAssert.assertEquals(errorResponseDto.getStatus(), HttpStatus.SC_BAD_REQUEST, "Error status is not correct");

        // TODO: Check if this error message is the expected one
        softAssert.assertEquals(errorResponseDto.extractErrorMessageByKey(ErrorMessagesEnum.REQUEST.getKey()),
                ErrorMessagesEnum.REQUEST.getErrorMessage(), "Error message is not correct");

        softAssert.assertAll();
    }

}
