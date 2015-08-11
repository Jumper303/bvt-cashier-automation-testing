package com.bvt.cashier.test.acceptance.pages;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreditCardDepositPage extends PageBase {
	@FindBy(id = "bcDropdownMenu")
	public WebElement bankCardDropdownMenu;

	@FindBy(xpath = "//*/a[contains(text(),'Add new')]")
	public WebElement addNewCardLink;

	@FindBy(xpath = "//input[contains(@class,'form-control newcc')]")
	public WebElement cardNumberField;

	@FindBy(id = "nameOnTheCard")
	public WebElement nameOnCardField;

	@FindBy(css = ".input-group-addon.btn")
	public WebElement datePickerButton;

	@FindBy(xpath = "//*/span[@class='year'][1]")
	public WebElement nextAvailableYearButton;

	@FindBy(xpath = "//*/span[@class='month'][1]")
	public WebElement firstAvailableMonthButton;

	@FindBy(id = "csc")
	public WebElement cscField;

	@FindBy(xpath = "//*/input[@id='amount']")
	public WebElement amountField;

	@FindBy(id = "submitBtn")
	public WebElement submitButton;

	@FindBy(xpath = "//*/div[@class='msg-header success-header']")
	public WebElement messageHeaderLabel;

	public WebElement waitForElementToBeClickable(WebElement element) {
		new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}

	public WebElement waitForTextToBePresentInElement(WebElement element, String text) {
		new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElement(element, text));
		return element;
	}

	public CreditCardDepositPage(WebDriver driver) {
		super(driver);
	}

	public void navigateToPage(String siteUrl) {
		super.navigateToPage(siteUrl+"/cashier/CreditDebitCardBVT/type/deposit");
		waitForPageLoad();
	}

	public void switchToIframe() {
		driver.switchTo().defaultContent();
		driver.switchTo().frame(driver.findElement(By.cssSelector("iframe.well.embed-responsive-item")));
		waitForElementToBeClickable(submitButton);
	}

	public void populatePageWithData(Map<String, String> paymentData) {
		switchToIframe();
		waitForElementToBeClickable(bankCardDropdownMenu).click();
		waitForElementToBeClickable(addNewCardLink).click();
		waitForElementToBeClickable(cardNumberField).clear();

		for (char digit : (paymentData.get("cardNumber")).toCharArray()) {
			waitForElementToBeClickable(cardNumberField).sendKeys("" + digit);
		}

		waitForElementToBeClickable(cardNumberField).sendKeys(Keys.TAB);
		waitForElementToBeClickable(nameOnCardField).clear();
		waitForElementToBeClickable(nameOnCardField).sendKeys(paymentData.get("nameOnCard"));
		waitForElementToBeClickable(nameOnCardField).sendKeys(Keys.TAB);
		waitForElementToBeClickable(datePickerButton).click();
		waitForElementToBeClickable(nextAvailableYearButton).click();
		waitForElementToBeClickable(firstAvailableMonthButton).click();
		waitForElementToBeClickable(cscField).clear();
		waitForElementToBeClickable(cscField).sendKeys(paymentData.get("csc"));
		waitForElementToBeClickable(cscField).sendKeys(Keys.TAB);
		waitForElementToBeClickable(amountField).clear();
		waitForElementToBeClickable(amountField).sendKeys(paymentData.get("amount"));
	}

	public void submit(String expectedTransactionResult) {
		waitForElementToBeClickable(submitButton).click();
		waitForTextToBePresentInElement(messageHeaderLabel, expectedTransactionResult);
	}

}
