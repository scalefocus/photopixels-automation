package com.photopixels.base;

import com.photopixels.helpers.MobileDriverUtils;
import com.photopixels.mobile.pages.MobileLoginPage;
import com.photopixels.mobile.pages.ServerConfigPage;
import io.appium.java_client.AppiumDriver;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MobileBaseTest extends BaseTest {

    private static final String SCREENSHOTS_DIR = "target/screenshots";

    private MobileDriverUtils mobileDriverUtils;

    @Getter
    protected AppiumDriver mobileDriver;

    @BeforeClass(alwaysRun = true)
    public void setupBaseClassMobile() {
        mobileDriverUtils = new MobileDriverUtils();

        mobileDriverUtils.startService();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupBaseMethodMobile() {
        mobileDriver = mobileDriverUtils.getMobileDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownMobile(ITestResult result) {
        if (mobileDriver != null) {
            String parameter = "";
            Object[] parameters = result.getParameters();

            for (int i = 0; i < parameters.length; i++) {
                parameter = "_" + parameters[i].toString();
            }

            takeScreenshot(result.getName() + parameter);

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

    public File takeScreenshot(String name) {
        File screenshotFile;
        File outputDir = new File(SCREENSHOTS_DIR);
        Date currentDate = Calendar.getInstance().getTime();
        String currentTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(currentDate);

        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        screenshotFile = new File(outputDir.getAbsolutePath(), currentTime + "_" + name + ".png");

        try {
            File screenshot = ((TakesScreenshot) mobileDriver).getScreenshotAs(OutputType.FILE);
            FileUtils.moveFile(screenshot, screenshotFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return screenshotFile;
    }
}
