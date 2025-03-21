package com.photopixels.api.steps.admin;

import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static com.photopixels.api.constants.BasePathsConstants.GET_USERS;

public class GetUsersSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetUsersSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_USERS);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();


        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get users")
    public GetUserResponseDto[] getUsers() {
        Response response = requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetUserResponseDto[].class);
    }
}
