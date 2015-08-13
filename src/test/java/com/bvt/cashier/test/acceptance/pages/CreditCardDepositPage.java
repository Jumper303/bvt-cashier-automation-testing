package com.bvt.cashier.test.acceptance.pages;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.bvt.cashier.test.acceptance.common.DatePicker;

/**
 * Page Object to provide interaction with the CreditCardDeposit Page
 *
 */
public class CreditCardDepositPage extends PageBase {
	private final static  String IFRAME_WELL_EMBED_RESPONSIVE_ITEM = "iframe.well.embed-responsive-item";
	private final static Logger logger = Logger.getLogger(CreditCardDepositPage.class);
	private final static String PAGE_URL = "/cashier/CreditDebitCardBVT/type/deposit";

	@FindBy(id = "bcDropdownMenu")
	public WebElement bankCardDropdownMenu;

	@FindBy(partialLinkText = "Add new")
	private WebElement addNewCardLink;

	@FindBy(xpath = "//input[contains(@class,'form-control newcc')]")
	private WebElement cardNumberField;

	@FindBy(id = "nameOnTheCard")
	private WebElement nameOnCardField;

	@FindBy(id = "csc")
	private WebElement cscField;

	@FindBy(xpath = "//*/input[@id='amount']")
	private WebElement amountField;

	@FindBy(id = "submitBtn")
	private WebElement submitButton;

	@FindBy(css = "div.msg-header")
	private WebElement messageHeaderLabel;

	@FindBy(xpath = "//small[@class='help-block' and @data-bv-result='INVALID']")
	private WebElement helpBlockLabel;

	@FindBy(xpath = "//*[@id='bcDropdownMenu']")
	private List<WebElement> bankCardDropdownMenuOptions;

	public CreditCardDepositPage(WebDriver driver) {
		super(driver);
	}

	public String getValidationError() {
		switchToIframe(IFRAME_WELL_EMBED_RESPONSIVE_ITEM);
		waitForAjaxCallToComplete();
		return helpBlockLabel.getText();
	}

	//site base URL is separated to enable switching between environments
	public void navigateToPage(String siteUrl) {
		super.navigateToPage(siteUrl + PAGE_URL);
		waitForPageLoad();
	}

	public String getTransactionResult() {
		return messageHeaderLabel.getText();
	}

	public void fillCardNumber(String cardNumber) {
		switchToIframe(IFRAME_WELL_EMBED_RESPONSIVE_ITEM);
		waitForElementToBeClickable(bankCardDropdownMenu).click();
		javascriptClick(addNewCardLink);
		fillCardNumberField(cardNumber);
	}

	public void populatePageWithData(Map<String, String> paymentData) throws NumberFormatException, Exception {
		switchToIframe(IFRAME_WELL_EMBED_RESPONSIVE_ITEM);
		waitForElementToBeClickable(bankCardDropdownMenu).click();

		if (Boolean.parseBoolean(paymentData.get("useExistingCard"))) {
			waitForElementToBeClickable(getCardElementByPattern(paymentData.get("cardNumber"))).click();
		} else {
			javascriptClick(addNewCardLink);
			fillCardNumberField(paymentData.get("cardNumber"));
			fillStandardField(nameOnCardField, paymentData.get("nameOnCard"));
			new DatePicker(driver).setDate(paymentData.get("expiryYear"), paymentData.get("expiryMonth"));
		}
		fillStandardField(cscField, paymentData.get("csc"));
		fillStandardField(amountField, paymentData.get("amount"));
	}

	public void waitForTransationToBeCompleted() {
		new WebDriverWait(driver, 30)
				.until(ExpectedConditions.textToBePresentInElement(messageHeaderLabel, "Successful Transaction"));
	}

	public void submit() {
		javascriptClick(submitButton);
	}

	public void invalidateNumberField() {
		waitForElementToBeClickable(cardNumberField).sendKeys(Keys.TAB);
	}

	private void fillCardNumberField(String cardNumber) {
		waitForElementToBeClickable(cardNumberField).clear();

		for (char digit : (cardNumber).toCharArray()) {
			waitForElementToBeClickable(cardNumberField).sendKeys("" + digit);
		}

		waitForElementToBeClickable(cardNumberField).sendKeys(Keys.TAB);
	}

	private void fillStandardField(WebElement field, String value) {
		waitForElementToBeClickable(field).clear();
		waitForElementToBeClickable(field).sendKeys(value);
		waitForElementToBeClickable(field).sendKeys(Keys.TAB);
	}

	public void performDeposit(String siteUrl, Map<String, String> paymentData) {
		navigateToPage(siteUrl);
		try {
			populatePageWithData(paymentData);
		} catch (Exception e) {
			logger.error("Exception ocurred in test during page popluation with data:" + paymentData.toString(), e);
			Assert.fail("Exception ocurred in test during page popluation with data:" + paymentData.toString(), e);
		}
		submit();
	}

	private WebElement getCardElementByPattern(String pattern) {
		WebElement result = null;

		for (WebElement element : bankCardDropdownMenuOptions) {
			if (element.findElement(By.xpath("span")).getText().equals(pattern)) {
				result = element;
			}
		}
		return result;
	}
}
