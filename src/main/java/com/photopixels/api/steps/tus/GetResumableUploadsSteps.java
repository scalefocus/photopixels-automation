package com.photopixels.api.steps.tus;

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
    public String getResumableUploads() {
        Response response = requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_OK);

        return response.getBody().asString();
    }

    @Step("Get resumable uploads without token")
    public Response getResumableUploadsError(){

        Response response = new RequestOperationsHelper()
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_UNAUTHORIZED);

        return response;
    }
}
