package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class FavoritesPage extends MediaContentPage {

    private WebDriver driver;

    @FindBy(xpath = "//h5[text()='Favorites']")
    private WebElement favoritesHeader;

    @FindBy(css = "[data-testid='FavoriteIcon']")
    private List<WebElement> favoriteIcon;

    @FindBy(css = "[data-testid='CancelIcon']")
    private WebElement removeFromFavorites;

    @FindBy(xpath = "//button[normalize-space()='Remove from Favorites']")
    private WebElement removeFromFavoritesConfirmation;


    public FavoritesPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Get favorites header")
    public String getFavoritesHeader() {
        waitForElementToBeVisible(favoritesHeader);

        return favoritesHeader.getText();
    }

    @Step("Verify all media items have a favorite icon")
    public boolean allMediaHasFavoriteIcon() {
        if (favoriteIcon.isEmpty()) {
        }

        for (WebElement icon : favoriteIcon) {
            if (!icon.isDisplayed()) {
                return false;
            }
        }

        return true;
    }

    @Step("Remove media from Favorites")
    public void removeMediaFromFavorites() {
        waitForElementToBeVisible(removeFromFavorites);
        removeFromFavorites.click();
        waitForElementToBeVisible(removeFromFavoritesConfirmation);
        removeFromFavoritesConfirmation.click();
    }

    @Step("Verify no media items are favorited (via aria-hidden check)")
    public boolean noMediaHasFavoriteIcon() {
        List<WebElement> icons = favoriteIcon;

        for (WebElement icon : icons) {
            String ariaHidden = icon.getAttribute("aria-hidden");
            boolean isActuallyVisible = icon.isDisplayed() && ("false".equals(ariaHidden) || ariaHidden == null);

            if (isActuallyVisible) {
                return false;
            }
        }

        return true;
    }
}
