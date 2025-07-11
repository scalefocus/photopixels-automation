package com.photopixels.base;

import com.photopixels.helpers.DriverUtils;
import com.photopixels.helpers.ScreenshotHelper;
import com.photopixels.web.pages.LoginPage;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class WebBaseTest implements IBaseTest {

    private DriverUtils driverUtils;
    private ScreenshotHelper screenshotHelper;

    @Getter
    protected WebDriver driver;

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

            driver.quit();
        }
    }

    public LoginPage loadPhotoPixelsApp() {
        driver.get(configProperties.getProperty("webUrl"));

        return new LoginPage(driver);
    }

}
