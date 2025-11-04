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

public interface IBaseTest {

	String CONFIG_PROPS = "config.properties";
	String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

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

	@AfterSuite(alwaysRun = true)
    default void prepareReport() {
		String allureResults = System.getProperty("allurePath");

		if (allureResults == null) {
			PropertiesUtils propertiesUtils = new PropertiesUtils();
			Properties props = propertiesUtils.loadProps("allure.properties");

			allureResults = props.getProperty("allure.results.directory");
		}

		copyReportHistory(allureResults);
		generateEnvironmentFile(allureResults);
		generateCategoriesFile(allureResults);
	}

	static void configureRestAssured() {
		baseURI = getBaseUri();
	}

	default void prepareUsers() {
		configureRestAssured();
		String token = getAdminToken();
		boolean isRegistrationEnabled = getRegistrationStatus();
		if (!isRegistrationEnabled) {
			PostDisableRegistrationSteps postDisableRegistrationSteps = new PostDisableRegistrationSteps(token);
			postDisableRegistrationSteps.disableRegistration(true);
		}
		GetUsersSteps getUsersSteps = new GetUsersSteps(token);
		GetUserResponseDto[] getUserResponseDto = getUsersSteps.getUsers();

		if (Arrays.stream(getUserResponseDto).noneMatch(
				u -> u.getEmail().equals(inputData.getUsername()))) {
			registerUser(inputData.getUserFullName(), inputData.getUsername(), inputData.getPassword(), UserRolesEnum.USER);
		}
	}

	default boolean getRegistrationStatus() {
		return getStatus();
	}

	default boolean getStatus() {
		GetStatusSteps getStatusSteps = new GetStatusSteps();
		GetStatusResponseDto getStatusResponseDto = getStatusSteps.getStatus();

		return getStatusResponseDto.getRegistration();
	}

	default String getAdminToken() {
		return getToken(inputData.getUsernameAdmin(), inputData.getPasswordAdmin());
	}

	default String getToken(String username, String password) {
		PostLoginSteps postLoginSteps = new PostLoginSteps();
		LoginResponseDto loginResponseDto = postLoginSteps.login(username, password);

		return loginResponseDto.getTokenType() + " " + loginResponseDto.getAccessToken();
	}

	default String getUserToken() {
		return getToken(inputData.getUsername(), inputData.getPassword());
	}

	default void registerUser(String username, String email, String password, UserRolesEnum role) {
		String token = getAdminToken();

		PostRegisterUserAdminSteps postRegisterUserAdminSteps = new PostRegisterUserAdminSteps(token);
		postRegisterUserAdminSteps.registerUserAdmin(username, email, password, role);
	}

	 default void addIssueLinkToAllureReport(String issueLink) {
		Allure.issue(issueLink, issueLink);
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

		File file = createFile(path);

		FileOutputStream out = null;

		try {
			out = new FileOutputStream(file);

			Properties prop = new Properties();

			prop.setProperty("environment", baseUri);

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

	private void generateCategoriesFile(String allureResults) {

		String path = allureResults + FILE_SEPARATOR + "categories.json";

		File file = createFile(path);

		String content = "[\n" +
				"  {\n" +
				"    \"name\": \"Known issues\",\n" +
				"    \"messageRegex\": \".*Known issue.*\"\n" +
				"  }\n" +
				"]";

		try (PrintWriter out = new PrintWriter(file)) {
			out.println(content);
		} catch (FileNotFoundException e) {
			System.out.println("Categories file not found!");
        }
    }

	private File createFile(String path) {
		File file = new File(path);

		if (!file.exists()) {
			try {
				file.getParentFile().mkdir();
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("File '" + path + "' could not be created!");
			}
		}

		return file;
	}
}
