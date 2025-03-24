package com.photopixels.helpers;

import org.testng.Reporter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	public Properties loadProps(String file) {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream in = classLoader.getResourceAsStream(file);

		Properties props = new Properties();

		try {
			props.load(in);
		} catch (IOException e) {
			Reporter.log(String.format("Could not load properties %s", file));
		}

		return props;
	}

}
