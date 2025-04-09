package com.photopixels.api.steps.status;

import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.constants.BasePathsConstants.GET_LOGS;

public class GetLogsSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetLogsSteps() {

        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_LOGS);
        requestSpecification.setContentType(ContentType.TEXT);
        requestSpecification.setRelaxedHttpsValidation();

    }

    @Step("Get server logs")
    public String getLogs() {
        Response response = requestOperationsHelper
        .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_OK);

        return response.getBody().asString();
    }
}
