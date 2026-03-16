package com.photopixels.base;

import com.photopixels.api.dtos.objectoperations.GetObjectsResponseDto;
import com.photopixels.api.dtos.objectoperations.GetTrashedObjectsResponseDto;
import com.photopixels.api.steps.objectoperations.GetObjectsSteps;
import com.photopixels.api.steps.objectoperations.GetTrashedObjectsSteps;
import com.photopixels.api.steps.objectoperations.PostTrashDeletePermanentSteps;
import com.photopixels.api.steps.objectoperations.PostTrashObjectsSteps;
import com.photopixels.helpers.DriverUtils;
import com.photopixels.helpers.ScreenshotHelper;
import com.photopixels.web.pages.LoginPage;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WebBaseTest implements IBaseTest {

    private DriverUtils driverUtils;
    private ScreenshotHelper screenshotHelper;

    @Getter
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void initSuiteWeb() {
        boolean isPrepareUsers = Boolean.parseBoolean(System.getProperty("isPrepareUsers"));
        if (isPrepareUsers) {
            prepareUsers();
        }
    }

    @BeforeClass(alwaysRun = true)
    public void setupBaseClass() {
        driverUtils = new DriverUtils();
        screenshotHelper = new ScreenshotHelper();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupBaseMethod() {
        driver = driverUtils.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null) {

            screenshotHelper.saveScreenshot(result, driver);

            if (!result.isSuccess()) {
                deleteAllMediaOnFailure();
            }

            driver.quit();
        }
    }

    private void deleteAllMediaOnFailure() {
        try {
            IBaseTest.configureRestAssured();
            String token = getUserToken();

            List<String> activeObjectIds = getAllActiveObjectIds(token);
            if (!activeObjectIds.isEmpty()) {
                PostTrashObjectsSteps trashSteps = new PostTrashObjectsSteps(token);
                trashSteps.trashObjects(activeObjectIds);
            }

            List<String> trashedObjectIds = getAllTrashedObjectIds(token);
            if (!trashedObjectIds.isEmpty()) {
                PostTrashDeletePermanentSteps deletePermanentSteps = new PostTrashDeletePermanentSteps(token);
                deletePermanentSteps.deletePermanentFromTrash(trashedObjectIds);
            }

            System.out.println("Cleanup completed: Deleted " + activeObjectIds.size() + " active and "
                    + trashedObjectIds.size() + " trashed objects after test failure.");
        } catch (Exception e) {
            System.err.println("Warning: Failed to cleanup media after test failure: " + e.getMessage());
        }
    }

    private List<String> getAllActiveObjectIds(String token) {
        List<String> allObjectIds = new ArrayList<>();
        String lastId = null;
        int pageSize = 100;

        try {
            GetObjectsSteps getObjectsSteps = new GetObjectsSteps(token);

            do {
                GetObjectsResponseDto response = getObjectsSteps.getObjects(lastId, pageSize);

                if (response.getProperties() != null && !response.getProperties().isEmpty()) {
                    List<String> ids = response.getProperties().stream()
                            .map(p -> p.getId())
                            .collect(Collectors.toList());
                    allObjectIds.addAll(ids);
                    lastId = response.getLastId();
                } else {
                    break;
                }
            } while (lastId != null && !lastId.isEmpty());
        } catch (Throwable e) {
        }

        return allObjectIds;
    }

    private List<String> getAllTrashedObjectIds(String token) {
        List<String> allTrashedIds = new ArrayList<>();
        String lastId = null;
        int pageSize = 100;

        try {
            GetTrashedObjectsSteps getTrashedSteps = new GetTrashedObjectsSteps(token);

            do {
                GetTrashedObjectsResponseDto response = getTrashedSteps.getTrashedObjects(lastId, pageSize);

                if (response.getProperties() != null && !response.getProperties().isEmpty()) {
                    List<String> ids = response.getProperties().stream()
                            .map(p -> p.getId())
                            .collect(Collectors.toList());
                    allTrashedIds.addAll(ids);
                    lastId = response.getLastId();
                } else {
                    break;
                }
            } while (lastId != null && !lastId.isEmpty());
        } catch (Throwable e) {
        }

        return allTrashedIds;
    }

    public LoginPage loadPhotoPixelsApp() {
        String webUrl = System.getProperty("webUrl");

        if (webUrl == null) {
            webUrl = configProperties.getProperty("webUrl");
        }
        driver.get(webUrl);

        return new LoginPage(driver);
    }

}
