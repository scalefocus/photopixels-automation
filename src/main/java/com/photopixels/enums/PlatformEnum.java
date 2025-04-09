package com.photopixels.enums;

import static org.testng.Assert.fail;

public enum PlatformEnum {
	ANDROID("android"), IOS("ios");

	private String platform;

	PlatformEnum(String pl) {
		platform = pl;
	}

	public String getValue() {
		return platform;
	}

	public static PlatformEnum fromString(String text) {
		for (PlatformEnum p : PlatformEnum.values()) {
			if (p.platform.equalsIgnoreCase(text)) {
				return p;
			}
		}

		fail("Platform '" + text + "' is not valid!");

		return null;
	}

}
