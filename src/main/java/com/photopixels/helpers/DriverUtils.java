package com.photopixels.helpers;

import com.photopixels.enums.BrowserEnum;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class DriverUtils {

	private static final String DRIVER_CONFIG = "driver.properties";

	private BrowserEnum browser;
	private boolean isGridRequired;
	private WebDriver driver;
	private String gridUrl;

	public DriverUtils() {
		setupDriver();
	}

	public String getBrowserName() {
		return browser.getValue();
	}

	public WebDriver getDriver() {
		if (isGridRequired) {
			getRemoteDriver();
		} else {
			getLocalDriver();
		}

		driver.manage().window().maximize();

		return driver;
	}

	private void setupDriver() {
		PropertiesUtils propertiesUtils = new PropertiesUtils();
		Properties props = propertiesUtils.loadProps(DRIVER_CONFIG);

		String brw = System.getProperty("browserName");

		if (brw == null) {
			brw = props.getProperty("browser");
		}

		browser = BrowserEnum.fromString(brw);

		isGridRequired = Boolean.parseBoolean(props.getProperty("useSeleniumGrid"));

		if (isGridRequired) {
			String host = System.getProperty("seleniumHubHost");

			if (host == null) {
				gridUrl = props.getProperty("gridUrl");
			} else {
				gridUrl = "http://" + host + ":4444/wd/hub";
			}
		}
	}

	private void getRemoteDriver() {
		URL url = null;

		try {
			url = new URL(gridUrl);
		} catch (MalformedURLException e) {
			System.out.println("Grid url is not valid!");
		}

		switch (browser) {
		case CHROME:
			ChromeOptions chromeOptions = new ChromeOptions();

			driver = new RemoteWebDriver(url, chromeOptions);
			break;
		case FIREFOX:
			FirefoxOptions firefoxOptions = new FirefoxOptions();

			driver = new RemoteWebDriver(url, firefoxOptions);
			break;
		default:
			break;
		}
	}

	private void getLocalDriver() {
		switch (browser) {
		case CHROME:
			WebDriverManager.chromedriver().setup();

			driver = new ChromeDriver();
			break;
		case FIREFOX:
			WebDriverManager.firefoxdriver().setup();

			driver = new FirefoxDriver();
			break;
		default:
			break;
		}
	}

}
