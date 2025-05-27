package com.photopixels.web.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class UsersPage extends NavigationPage {

    private WebDriver driver;
    private JavascriptExecutor js;

    @FindBy(css = "input[placeholder='Search Users']")
    private WebElement usersSearchBarElement;

    @FindBy(css = "tbody tr.MuiTableRow-root td:nth-child(2)")
    private List<WebElement> emailElements;

    @FindBy(css = "tbody tr.MuiTableRow-root td:nth-child(3)")
    private List<WebElement> roleElements;

   public UsersPage(WebDriver driver) {
        super(driver);
        this.driver = driver;

        PageFactory.initElements(driver, this);
    }

    @Step("Return users search bar element")
    public UsersPage searchUser(String credential) {
        waitForElementToBeVisible(usersSearchBarElement);
        usersSearchBarElement.click();
        usersSearchBarElement.sendKeys(credential);

        return this;
    }

    @Step("Get emails from search results")
    public List<String> getEmailsFromResults() {
        return emailElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Verify search results contain correct email")
    public boolean hasSearchResultEmail(String expectedEmail) {
        List<String> emails = getEmailsFromResults();
        if (emails.isEmpty()) {
            return false;
        }
        return emails.contains(expectedEmail);
    }

    @Step("Get roles from search results")
    public List<String> getRolesFromResults() {
        return roleElements.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Step("Verify search result role is expected role")
    public boolean hasSearchResultRole(String expectedRole) {
        List<String> roles = getRolesFromResults();
        if (roles.isEmpty()) {
            return false;
        }
        String actualRole = roles.get(0); // Since we expect exactly one result
        return actualRole.equalsIgnoreCase(expectedRole);
    }
}