package com.photopixels.helpers;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ScreenshotHelper {

    private static final String SCREENSHOTS_DIR = "target/screenshots";

    public void saveScreenshot(ITestResult result, WebDriver driver) {
        String parameter = "";
        Object[] parameters = result.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            parameter = "_" + parameters[i].toString();
        }

        takeScreenshot(result.getName() + parameter, driver);
    }

    public File takeScreenshot(String name, WebDriver driver) {
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
