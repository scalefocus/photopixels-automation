package com.photopixels.base;


import com.photopixels.api.dtos.admin.GetUserResponseDto;
import com.photopixels.api.dtos.status.GetStatusResponseDto;
import com.photopixels.api.dtos.users.LoginResponseDto;
import com.photopixels.api.steps.admin.GetUsersSteps;
import com.photopixels.api.steps.admin.PostDisableRegistrationSteps;
import com.photopixels.api.steps.admin.PostRegisterUserAdminSteps;
import com.photopixels.api.steps.status.GetStatusSteps;
import com.photopixels.api.steps.users.PostLoginSteps;
import com.photopixels.enums.UserRolesEnum;
import com.photopixels.helpers.InputDataHelper;
import com.photopixels.helpers.PropertiesUtils;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterSuite;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;

public interface IosBaseTest {

	String CONFIG_PROPS = "config.properties";
	InputDataHelper inputData = new InputDataHelper();
	Properties configProperties = new PropertiesUtils().loadProps(CONFIG_PROPS);
	String baseUri = getBaseUri();

	static String getBaseUri() {
		String baseUri = System.getProperty("baseUri");

		if (baseUri == null || baseUri.isEmpty()) {
			baseUri = configProperties.getProperty("baseUri");
		}

		return baseUri;
	}
}
