package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

	private WebDriver driver;

	@FindBy(xpath = "//input[@type = 'email']")
	private WebElement usernameField;

	@FindBy(xpath = "//input[@type = 'password']")
	private WebElement passwordField;

	@FindBy(xpath = "//button[@type = 'submit']")
	private WebElement loginButton;

	public LoginPage(WebDriver driver) {
		this.driver = driver;

		PageFactory.initElements(driver, this);
	}

	@Step("Login successfully")
	public OverviewPage login(String username, String password) {
		usernameField.sendKeys(username);
		passwordField.sendKeys(password);

		loginButton.click();

		return new OverviewPage(driver);
	}
}