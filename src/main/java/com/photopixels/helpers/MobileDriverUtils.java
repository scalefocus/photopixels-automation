package com.photopixels.helpers;

import com.photopixels.enums.PlatformEnum;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import lombok.Getter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

public class MobileDriverUtils {

    private static final String MOBILE_CONFIG = "mobile.properties";

    private static AppiumDriverLocalService service;
    private boolean isServiceRequired;
    private String appiumUrl;
    private String appiumHost;
    private int appiumPort;
    private String appName;
    private String appPackage;
    private String activityName;
    private String deviceName;
    private PlatformEnum platform;
    @Getter
    private AppiumDriver driver;

    public MobileDriverUtils() {
        setupMobileDriver();

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

    private void setupMobileDriver() {
        PropertiesUtils propertiesUtils = new PropertiesUtils();
        Properties props = propertiesUtils.loadProps(MOBILE_CONFIG);

        String pl = System.getProperty("platform");

        if (pl == null) {
            pl = props.getProperty("platform");
        }

        platform = PlatformEnum.fromString(pl);

        String host = System.getProperty("appiumServiceHost", System.getenv().get("APPIUM_HOST"));

        appiumPort = Integer.parseInt(System.getProperty("appiumPort",
                System.getenv().getOrDefault("APPIUM_PORT", props.getProperty("appiumPort"))));
        appiumHost = props.getProperty("appiumHost");

        if (host == null) {
            appiumUrl = "http://" + appiumHost + ":" + appiumPort;
        } else {
            appiumUrl = "http://" + host + ":" + appiumPort;
        }

        isServiceRequired = Boolean.parseBoolean(props.getProperty("isAppiumServerNeeded"));

        String app = System.getenv().getOrDefault("APP_NAME", System.getProperty("applicationName"));

        if (app == null) {
            appName = props.getProperty("appName");
        }

        appPackage = System.getenv().getOrDefault("$APP_PACKAGE", props.getProperty("appPackage"));
        activityName = System.getenv().getOrDefault("ACTIVITY_NAME", props.getProperty("activityName"));

        String device = System.getenv().get("deviceName");

        if (device == null) {
            deviceName = props.getProperty("deviceName");
        }
    }

    public AppiumDriver getMobileDriver() {
        URL url = null;

        try {
            url = new URL(appiumUrl);
        } catch (MalformedURLException e) {
            System.out.println("Appium url is not valid!");
        }

        switch (platform) {
            case ANDROID:
                UiAutomator2Options androidOptions = new UiAutomator2Options()
                        .setDeviceName(deviceName)
                        .setApp(appName);

                driver = new AndroidDriver(url, androidOptions);

                break;
            case IOS:
                XCUITestOptions options = new XCUITestOptions()
                        .setDeviceName(deviceName)
                        .setApp(appName);

                driver = new IOSDriver(url, options);

                break;
            default:
                break;
        }

        return driver;
    }


    public void startActivity() {
        driver.executeScript(
                "mobile: startActivity",
                Map.of(
                        "component", String.format("%s/%s", appPackage, activityName)
                ));
    }

    public void clearApp() {
        driver.executeScript(
                "mobile: clearApp",
                Map.of(
                        "appId", appPackage
                ));
    }
}
