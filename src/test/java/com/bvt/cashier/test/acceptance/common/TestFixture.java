package com.bvt.cashier.test.acceptance.common;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base class for specific tests.
 * Defines before and after hook.
 */
public abstract class TestFixture {
	private final static String SCREENSHOT_DIRECTORY = "target/surefire-reports/";
	private final static String SCREENSHOT_FILE_PREFIX = "_failed.jpg";
	final static Logger logger = Logger.getLogger(TestFixture.class);
	protected WebDriver driver = null;
	protected String siteUrl;

	/**
	 * The specific webdriver is instantiated according the given java parameter -Dbrowser
	 * @param chromeDriverLocation
	 * @param ieDriverLocation
	 * @param browser
	 * @param siteUrl
	 * @param seleniumGridHubUrl
	 */
	@Parameters({ "chromeDriverLocation", "ieDriverLocation", "browser", "siteUrl", "seleniumGridHubUrl" })
	@BeforeClass
	public void setUp(String chromeDriverLocation, String ieDriverLocation, String browser, String siteUrl,
			@Optional String seleniumGridHubUrl) {
		this.siteUrl = siteUrl;
		DesiredCapabilities capabilities = null;
		if (!seleniumGridHubUrl.equals("")) {
			// via Selenium Grid
			if (browser.equalsIgnoreCase("firefox")) {
				capabilities = DesiredCapabilities.firefox();
			} else if (browser.equalsIgnoreCase("internet explorer")) {
				capabilities = DesiredCapabilities.internetExplorer();
				capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);
				if (ieDriverLocation != null && !ieDriverLocation.isEmpty()) {
					System.setProperty("webdriver.ie.driver", ieDriverLocation);
				}
			} else if (browser.equalsIgnoreCase("chrome")) {
				capabilities = DesiredCapabilities.chrome();
				if (chromeDriverLocation != null && !chromeDriverLocation.isEmpty()) {
					System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
				}
			}
			
			try {
				driver = new RemoteWebDriver(new URL(seleniumGridHubUrl), capabilities);
			} catch (MalformedURLException e) {
				logger.error("MalformedURLException: "+seleniumGridHubUrl);
				e.printStackTrace();
			}
		} else {
			// on localhost
			if (browser.equalsIgnoreCase("firefox")) {
				driver = new FirefoxDriver();
			} else if (browser.equalsIgnoreCase("internet explorer")) {
				if (ieDriverLocation != null && !ieDriverLocation.isEmpty()) {
					System.setProperty("webdriver.ie.driver", ieDriverLocation);
					driver = new InternetExplorerDriver();
				}

			} else if (browser.equalsIgnoreCase("chrome")) {
				if (chromeDriverLocation != null && !chromeDriverLocation.isEmpty()) {
					System.setProperty("webdriver.chrome.driver", chromeDriverLocation);
					driver = new ChromeDriver();
				}
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
			FileUtils.copyFile(scrFile, new File(
					String.format("%1$s%2$s%3$s", SCREENSHOT_DIRECTORY, testResult.getName(), SCREENSHOT_FILE_PREFIX)));
		}
	}
}
