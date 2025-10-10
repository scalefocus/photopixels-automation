package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.objectoperations.GetObjectsFavoritesResponseDto;
import com.photopixels.api.dtos.objectoperations.PropertiesResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.FavoritesObjectSteps;
import com.photopixels.api.steps.objectoperations.GetObjectsFavoritesSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.IApiBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.photopixels.constants.Constants.*;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

public class GetObjectsFavoritesTests implements IApiBaseTest {

    private final String fileName = TRAINING_FILE;
    private final int pageSize = 10;
    private String token;
    private String objectId;
    private List<String> objectIds;
    private List<String> files;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();
        objectIds = new ArrayList<>();
        files = new ArrayList<>();
        files.add(COCTAIL_FILE);
        files.add(FRENCH_FRIES_FILE);
        files.add(UNNAMED_FILE);
        objectIds = new ArrayList<>();
        String objectHash = getObjectHash(fileName);

        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                .uploadObject(fileName, objectHash);
        objectId = uploadObjectResponseDto.getId();
        objectIds.add(objectId);
        FavoritesObjectSteps favoritesObjectSteps = new FavoritesObjectSteps(token);
        favoritesObjectSteps.addObjectToFavorites(objectIds, 0);
    }

    @AfterClass(alwaysRun = true)
    public void cleanup() {
        deleteObjects(objectIds, token);
    }

    @Test(description = "Get objects favorites")
    @Description("Successful get of objects favorites")
    @Story("Favorites")
    @Severity(SeverityLevel.CRITICAL)
    public void getObjectsFavoritesTest() {
        GetObjectsFavoritesSteps getObjectsFavoritesSteps = new GetObjectsFavoritesSteps(token);
        GetObjectsFavoritesResponseDto getObjectsFavoritesResponseDto = getObjectsFavoritesSteps.getObjectsFavorites(objectId, 1);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(getObjectsFavoritesResponseDto, "Favorites response is null");
        for (PropertiesResponseDto property : getObjectsFavoritesResponseDto.getProperties()) {
            softAssert.assertFalse(property.getId().isEmpty(), "Object id is not returned");
            softAssert.assertEquals(property.getId(), objectId, "Object id is not the same");
            softAssert.assertFalse(property.getDateCreated().isEmpty(), "Object date created is not returned");
            softAssert.assertFalse(property.getMediaType().isEmpty(), "Object media type is not returned");
            softAssert.assertTrue(property.isFavorite(), "The boolean parameter isFavorite is not true.");
        }
        softAssert.assertAll();
    }

    @Test(description = "Get objects favorites with page size")
    @Description("Get objects favorites with page size only")
    @Story("Favorites")
    @Severity(SeverityLevel.NORMAL)
    public void getObjectsFavoritesWithPageSizeTest() {
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of("UTC"));

        GetObjectsFavoritesSteps getObjectsFavoritesSteps = new GetObjectsFavoritesSteps(token);
        GetObjectsFavoritesResponseDto getObjectsFavoritesResponseDto = getObjectsFavoritesSteps.getObjectsFavorites(null, pageSize);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(getObjectsFavoritesResponseDto, "Object response is not returned");
        softAssert.assertTrue(getObjectsFavoritesResponseDto.getLastId().isEmpty(), "Last object id is not empty");
        softAssert.assertFalse(getObjectsFavoritesResponseDto.getProperties().isEmpty(),
                "Properties are not returned when last object id is not provided");
        LocalDateTime propertyDateTime;
        for (PropertiesResponseDto property : getObjectsFavoritesResponseDto.getProperties()) {
            propertyDateTime = LocalDateTime.parse(property.getDateCreated(), ISO_OFFSET_DATE_TIME);
            softAssert.assertTrue(currentDateTime.isAfter(propertyDateTime),
                    "Date created of object is not before current date");
            softAssert.assertNotNull(property.getId(), "Object property id is not returned");
            softAssert.assertTrue(property.isFavorite(), "Object property isFavorite is not True");
        }
        softAssert.assertAll();
    }

    @Test(description = "Get objects favorites with last object id and page size")
    @Description("Get objects favorites with last object id and page size")
    @Story("Favorites")
    @Severity(SeverityLevel.NORMAL)
    public void getObjectsFavoritesWithLastObjectIdTest() {
        String newerObjectId = "";
        for (String file : files) {
            String objectHash = getObjectHash(file);
            PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
            UploadObjectResponseDto uploadObjectResponseDto = postUploadObjectSteps
                    .uploadObject(file, objectHash);
            newerObjectId = uploadObjectResponseDto.getId();
            objectIds.add(newerObjectId);
        }
        FavoritesObjectSteps favoritesObjectSteps = new FavoritesObjectSteps(token);
        favoritesObjectSteps.addObjectToFavorites(Collections.singletonList(newerObjectId), 0);

        GetObjectsFavoritesSteps getObjectsFavoritesSteps = new GetObjectsFavoritesSteps(token);
        GetObjectsFavoritesResponseDto getObjectsFavoritesResponseDto = getObjectsFavoritesSteps.getObjectsFavorites(objectId, pageSize);

        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/206");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(getObjectsFavoritesResponseDto, "Object response is not returned");
        // Only one favorite object is expected
        softAssert.assertEquals(getObjectsFavoritesResponseDto.getProperties().size(), 1,
                "More than one expected favorite object properties is returned");
        softAssert.assertAll();
    }

    @Test(description = "Get objects favorites with not existing last object id")
    @Description("Get objects favorites with not existing last object id only")
    @Story("Favorites")
    @Severity(SeverityLevel.MINOR)
    public void getEmptyObjectsFavoritesTest() {
        String notExistingId = "notExisting";
        GetObjectsFavoritesSteps getObjectsFavoritesSteps = new GetObjectsFavoritesSteps(token);
        getObjectsFavoritesSteps.getEmptyObjectsFavorites(notExistingId, pageSize);
        // No response body is returned
    }
}
