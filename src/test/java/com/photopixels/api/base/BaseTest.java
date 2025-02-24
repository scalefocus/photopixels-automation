package com.photopixels.api.base;


import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.helpers.InputDataHelper;
import com.photopixels.api.helpers.PropertiesUtils;
import com.photopixels.api.steps.users.PostLoginSteps;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.proxy;

public class BaseTest {

	private static final String CONFIG_PROPS = "config.properties";
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");

	protected static InputDataHelper inputData;

	@BeforeSuite(alwaysRun = true)
	public void initSuiteBase() {
		baseURI = System.getProperty("baseUri");

		Properties props = new PropertiesUtils().loadProps(CONFIG_PROPS);

		if (baseURI == null || baseURI.isEmpty()) {
			baseURI = props.getProperty("baseUri");

			if (Boolean.parseBoolean(props.getProperty("isProxyNeeded"))) {
				proxy(props.getProperty("proxyHost"), Integer.parseInt(props.getProperty("proxyPort")));
				RestAssured.useRelaxedHTTPSValidation();
			}
		}

		String logEnabled = System.getProperty("isLogEnabled");
		if (logEnabled == null) {
			logEnabled = props.getProperty("isLogEnabled");
		}

		if (Boolean.parseBoolean(logEnabled)) {
			RestAssured.replaceFiltersWith(new RequestLoggingFilter(LogDetail.ALL),
					new ResponseLoggingFilter(LogDetail.ALL));
		} else {
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		}

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

	protected String getToken() {
		return getToken(inputData.getUsername(), inputData.getPassword());
	}

	protected String getToken(String username, String password) {
		PostLoginSteps postLoginSteps = new PostLoginSteps();
		LoginResponseDto loginResponseDto = postLoginSteps.login(username, password);

		return loginResponseDto.getTokenType() + " " + loginResponseDto.getAccessToken();
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
