package com.photopixels.api.steps.sync;

import com.photopixels.api.dtos.sync.GetChangesResponseDto;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.photopixels.api.constants.BasePathsConstants.GET_CHANGES;

public class GetChangesSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public GetChangesSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_CHANGES);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();


        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get changes")
    public GetChangesResponseDto getChanges(String revisionId) {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("RevisionId", revisionId);

        requestSpecification.addPathParams(pathParams);

        Response response = requestOperationsHelper
                .sendGetRequest(requestSpecification.getFilterableRequestSpecification());

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetChangesResponseDto.class);
    }
}
