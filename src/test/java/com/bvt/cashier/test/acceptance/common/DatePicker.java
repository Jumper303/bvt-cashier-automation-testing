package com.bvt.cashier.test.acceptance.common;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.bvt.cashier.test.acceptance.pages.PageBase;

public class DatePicker extends PageBase {
	private static final String IFRAME_WELL_EMBED_RESPONSIVE_ITEM = "iframe.well.embed-responsive-item";
	private static final String ACTIVE_YEAR_ELEMENT_LIST_LOCATOR = "//span[@class='year' or @class='year active']";
	private static final String DYNAMIC_YEAR_ELEMENT_LOCATOR = "//span[(@class='year' or @class='year active') and contains(text(),'";
	private static final String DYNAMIC_MONTH_ELEMENT_LOCATOR = "//span[@class='month' and contains(text(),'";

	@FindBy(css = ".input-group-addon.btn")
	public WebElement datePickerButton;

	@FindBy(xpath = "//*/div[@class='datepicker-years']/table/thead/tr/th[@class='next']")
	public WebElement nextCalendarPage;

	@FindBy(xpath = "//*/span[@class='year'][1]")
	public WebElement nextAvailableYearButton;

	@FindBy(xpath = "//*/span[@class='month'][1]")
	public WebElement firstAvailableMonthButton;

	public DatePicker(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	public void setYear(int year) {
		switchToIframe(IFRAME_WELL_EMBED_RESPONSIVE_ITEM);
		datePickerButton.click();
		if (driver instanceof FirefoxDriver) {
			List<WebElement> yearElements = driver.findElements(By.xpath(ACTIVE_YEAR_ELEMENT_LIST_LOCATOR));

			boolean found = false;
			for (WebElement element : yearElements) {
				if (element.getText().equals("" + year)) {
					found = true;
					break;
				}
			}

			if (!found) {
				nextCalendarPage.click();
				waitForAjaxCallToComplete();
			}
			try {
				WebElement yearElement = driver.findElement(By.xpath(DYNAMIC_YEAR_ELEMENT_LOCATOR + year + "')]"));
				waitForElementToBeClickable(yearElement).click();
			} catch (Exception e) {
				//Fallback to select next available year if the specific selection would fail
				waitForElementToBeClickable(nextAvailableYearButton).click();
			}
			
		} else {
			//Fallback to select next available year if the specific selection would fail
			waitForElementToBeClickable(nextAvailableYearButton).click();
		}
	}

	public void setMonth(int month) {
		if (driver instanceof FirefoxDriver) {
			String monthId = (month < 10) ? "0" + month : "" + month;

			WebElement monthElement = driver.findElement(By.xpath(DYNAMIC_MONTH_ELEMENT_LOCATOR + monthId + "')]"));
			waitForElementToBeClickable(monthElement).click();
		} else {
			//Fallback to select January if the specific selection would fail
			waitForElementToBeClickable(firstAvailableMonthButton).click();
		}

	}

	public void setDate(String year, String month) throws Exception {
		if (new Date().after(new Date(Integer.parseInt(year), Integer.parseInt(month), 1))) {
			throw new Exception("The current date is beyond the expiration date!");
		}

		setYear(Integer.parseInt(year));
		setMonth(Integer.parseInt(month));
	}
}
