package com.photopixels.enums;

import static org.testng.Assert.fail;

public enum BrowserEnum {
	CHROME("chrome"), FIREFOX("firefox");

	private String browser;

	BrowserEnum(String brw) {
		browser = brw;
	}

	public String getValue() {
		return browser;
	}

	public static BrowserEnum fromString(String text) {
		for (BrowserEnum b : BrowserEnum.values()) {
			if (b.browser.equalsIgnoreCase(text)) {
				return b;
			}
		}

		fail("Browser name '" + text + "' is not valid!");

		return null;
	}

}
