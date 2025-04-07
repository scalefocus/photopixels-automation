package com.photopixels.base;

import com.photopixels.helpers.MobileDriverUtils;
import com.photopixels.helpers.ScreenshotHelper;
import com.photopixels.mobile.pages.MobileLoginPage;
import com.photopixels.mobile.pages.ServerConfigPage;
import io.appium.java_client.AppiumDriver;
import lombok.Getter;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class MobileBaseTest extends BaseTest {

    private MobileDriverUtils mobileDriverUtils;
    private ScreenshotHelper screenshotHelper;

    @Getter
    protected AppiumDriver mobileDriver;

    @BeforeClass(alwaysRun = true)
    public void setupBaseClassMobile() {
        mobileDriverUtils = new MobileDriverUtils();
        screenshotHelper = new ScreenshotHelper();

        mobileDriverUtils.startService();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupBaseMethodMobile() {
        mobileDriver = mobileDriverUtils.getMobileDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMobile(ITestResult result) {
        if (mobileDriver != null) {
            screenshotHelper.saveScreenshot(result, mobileDriver);

            mobileDriver.quit();
        }
    }

    @AfterClass
    public void tearDownBaseClassMobile() {
        mobileDriverUtils.stopService();
    }

    public MobileLoginPage loadPhotoPixelsApp() {
        String serverAddress = configProperties.getProperty("webUrl");

        ServerConfigPage serverConfigPage = new ServerConfigPage(getMobileDriver());

        if (!serverConfigPage.isServerAddressFilled()) {
            serverConfigPage.fillServerAddress(serverAddress);
        }

        return serverConfigPage.clickNextButton();
    }
}
