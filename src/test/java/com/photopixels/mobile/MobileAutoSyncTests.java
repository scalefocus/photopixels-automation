package com.photopixels.mobile;

import com.photopixels.api.dtos.objectoperations.GetObjectDataResponseDto;
import com.photopixels.api.dtos.objectoperations.GetObjectsResponseDto;
import com.photopixels.api.steps.objectoperations.GetObjectDataSteps;
import com.photopixels.api.steps.objectoperations.GetObjectsSteps;
import com.photopixels.base.IApiBaseTest;
import com.photopixels.base.MobileBaseTest;
import com.photopixels.enums.MobileNetworkOptionsEnum;
import com.photopixels.mobile.pages.HomePage;
import com.photopixels.mobile.pages.MobileLoginPage;
import com.photopixels.mobile.pages.SettingsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Collections;

import static com.photopixels.constants.Constants.FRENCH_FRIES_FILE;

public class MobileAutoSyncTests extends MobileBaseTest implements IApiBaseTest {

    private String email;
    private String password;
    private String token;
    private String objectId;

    @BeforeClass(alwaysRun = true)
    public void setup() {
        email = inputData.getUsername();
        password = inputData.getPassword();
        token = getUserToken();
        try {
            pushImageToGallery(FRENCH_FRIES_FILE, "french-fries.jpg");
        } catch (Exception e) {
            System.out.println("Image could not be pushed to device!" + e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        deleteObjects(Collections.singletonList(objectId), token);
    }

    @Test(description = "Auto Sync photos only through Wi-Fi")
    @Description("Auto Sync photos only through Wi-Fi")
    @Story("Auto Sync")
    @Severity(SeverityLevel.CRITICAL)
    public void autoSyncThroughWiFiTest() {
        setNetworkConnection(MobileNetworkOptionsEnum.WIFI_AND_DATA_ON);
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        HomePage homePage = loginPage.login(email, password);
        SettingsPage settingsPage = homePage.navigateToSettings();
        settingsPage.turnRequireWiFiForSyncOnOff(true);
        homePage.clickHomeButton();
        homePage.clickSyncMediaButton();
        homePage.clickAllowNotificationsButton();
        homePage.clickAllowAllPhotosAccessButton();
        homePage.waitForUploadToFinish();

        String objectHash = getObjectHash(FRENCH_FRIES_FILE);

        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        GetObjectsResponseDto getObjectsResponseDto = getObjectsSteps.getObjects(null, 1);
        objectId = getObjectsResponseDto.getProperties().get(0).getId();

        GetObjectDataSteps getObjectDataSteps = new GetObjectDataSteps(token);
        GetObjectDataResponseDto getObjectDataResponseDto = getObjectDataSteps.getObjectData(objectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getObjectDataResponseDto, "Object data is not returned");
        softAssert.assertEquals(getObjectDataResponseDto.getId(), objectId, "Object data id is not correct");
        softAssert.assertNotNull(getObjectDataResponseDto.getThumbnail(), "Object data thumbnail is not returned");
        softAssert.assertNotNull(getObjectDataResponseDto.getContentType(), "Object data content type is not returned");
        softAssert.assertNotNull(getObjectDataResponseDto.getDateCreated(), "Date created should not be null");
        softAssert.assertEquals(getObjectDataResponseDto.getOriginalHash(), objectHash,
                "Original hash does not match expected object hash");
        softAssert.assertEquals(getObjectDataResponseDto.getHash(), objectHash.substring(0, objectHash.length() - 1),
                "Object data hash is not correct");

        softAssert.assertAll();
    }

    @Test(description = "Auto Sync photos only through Mobile Data Only")
    @Description("Auto Sync photos only through Mobile Data Only")
    @Story("Auto Sync")
    @Severity(SeverityLevel.CRITICAL)
    public void autoSyncThroughMobileDataTest() {
        setNetworkConnection(MobileNetworkOptionsEnum.DATA_ON);
        MobileLoginPage loginPage = loadPhotoPixelsApp();
        HomePage homePage = loginPage.login(email, password);
        SettingsPage settingsPage = homePage.navigateToSettings();
        settingsPage.turnRequireWiFiForSyncOnOff(false);
        homePage.clickHomeButton();
        homePage.clickSyncMediaButton();
        homePage.clickAllowNotificationsButton();
        homePage.clickAllowAllPhotosAccessButton();
        homePage.waitForUploadToFinish();

        String objectHash = getObjectHash(FRENCH_FRIES_FILE);

        GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);
        GetObjectsResponseDto getObjectsResponseDto = getObjectsSteps.getObjects(null, 1);
        objectId = getObjectsResponseDto.getProperties().get(0).getId();

        GetObjectDataSteps getObjectDataSteps = new GetObjectDataSteps(token);
        GetObjectDataResponseDto getObjectDataResponseDto = getObjectDataSteps.getObjectData(objectId);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertNotNull(getObjectDataResponseDto, "Object data is not returned");
        softAssert.assertEquals(getObjectDataResponseDto.getId(), objectId, "Object data id is not correct");
        softAssert.assertNotNull(getObjectDataResponseDto.getThumbnail(), "Object data thumbnail is not returned");
        softAssert.assertNotNull(getObjectDataResponseDto.getContentType(), "Object data content type is not returned");
        softAssert.assertNotNull(getObjectDataResponseDto.getDateCreated(), "Date created should not be null");
        softAssert.assertEquals(getObjectDataResponseDto.getOriginalHash(), objectHash,
                "Original hash does not match expected object hash");
        softAssert.assertEquals(getObjectDataResponseDto.getHash(), objectHash.substring(0, objectHash.length() - 1),
                "Object data hash is not correct");

        softAssert.assertAll();
    }
}
