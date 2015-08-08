package com.bvt.cashier.test.acceptance.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public abstract class TestFixture {
	protected WebDriver driver = null;
	
	@Parameters({ "driverLocation", "browser" })
	@BeforeClass
	public void setUp(String driverLocation, String browser) {
		if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("internet explorer")) {
			driver = new InternetExplorerDriver();
			if (driverLocation != null && !driverLocation.isEmpty()) {
				System.setProperty("webdriver.ie.driver", driverLocation);
			}
		} else if (browser.equalsIgnoreCase("chrome")) {			
			if (driverLocation != null && !driverLocation.isEmpty()) {
				System.setProperty("webdriver.chrome.driver", driverLocation);
				driver = new ChromeDriver();
			}
		}
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

}
