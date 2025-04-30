package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetTrashedObjectsResponseDto;
import com.photopixels.api.dtos.objectoperations.TrashedObjectPropertyDto;
import com.photopixels.api.steps.objectoperations.GetTrashedObjectsSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class GetTrashedObjectsTests extends ApiBaseTest {

    private String token;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();
    }

    @Test(description = "Get trashed objects with valid page size")
    @Description("Positive test: Get trashed objects with only valid page size")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.CRITICAL)
    public void getTrashedObjectsSuccessfullyTest() {
        int pageSize = 30;

        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);
        GetTrashedObjectsResponseDto response = getTrashedObjectsSteps.getTrashedObjects(null, pageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Response is null");
        softAssert.assertNotNull(response.getProperties(), "Properties list is null");

        List<TrashedObjectPropertyDto> properties = response.getProperties();
        for (TrashedObjectPropertyDto property : properties) {
            softAssert.assertNotNull(property.getId(), "Property ID is null");
            softAssert.assertFalse(property.getId().isEmpty(), "Property ID is empty");

            softAssert.assertNotNull(property.getDateCreated(), "dateCreated is null");
            softAssert.assertFalse(property.getDateCreated().isEmpty(), "dateCreated is empty");

            softAssert.assertNotNull(property.getDateTrashed(), "dateTrashed is null");
            softAssert.assertFalse(property.getDateTrashed().isEmpty(), "dateTrashed is empty");
        }

        softAssert.assertAll();
    }

    @Test(description = "Get trashed objects with invalid page size")
    @Description("Validation of get trashed objects with invalid (non-integer) pageSize")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.MINOR)
    public void getTrashedObjectsWithInvalidPageSizeTest() {
        String invalidPageSize = "avc";

        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);
        ErrorResponseDto response = getTrashedObjectsSteps.getTrashedObjectsWithInvalidPageSize(invalidPageSize);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(response, "Error response is null");
        softAssert.assertEquals(response.getTitle(), "One or more validation errors occurred.", "Unexpected error title");
        softAssert.assertEquals(response.extractErrorMessageByKey("PageSize"), "The value 'avc' is not valid for PageSize.");
        softAssert.assertNotNull(response.getTraceId(), "traceId is null");

        softAssert.assertAll();
    }
}