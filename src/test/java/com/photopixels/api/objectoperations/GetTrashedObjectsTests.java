package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.errors.ErrorResponseDto;
import com.photopixels.api.dtos.objectoperations.GetTrashedObjectsResponseDto;
import com.photopixels.api.dtos.objectoperations.TrashedObjectPropertyDto;
import com.photopixels.api.steps.objectoperations.GetTrashedObjectsSteps;
import com.photopixels.base.ApiBaseTest;
import com.photopixels.enums.ErrorMessagesEnum;
import com.photopixels.listeners.StatusTestListener;
import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.photopixels.constants.ErrorMessageConstants.VALIDATION_ERRORS_TITLE;

@Listeners(StatusTestListener.class)
@Feature("Object operations")
public class GetTrashedObjectsTests extends ApiBaseTest {

    private String token;
    int pageSize = 30;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();
    }

    @Test(description = "Get trashed objects with valid page size")
    @Description("Positive test: Get trashed objects with only valid page size")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.CRITICAL)
    public void getTrashedObjectsSuccessfullyTest() {

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

    @Test(description = "Get trashed objects with valid last object id")
    @Description("Verify API returns no content when lastId is the most recent object")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.NORMAL)
    public void getTrashedObjectsWithValidLastIdTest() {
        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);

        // Step 1: Get initial list of trashed objects
        GetTrashedObjectsResponseDto initialResponse = getTrashedObjectsSteps.getTrashedObjects(null, pageSize);
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(initialResponse, "Initial response is null");

        List<TrashedObjectPropertyDto> properties = initialResponse.getProperties();
        softAssert.assertFalse(properties.isEmpty(), "Initial list of properties is empty");

        // Step 2: Use the last object's ID as lastId
        String lastId = properties.get(properties.size() - 1).getId();

        // Step 3: Expect 204 No Content
        getTrashedObjectsSteps.getRawTrashedObjectsResponseExpectingNoContent(lastId, pageSize);

        softAssert.assertAll();
    }


    @Test(description = "Get trashed objects with non-existing last object id")
    @Description("Verify API returns no content when lastId does not exist in the system")
    @Story("Get Trashed Objects")
    @Severity(SeverityLevel.MINOR)
    public void getTrashedObjectsWithNonExistingLastIdTest() {
        String nonExistingId = "nonExisting_123";

        GetTrashedObjectsSteps getTrashedObjectsSteps = new GetTrashedObjectsSteps(token);

        getTrashedObjectsSteps.getRawTrashedObjectsResponseExpectingNoContent(nonExistingId, pageSize);

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
        softAssert.assertEquals(response.getTitle(), VALIDATION_ERRORS_TITLE, "Unexpected error title");
        softAssert.assertEquals(response.extractErrorMessageByKey(ErrorMessagesEnum.PAGE_SIZE_INVALID.getKey()), ErrorMessagesEnum.PAGE_SIZE_INVALID.getErrorMessage(),
                "Unexpected validation message for PageSize");
        softAssert.assertNotNull(response.getTraceId(), "traceId is null");

        softAssert.assertAll();
    }
}