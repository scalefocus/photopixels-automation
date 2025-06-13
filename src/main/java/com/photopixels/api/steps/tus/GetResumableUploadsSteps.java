package com.photopixels.api.steps.tus;

import com.photopixels.api.dtos.tus.ResumableUploadsResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.GET_RESUMABLE_UPLOADS;

public class GetResumableUploadsSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetResumableUploadsSteps(String token) {

        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_RESUMABLE_UPLOADS);
        requestSpecification.setRelaxedHttpsValidation();
        if (token != null) {
            requestSpecification.addCustomHeader("Authorization", token);
        }
    }

    @Step("Get resumable uploads")
    public ResumableUploadsResponseDto getResumableUploads() {
        Response response = requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(ResumableUploadsResponseDto.class);
    }

    @Step("Get resumable uploads without token")
    public void getResumableUploadsError() {

        Response response = new RequestOperationsHelper()
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);

    }

    @Step("Check if file ID is present in user uploads")
    public boolean getResumableUploadsFileIdPresent(String fileId) {
        ResumableUploadsResponseDto response = getResumableUploads();
        return response.getUserUploads().stream()
                .anyMatch(upload -> fileId.equals(upload.getFileId()));
    }

}
