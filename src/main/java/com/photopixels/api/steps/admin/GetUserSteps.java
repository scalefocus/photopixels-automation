package com.photopixels.api.steps.admin;

import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.api.constants.BasePathsConstants.GET_USER;

public class GetUserSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetUserSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_USER);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get user")
    public GetUserResponseDto getUser(String id) {
        Response response = getUserResponse(id);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetUserResponseDto.class);
    }

    @Step("Get user with no content response")
    public void getUserNoContent(String id) {
        Response response = getUserResponse(id);

        response.then().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private Response getUserResponse(String id) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("Id", id);

        requestSpecification.addPathParams(pathParams);

        return requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
