package com.photopixels.base;


import com.photopixels.helpers.InputDataHelper;
import com.photopixels.helpers.PropertiesUtils;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {

	private static final String CONFIG_PROPS = "config.properties";
	private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

	protected static InputDataHelper inputData;
	protected static Properties configProperties;

	@BeforeSuite(alwaysRun = true)
	public void initSuiteBase() {
		configProperties = new PropertiesUtils().loadProps(CONFIG_PROPS);

		inputData = new InputDataHelper();
	}

	@AfterSuite(alwaysRun = true)
	public void prepareReport() {
		String allureResults = System.getProperty("allurePath");

		if (allureResults == null) {
			PropertiesUtils propertiesUtils = new PropertiesUtils();
			Properties props = propertiesUtils.loadProps("allure.properties");

			allureResults = props.getProperty("allure.results.directory");
		}

		copyReportHistory(allureResults);
		generateEnvironmentFile(allureResults);
	}

	protected void attachIssueLinkToAllureReport(String issueLink) {
		Allure.addAttachment("Github issue link", "text/uri-list", issueLink);
	}

	private void copyReportHistory(String allureResults) {
		String allureHistoryFolder = "allure-history";
		File history = new File(allureHistoryFolder);

		if (history.exists()) {
			String destinationFolder = allureResults + FILE_SEPARATOR + "history";
			File destination = new File(destinationFolder);

			try {
				FileUtils.copyDirectory(history, destination);
			} catch (IOException e) {
				System.out.println("History folder could not be moved!");
			}
		}
	}

	private void generateEnvironmentFile(String allureResults) {

		String path = allureResults + FILE_SEPARATOR + "environment.properties";

		File file = new File(path);

		if (!file.exists()) {
			try {
				file.getParentFile().mkdir();
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Environment file could not be created!");
			}
		}

		FileOutputStream out = null;

		try {
			out = new FileOutputStream(file);

			Properties prop = new Properties();

			prop.setProperty("environment", baseURI);

			prop.store(out, "Setting environment data");

		} catch (IOException ie) {
			System.out.println("Environment file not found!");
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				System.out.println("Error while creating environment file!");
			}
		}
	}
}
