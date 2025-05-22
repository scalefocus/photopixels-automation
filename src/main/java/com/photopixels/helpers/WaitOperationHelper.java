package com.photopixels.helpers;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

public class WaitOperationHelper {

	public static final int ONE_SECOND_IN_MS = 1000;
	public static final int HALF_SECOND_IN_MS = 500;
	public static final int SIXTY_SECONDS = 60;

	private FluentWait<WebDriver> wait;

	public WaitOperationHelper(WebDriver driver) {
		wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(SIXTY_SECONDS));
		wait.pollingEvery(Duration.ofMillis(HALF_SECOND_IN_MS)).ignoring(NoSuchElementException.class);
	}

	public void waitMs() {
		try {
			Thread.sleep(ONE_SECOND_IN_MS);
		} catch (InterruptedException e) {
			// No need to do anything here
		}
	}

	public void waitForElementToBeVisible(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void waitForElementToBeClickable(WebElement element) {
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}

}
