package com.photopixels.api.steps.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectDataResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectsDataRequestDto;
import com.photopixels.api.factories.objectoperations.GetObjectsDataFactory;
import com.photopixels.helpers.CustomRequestSpecification;
import com.photopixels.helpers.RequestOperationsHelper;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.List;

import static com.photopixels.constants.BasePathsConstants.GET_OBJECTS_DATA;

public class PostGetObjectsDataSteps {

    private final RequestOperationsHelper requestOperationsHelper;
    private final CustomRequestSpecification requestSpecification;

    public PostGetObjectsDataSteps(String token) {
        requestOperationsHelper = new RequestOperationsHelper();
        requestSpecification = new CustomRequestSpecification();

        requestSpecification.addBasePath(GET_OBJECTS_DATA);
        requestSpecification.setContentType(ContentType.JSON);
        requestSpecification.setRelaxedHttpsValidation();

        requestSpecification.addCustomHeader("Authorization", token);
    }

    @Step("Get objects data")
    public GetObjectDataResponseDto[] getObjectsData(List<String> objectIds) {
        Response response = getObjectsDataResponse(objectIds);

        response.then().statusCode(HttpStatus.SC_OK);

        return response.as(GetObjectDataResponseDto[].class);
    }

    @Step("Get objects data with error response")
    public ErrorResponseDto getObjectsDataWithError(List<String> objectIds) {
        Response response = getObjectsDataResponse(objectIds);

        response.then().statusCode(HttpStatus.SC_BAD_REQUEST);

        return response.as(ErrorResponseDto.class);
    }

    private Response getObjectsDataResponse(List<String> objectIds) {
        GetObjectsDataFactory getObjectsDataFactory = new GetObjectsDataFactory();
        GetObjectsDataRequestDto getObjectsDataRequestDto = getObjectsDataFactory.createGetObjectsDataRequestDto(objectIds);

        requestSpecification.addBodyToRequest(getObjectsDataRequestDto);

        return requestOperationsHelper
                .sendPostRequest(requestSpecification.getFilterableRequestSpecification());
    }
}
