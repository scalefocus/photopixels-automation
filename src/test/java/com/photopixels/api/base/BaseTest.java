package com.photopixels.api.base;


import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.helpers.InputDataHelper;
import com.photopixels.api.helpers.PropertiesUtils;
import com.photopixels.api.steps.users.PostLoginSteps;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;

import java.util.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.proxy;

public class BaseTest {

	private static final String CONFIG_PROPS = "config.properties";

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

	protected String getToken() {
		return getToken(inputData.getUsername(), inputData.getPassword());
	}

	protected String getToken(String username, String password) {
		PostLoginSteps postLoginSteps = new PostLoginSteps();
		LoginResponseDto loginResponseDto = postLoginSteps.login(username, password);

		return loginResponseDto.getTokenType() + " " + loginResponseDto.getAccessToken();
	}
}
