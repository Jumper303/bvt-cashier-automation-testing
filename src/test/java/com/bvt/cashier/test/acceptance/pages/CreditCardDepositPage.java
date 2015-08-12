package com.bvt.cashier.test.acceptance.pages;

import java.util.HashMap;
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

public class CreditCardDepositPage extends PageBase {
	private static final String IFRAME_WELL_EMBED_RESPONSIVE_ITEM = "iframe.well.embed-responsive-item";

	final static Logger logger = Logger.getLogger(CreditCardDepositPage.class);

	@FindBy(id = "bcDropdownMenu")
	public WebElement bankCardDropdownMenu;

	// There is no unique id or class for this element. Don't want to rely on
	// link text
	@FindBy(xpath = "//a[contains(@onclick,'addNewBankcard')]")
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

	private WebElement getCardElementByPattern(String pattern) {
		WebElement result = null;

		for (WebElement element : bankCardDropdownMenuOptions) {
			if (element.findElement(By.xpath("span")).getText().equals(pattern)) {
				result = element;
			}
		}
		return result;
	}

	public CreditCardDepositPage(WebDriver driver) {
		super(driver);
	}

	public String getValidationError() {
		switchToIframe(IFRAME_WELL_EMBED_RESPONSIVE_ITEM);
		waitForAjaxCallToComplete();
		//new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//small[@class='help-block' and @data-bv-result='INVALID']")));
		return helpBlockLabel.getText();
	}

	public void navigateToPage(String siteUrl) {
		super.navigateToPage(siteUrl + "/cashier/CreditDebitCardBVT/type/deposit");
		waitForPageLoad();
	}

	public String getTransactionResult() {
		return messageHeaderLabel.getText();
	}

	public void fillCardNumber(String cardNumber) {
		switchToIframe(IFRAME_WELL_EMBED_RESPONSIVE_ITEM);
		waitForElementToBeClickable(bankCardDropdownMenu).click();
		waitForElementToBeClickable(addNewCardLink).click();
		fillCardNumberField(cardNumber);
	}

	public void populatePageWithData(Map<String, String> paymentData) throws NumberFormatException, Exception {
		switchToIframe(IFRAME_WELL_EMBED_RESPONSIVE_ITEM);
		waitForElementToBeClickable(bankCardDropdownMenu).click();

		if (Boolean.parseBoolean(paymentData.get("useExistingCard"))) {
			waitForElementToBeClickable(getCardElementByPattern(paymentData.get("cardNumber"))).click();
		} else {
			waitForElementToBeClickable(addNewCardLink).click();
			fillCardNumberField(paymentData.get("cardNumber"));
			fillStandardField(nameOnCardField, paymentData.get("nameOnCard"));
			new DatePicker(driver).setDate(paymentData.get("expiryYear"), paymentData.get("expiryMonth"));
		}
		fillStandardField(cscField, paymentData.get("csc"));
		fillStandardField(amountField, paymentData.get("amount"));
	}

	public void waitFortransationToBeCompleted() {
		new WebDriverWait(driver, 30)
				.until(ExpectedConditions.textToBePresentInElement(messageHeaderLabel, "Successful Transaction"));
	}

	public void submit() {
		waitForElementToBeClickable(submitButton).click();
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

	public Map<String, String> constructPaymentDto(String cardNumber, String nameOnCard, String expiryYear,
			String expiryMonth, String csc, String amount, String useExistingCard, String authenticationStatus) {
		Map<String, String> paymentData = new HashMap<String, String>();
		paymentData.put("cardNumber", cardNumber);
		paymentData.put("nameOnCard", nameOnCard);
		paymentData.put("expiryYear", expiryYear);
		paymentData.put("expiryMonth", expiryMonth);
		paymentData.put("csc", csc);
		paymentData.put("amount", amount);
		paymentData.put("useExistingCard", useExistingCard);
		paymentData.put("authenticationStatus", authenticationStatus);

		return paymentData;
	}

}
