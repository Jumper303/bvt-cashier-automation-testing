package com.bvt.cashier.test.acceptance.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DatePicker {
	WebDriver driver = null;
	@FindBy(css = ".input-group-addon.btn")
	public WebElement datePickerButton;

	@FindBy(xpath = "//*/span[@class='year'][1]")
	public WebElement nextAvailableYearButton;

	@FindBy(xpath = "//*/span[@class='month'][1]")
	public WebElement firstAvailableMonthButton;

	public DatePicker(WebDriver driver) {
		PageFactory.initElements(driver, this);
		this.driver = driver;
		datePickerButton.click();
	}

	public void setYear(int year) {

	}

	public void setMonth(int month) {

	}
	
	public void setDate(int year, int month)
	{
		setYear(year);
		setMonth(month);
	}
}
