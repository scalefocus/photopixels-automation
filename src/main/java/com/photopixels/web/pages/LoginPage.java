package com.photopixels.web.pages;

import com.photopixels.helpers.WaitOperationHelper;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends WaitOperationHelper {

	private WebDriver driver;

	@FindBy(xpath = "//input[@type = 'email']")
	private WebElement usernameField;

	@FindBy(xpath = "//input[@type = 'password']")
	private WebElement passwordField;

	@FindBy(xpath = "//button[@type = 'submit']")
	private WebElement loginButton;

	@FindBy(xpath = "//div[@role='status']")
	private WebElement errorMessage;

	@FindBy(xpath = "//a[@href='/register']//p[contains(@class, 'css-1fsf1k1')]")
	private WebElement signUpLink;

	public LoginPage(WebDriver driver) {
		super(driver);
		this.driver = driver;

		PageFactory.initElements(driver, this);
	}

	@Step("Login successfully")
	public OverviewPage login(String username, String password) {
		fillCredentials(username, password);

		loginButton.click();

		return new OverviewPage(driver);
	}

	@Step("Fill credentials")
	public void fillCredentials(String username, String password) {

		if (username != null) {
			usernameField.sendKeys(username);
		}

		if (password != null) {
			passwordField.sendKeys(password);
		}

	}

	@Step("Get error message")
	public String getErrorMessage() {
		waitForElementToBeVisible(errorMessage);

		return errorMessage.getText();
	}

	@Step("Check if login button is enabled")
	public boolean isLoginButtonEnabled() {
		return loginButton.isEnabled();
	}

	@Step("Open Sign up page")
	public SignUpUserPage openSignUpUserPage() {
		waitForElementToBeVisible(signUpLink);

		signUpLink.click();

		return new SignUpUserPage(driver);
	}
}