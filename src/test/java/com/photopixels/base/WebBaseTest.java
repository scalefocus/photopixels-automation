package com.photopixels.base;

import com.photopixels.helpers.DriverUtils;
import com.photopixels.web.pages.LoginPage;
import com.photopixels.web.pages.email.EmailPage;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WebBaseTest extends BaseTest {

    private static final String SCREENSHOTS_DIR = "target/screenshots";

    private DriverUtils driverUtils;

    @Getter
    protected WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setupBaseClass() {
        driverUtils = new DriverUtils();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupBaseMethod() {
        driver = driverUtils.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (driver != null) {
            String parameter = "";
            Object[] parameters = result.getParameters();

            for (int i = 0; i < parameters.length; i++) {
                parameter = "_" + parameters[i].toString();
            }

            takeScreenshot(result.getName() + parameter);

            driver.quit();
        }
    }

    public LoginPage loadPhotoPixelsApp() {
        driver.get(configProperties.getProperty("webUrl"));

        return new LoginPage(driver);
    }

    public EmailPage loadEmail() {
        driver.get(configProperties.getProperty("emailUrl"));

        return new EmailPage(driver);
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
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.moveFile(screenshot, screenshotFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return screenshotFile;
    }
}
