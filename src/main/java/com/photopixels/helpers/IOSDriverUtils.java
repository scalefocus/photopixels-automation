package com.photopixels.helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class IOSDriverUtils {

    private static final String IOS_CONFIG = "ios.properties";

    private static AppiumDriverLocalService service;

    private boolean isServiceRequired;
    private String appiumUrl;
    private String appiumHost;
    private int appiumPort;

    private String deviceName;
    private String appName;
    private String bundleId;
    private String platformVersion;
    private String udid;
    private String automationName;
    private boolean showXcodeLog;

    @Getter
    private AppiumDriver driver;

    public IOSDriverUtils() {
        setupIOSDriver();
        setupService();
    }

    private void setupService() {
        service = new AppiumServiceBuilder()
                .withIPAddress(appiumHost)
                .usingPort(appiumPort)
                .build();
    }

    public void startService() {
        if (isServiceRequired) {
            service.start();
        }
    }

    public void stopService() {
        if (service != null && isServiceRequired) {
            service.stop();
        }
    }

    private void setupIOSDriver() {
        PropertiesUtils propertiesUtils = new PropertiesUtils();
        Properties props = propertiesUtils.loadProps(IOS_CONFIG);

        String host = System.getProperty("appiumServiceHost");

        appiumPort = Integer.parseInt(props.getProperty("appiumPort"));
        appiumHost = props.getProperty("appiumHost");

        if (host == null) {
            appiumUrl = "http://" + appiumHost + ":" + appiumPort;
        } else {
            appiumUrl = "http://" + host + ":" + appiumPort;
        }

        isServiceRequired = Boolean.parseBoolean(props.getProperty("isAppiumServerNeeded"));

        String device = System.getProperty("deviceName");

        if (device == null) {
            deviceName = props.getProperty("deviceName");
        }

        String app = System.getProperty("applicationName");

        if (app == null) {
            appName = props.getProperty("appName");
        }

        bundleId = props.getProperty("bundleId");
        platformVersion = props.getProperty("platformVersion");
        udid = props.getProperty("udid");
        automationName = props.getProperty("automationName", "XCUITest");
        showXcodeLog = Boolean.parseBoolean(props.getProperty("showXcodeLog", "false"));
    }

    public AppiumDriver getIOSDriver() {

        URL url = null;

        try {
            url = new URL(appiumUrl);
        } catch (MalformedURLException e) {
            System.out.println("Appium url is not valid!");
        }

        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setAutomationName(automationName)
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .setUdid(udid)
                .setShowXcodeLog(showXcodeLog);

        if (bundleId != null && !bundleId.isEmpty()) {
            options.setBundleId(bundleId);
        } else if (appName != null) {
            options.setApp(appName);
        }

        driver = new IOSDriver(url, options);

        return driver;
    }

    public void terminateApp() {
        driver.executeScript(
                "mobile: terminateApp",
                Map.of(
                        "bundleId", bundleId
                ));
    }

    public void activateApp() {
        driver.executeScript(
                "mobile: activateApp",
                Map.of(
                        "bundleId", bundleId
                ));
    }

    public void clearIOSApp() {
        terminateApp();
        activateApp();
    }
}