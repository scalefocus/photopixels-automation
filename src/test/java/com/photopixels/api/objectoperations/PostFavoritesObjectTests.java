package com.photopixels.api.objectoperations;

import com.photopixels.api.dtos.objectoperations.FavoritesResponseDto;
import com.photopixels.api.dtos.objectoperations.UploadObjectResponseDto;
import com.photopixels.api.steps.objectoperations.FavoritesObjectSteps;
import com.photopixels.api.steps.objectoperations.PostUploadObjectSteps;
import com.photopixels.base.IApiBaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostFavoritesObjectTests implements IApiBaseTest {

    private String token;
    private List<String> objectIds;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        token = getUserToken();
        objectIds = new ArrayList<>();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (!objectIds.isEmpty()) {
            deleteObjects(objectIds, token);
            objectIds.clear();
        }
    }

    @Test(dataProvider = "files", description = "Add to favorites by id")
    @Description("Positive test: Successfully add object to favorites by id")
    @Story("Favorites")
    @Severity(SeverityLevel.CRITICAL)
    public void addObjectToFavoritesSuccessfullyTest(String filePath) {
        String objectHash = getObjectHash(filePath);
        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObject = postUploadObjectSteps.uploadObject(filePath, objectHash);

        String objectId = uploadObject.getId();
        objectIds.add(objectId);

        FavoritesObjectSteps favoritesObjectSteps = new FavoritesObjectSteps(token);
        FavoritesResponseDto favoritesResponse = favoritesObjectSteps.addObjectToFavorites(objectIds, 0);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(favoritesResponse, "Favorites response is null");
        softAssert.assertTrue(favoritesResponse.isSuccess(), "isSuccessful is false");

        softAssert.assertAll();
    }

    @Test(description = "Add non existing object id to favorites")
    @Description("Validation of adding to favorites an object with not existing id")
    @Story("Favorites")
    @Severity(SeverityLevel.MINOR)
    public void addNonExistingObjectToFavoritesTest() {
        FavoritesObjectSteps favoritesObjectSteps = new FavoritesObjectSteps(token);
        FavoritesResponseDto favoritesResponse = favoritesObjectSteps.addObjectToFavorites(Collections.singletonList("NotExisting"), 0);

        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/205");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(favoritesResponse, "Favorites response is null");
        softAssert.assertFalse(favoritesResponse.isSuccess(), "isSuccessful is true");

        softAssert.assertAll();
    }

    @Test(dataProvider = "files", description = "Remove object from favorites by id")
    @Description("Positive test: Successfully remove object from favorites by id")
    @Story("Favorites")
    @Severity(SeverityLevel.CRITICAL)
    public void removeObjectFromFavoritesSuccessfullyTest(String filePath) {
        String objectHash = getObjectHash(filePath);
        PostUploadObjectSteps postUploadObjectSteps = new PostUploadObjectSteps(token);
        UploadObjectResponseDto uploadObject = postUploadObjectSteps.uploadObject(filePath, objectHash);

        String objectId = uploadObject.getId();
        objectIds.add(objectId);

        FavoritesObjectSteps favoritesObjectSteps = new FavoritesObjectSteps(token);
        favoritesObjectSteps.addObjectToFavorites(objectIds, 0);
        FavoritesResponseDto removeFavoritesResponse = favoritesObjectSteps.removeObjectFromFavorites(objectIds, 0);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(removeFavoritesResponse, "Favorites response is null");
        softAssert.assertTrue(removeFavoritesResponse.isSuccess(), "isSuccessful is false");

        softAssert.assertAll();
    }

    @Test(description = "Remove non existing object id from favorites")
    @Description("Validation of removing an non existing object from favorites")
    @Story("Favorites")
    @Severity(SeverityLevel.MINOR)
    public void removeNonExistingObjectFromFavoritesTest() {
        FavoritesObjectSteps favoritesObjectSteps = new FavoritesObjectSteps(token);
        FavoritesResponseDto removeFavoritesResponse = favoritesObjectSteps.removeObjectFromFavorites(Collections.singletonList("NotExisting"), 0);

        addIssueLinkToAllureReport("https://github.com/scalefocus/photopixels/issues/205");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(removeFavoritesResponse, "Favorites response is null");
        softAssert.assertFalse(removeFavoritesResponse.isSuccess(), "isSuccessful is true");

        softAssert.assertAll();
    }
}
