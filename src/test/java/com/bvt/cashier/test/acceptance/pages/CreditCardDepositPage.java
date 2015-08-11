package com.bvt.cashier.test.acceptance.pages;

import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreditCardDepositPage extends PageBase {
	
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

	@FindBy(css = ".input-group-addon.btn")
	private WebElement datePickerButton;

	@FindBy(xpath = "//*/span[@class='year'][1]")
	private WebElement nextAvailableYearButton;

	@FindBy(xpath = "//*/span[@class='month'][1]")
	private WebElement firstAvailableMonthButton;

	@FindBy(id = "csc")
	private WebElement cscField;

	@FindBy(xpath = "//*/input[@id='amount']")
	private WebElement amountField;

	@FindBy(id = "submitBtn")
	private WebElement submitButton;
	
	@FindBy(xpath = "//*/div[@class='msg-header success-header']")
	private WebElement messageHeaderLabel;

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

	public void navigateToPage(String siteUrl) {
		super.navigateToPage(siteUrl + "/cashier/CreditDebitCardBVT/type/deposit");
		waitForPageLoad();
	}

	public String getTransactionResult() {
		return messageHeaderLabel.getText();
	}

	public void populatePageWithData(Map<String, String> paymentData) {
		switchToIframe("iframe.well.embed-responsive-item");
		waitForElementToBeClickable(bankCardDropdownMenu).click();

		if (Boolean.parseBoolean(paymentData.get("useExistingCard"))) {
			waitForElementToBeClickable(getCardElementByPattern(paymentData.get("cardNumber"))).click();
		} else {
			waitForElementToBeClickable(addNewCardLink).click();
			waitForElementToBeClickable(cardNumberField).clear();

			for (char digit : (paymentData.get("cardNumber")).toCharArray()) {
				waitForElementToBeClickable(cardNumberField).sendKeys("" + digit);
			}

			waitForElementToBeClickable(cardNumberField).sendKeys(Keys.TAB);
			waitForElementToBeClickable(nameOnCardField).clear();
			waitForElementToBeClickable(nameOnCardField).sendKeys(paymentData.get("nameOnCard"));
			waitForElementToBeClickable(nameOnCardField).sendKeys(Keys.TAB);

			// DatePicker datePicker = new DatePicker(driver);
			// datePicker.setDate(Integer.parseInt(paymentData.get("expiryYear")),
			// Integer.parseInt(paymentData.get("expiryMonth")));

			waitForElementToBeClickable(datePickerButton).click();
			waitForElementToBeClickable(nextAvailableYearButton).click();
			waitForElementToBeClickable(firstAvailableMonthButton).click();
		}

		waitForElementToBeClickable(cscField).clear();
		waitForElementToBeClickable(cscField).sendKeys(paymentData.get("csc"));
		waitForElementToBeClickable(cscField).sendKeys(Keys.TAB);
		waitForElementToBeClickable(amountField).clear();
		waitForElementToBeClickable(amountField).sendKeys(paymentData.get("amount"));
	}

	
	
	public void waitFortransationToBeCompleted() {
		new WebDriverWait(driver, 30)
				.until(ExpectedConditions.textToBePresentInElement(messageHeaderLabel, "Successful Transaction"));
	}

	public void submit() {
		waitForElementToBeClickable(submitButton).click();
	}

}
