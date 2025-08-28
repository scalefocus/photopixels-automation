package com.photopixels.base;

import com.photopixels.enums.PlatformEnum;
import com.photopixels.helpers.MobileDriverUtils;
import com.photopixels.helpers.ScreenshotHelper;
import com.photopixels.mobile.pages.MobileLoginPage;
import com.photopixels.mobile.pages.ServerConfigPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import lombok.Getter;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;

public class MobileBaseTest implements IBaseTest {

    private MobileDriverUtils mobileDriverUtils;
    private ScreenshotHelper screenshotHelper;

    @Getter
    protected AppiumDriver mobileDriver;

    @BeforeClass(alwaysRun = true)
    public void setupBaseClassMobile() {
        mobileDriverUtils = new MobileDriverUtils();
        screenshotHelper = new ScreenshotHelper();

        mobileDriverUtils.startService();

        mobileDriver = mobileDriverUtils.getMobileDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupBaseMethodMobile() {
        mobileDriverUtils.startActivity();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMobile(ITestResult result) {
        if (mobileDriver != null) {
            screenshotHelper.saveScreenshot(result, mobileDriver);

            mobileDriverUtils.clearApp();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownBaseClassMobile() {
        if (mobileDriver != null) {
            mobileDriver.quit();
        }

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

    public void pushImageToGallery(String localFilePath, String fileName) throws Exception {
        // Upload the file to /storage/emulated/0/Download/ for Android Platform
        if (mobileDriver.getCapabilities().getPlatformName().name().equalsIgnoreCase(PlatformEnum.ANDROID.toString())) {
            ((AndroidDriver) mobileDriver).pushFile("/storage/emulated/0/Download/" + fileName, new File(localFilePath));
        }
    }
}
