package com.bvt.cashier.test.acceptance.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * Page Object to provide interaction with the SecureAuthentication Page
 *
 */
public class SecureAuthenticationPage extends PageBase {
	@FindBy(id = "submit")
	private WebElement completeSecureCode;

	@FindBy(id = "eci")
	private WebElement authenticationStatusDropdown;

	public SecureAuthenticationPage(WebDriver driver) {
		super(driver);
	}

	public void authenticateWith(String value) {
		switchToIframe("iframe.well.embed-responsive-item");
		new Select(waitForElementToBeClickable(authenticationStatusDropdown)).selectByValue(value);
		javascriptClick(completeSecureCode);
	}

}
