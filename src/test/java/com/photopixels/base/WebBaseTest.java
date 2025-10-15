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
import org.testng.annotations.BeforeSuite;

public class WebBaseTest implements IBaseTest {

    private DriverUtils driverUtils;
    private ScreenshotHelper screenshotHelper;

    @Getter
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void initSuiteWeb() {
        prepareUsers();
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

            driver.quit();
        }
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
