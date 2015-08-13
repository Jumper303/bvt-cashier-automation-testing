package com.bvt.cashier.test.acceptance.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public abstract class TestFixture {
	protected WebDriver driver = null;
	protected String siteUrl;

	@Parameters({ "chromeDriverLocation","ieDriverLocation", "browser", "siteUrl" })
	@BeforeClass
	public void setUp(String chromeDriverLocation, String ieDriverLocation, String browser, String siteUrl) {
		this.siteUrl = siteUrl;
		if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("internet explorer")) {
			if (ieDriverLocation != null && !ieDriverLocation.isEmpty()) {
				System.setProperty("webdriver.ie.driver", ieDriverLocation);
				driver = new InternetExplorerDriver();			}
						
		} else if (browser.equalsIgnoreCase("chrome")) {
			if (chromeDriverLocation != null && !chromeDriverLocation.isEmpty()) {
				System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
				driver = new ChromeDriver();
			}
		}
	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

	@AfterMethod
	public void takeScreenShotOnFailure(ITestResult testResult) throws IOException {
		if (testResult.getStatus() == ITestResult.FAILURE) {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("target/surefire-reports/"+testResult.getName()+"_failed.jpg"));
			
		}
	}
	
	

}
