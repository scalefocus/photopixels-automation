package com.photopixels.mobile.pages;

import com.photopixels.helpers.WaitOperationHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import static java.time.Duration.ofSeconds;

public class ErrorPopUpPage extends WaitOperationHelper {

    private AppiumDriver driver;

    @AndroidFindBy(xpath = "//android.widget.TextView[@index='0']")
    private WebElement errorTitle;

    @AndroidFindBy(xpath = "//android.widget.TextView[@index='1']")
    private WebElement errorText;

    @AndroidFindBy(xpath = "//android.widget.Button")
    private WebElement confirmButton;

    public ErrorPopUpPage(AppiumDriver driver) {
        super(driver);
        this.driver = driver;

        // This time out is set because test can be run on slow Android SDK emulator
        PageFactory.initElements(new AppiumFieldDecorator(driver, ofSeconds(5)), this);
    }

    @Step("Get error title")
    public String getErrorTitle() {
        // Wait is needed so the elements are loaded properly on emulator
        waitMs();

        return errorTitle.getText();
    }

    @Step("Get error text")
    public String getErrorText() {
        return errorText.getText();
    }

}
