package com.photopixels.helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

public class IOSSwippingHelper {

    private final AppiumDriver driver;

    public IOSSwippingHelper (AppiumDriver driver){
        this.driver = driver;
    }
    public void swipeLeft() {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.8);
        int endX   = (int) (size.width * 0.2);
        int y      = (int) (size.height * 0.5);

        performSwipe(startX, y, endX, y);
    }

    public void swipeRight() {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.2);
        int endX   = (int) (size.width * 0.8);
        int y      = (int) (size.height * 0.5);

        performSwipe(startX, y, endX, y);
    }

    private void performSwipe(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger1");

        Sequence swipe = new Sequence(finger, 0)
                .addAction(finger.createPointerMove(Duration.ZERO,
                        PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(100),
                        PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerMove(Duration.ofMillis(300),
                        PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(List.of(swipe));
    }
}
