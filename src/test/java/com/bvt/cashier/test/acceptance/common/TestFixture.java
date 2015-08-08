package com.bvt.cashier.test.acceptance.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

public abstract class TestFixture {
	protected WebDriver driver = null;
	private DesiredCapabilities capabilities = null;

	@Parameters({ "hubUrl", "driverLocation", "browser", "os", "version" })
	@BeforeClass
	public void setUp(String hubUrl, String driverLocation, String browser, String os, String version) {
		Platform platform = null;

		if (browser.equalsIgnoreCase("firefox")) {
			capabilities = DesiredCapabilities.firefox();
		} else if (browser.equalsIgnoreCase("internet explorer")) {
			capabilities = DesiredCapabilities.internetExplorer();
			if (driverLocation != null && !driverLocation.isEmpty()) {
				System.setProperty("webdriver.ie.driver", driverLocation);
			}
		} else if (browser.equalsIgnoreCase("chrome")) {
			capabilities = DesiredCapabilities.chrome();
			if (driverLocation != null && !driverLocation.isEmpty()) {
				System.setProperty("webdriver.chrome.driver", driverLocation);
			}
		}

		if (version != null && !version.isEmpty()) {
			capabilities.setVersion(version);
		}

		if (os.equalsIgnoreCase("Windows7")) {
			platform = Platform.WINDOWS;
		} else if (os.equalsIgnoreCase("Vista")) {
			platform = Platform.VISTA;
		} else if (os.equalsIgnoreCase("Linux")) {
			platform = Platform.LINUX;
		}

		if (platform != null) {
			capabilities.setPlatform(platform);
		}

		try {
			driver = new RemoteWebDriver(new URL(hubUrl), capabilities);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@AfterClass
	public void tearDown() {
		driver.quit();
	}

}
