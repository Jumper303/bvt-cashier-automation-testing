package com.bvt.cashier.test.acceptance.common;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.bvt.cashier.test.acceptance.pages.PageBase;

public class DatePicker extends PageBase {
	@FindBy(css = ".input-group-addon.btn")
	public WebElement datePickerButton;

	@FindBy(xpath = "//*/div[@class='datepicker-years']/table/thead/tr/th[@class='next']")
	public WebElement nextCalendarPage;
	
	public DatePicker(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);		
	}

	public void setYear(int year) {
		switchToIframe("iframe.well.embed-responsive-item");
		datePickerButton.click();
		List<WebElement> yearElements = driver.findElements(By.xpath("//span[@class='year' or @class='year active']"));

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

		WebElement yearElement = driver.findElement(
				By.xpath("//span[(@class='year' or @class='year active') and contains(text(),'" + year + "')]"));
		waitForElementToBeClickable(yearElement).click();
	}

	public void setMonth(int month) {
		String monthId = (month < 10) ? "0" + month : "" + month;
		WebElement monthElement = driver
				.findElement(By.xpath("//span[@class='month' and contains(text(),'" + monthId + "')]"));
		waitForElementToBeClickable(monthElement).click();
	}

	public void setDate(String year, String month) throws Exception {
		if (new Date().after(new Date(Integer.parseInt(year), Integer.parseInt(month), 1))) {
			throw new Exception("The current date is beyond the expiration date!");
		}

		setYear(Integer.parseInt(year));
		setMonth(Integer.parseInt(month));
	}
}
