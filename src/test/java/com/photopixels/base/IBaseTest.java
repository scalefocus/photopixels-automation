package com.photopixels.base;


import com.photopixels.helpers.InputDataHelper;
import com.photopixels.helpers.PropertiesUtils;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.AfterSuite;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.Properties;

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
