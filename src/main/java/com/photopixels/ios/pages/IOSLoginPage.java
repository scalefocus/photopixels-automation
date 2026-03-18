package com.photopixels.ios.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class IOSLoginPage {

    private final AppiumDriver driver;
    private final WebDriverWait wait;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@value='Server Address']")
    private WebElement serverAddressInput;

    @iOSXCUITFindBy(accessibility = "Connect")
    private WebElement connectToServerButton;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeTextField[@value='Username']")
    private WebElement usernameInput;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeSecureTextField[@value='Password']")
    private WebElement passwordInput;

    @iOSXCUITFindBy(xpath = "//XCUIElementTypeButton[@name='Login']")
    private WebElement loginButton;

    public IOSLoginPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public void connectToServer(String serverAddress) {
        wait.until(ExpectedConditions.visibilityOf(serverAddressInput));
        serverAddressInput.sendKeys(serverAddress);
        connectToServerButton.click();
    }

    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOf(usernameInput));
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
    }
}