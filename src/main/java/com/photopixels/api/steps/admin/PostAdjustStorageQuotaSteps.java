package com.photopixels.api.steps.admin;

import com.photopixels.api.dtos.admin.AdjustStorageQuotaRequestDto;
import com.photopixels.api.dtos.admin.AdjustStorageQuotaResponseDto;
import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.factories.admin.AdjustStorageQuotaFactory;
import com.photopixels.api.helpers.CustomRequestSpecification;
import com.photopixels.api.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.POST_ADJUST_STORAGE_QUOTA;

public class PostAdjustStorageQuotaSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostAdjustStorageQuotaSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(POST_ADJUST_STORAGE_QUOTA);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Adjust storage quota")
    public AdjustStorageQuotaResponseDto adjustStorageQuota(String userId, Long quota) {
                Response response = adjustStorageQuotaResponse(userId, quota);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(AdjustStorageQuotaResponseDto.class);
    }

    @Step("Adjust storage quota with error response")
    public ErrorResponseDto adjustStorageQuotaError(String userId, Long quota, Integer status) {
        Response response = adjustStorageQuotaResponse(userId, quota);

        response.then().statusCode(status);

        return response.as(ErrorResponseDto.class);
    }

    private Response adjustStorageQuotaResponse(String userId, Long quota) {
        AdjustStorageQuotaFactory adjustStorageQuotaFactory = new AdjustStorageQuotaFactory();
        AdjustStorageQuotaRequestDto adjustStorageQuotaRequestDto = adjustStorageQuotaFactory
                .createAdjustStorageQuotaRequestDto(userId, quota);

        requestSpecification.addBodyToRequest(adjustStorageQuotaRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFiltarableRequestSpecification());
    }
}
