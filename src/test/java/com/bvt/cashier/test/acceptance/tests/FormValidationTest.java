package com.bvt.cashier.test.acceptance.tests;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.bvt.cashier.test.acceptance.common.TestFixture;
import com.bvt.cashier.test.acceptance.pages.CreditCardDepositPage;

public class FormValidationTest extends TestFixture {
	final static Logger logger = Logger.getLogger(DepositTest.class);

	@Test(enabled = true, testName = "Test:shouldReturnWarningForInvalidCardNumber")
	@Parameters({ "siteUrl" })
	public void shouldReturnWarningForInvalidCardNumber(String siteUrl) {
		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);
		creditCardDepositPage.navigateToPage(siteUrl);		
		creditCardDepositPage.fillCardNumber("1111");		
	
		String actualResult = creditCardDepositPage.getValidationError();
	
		Assert.assertEquals(actualResult, "Invalid card number. Please review the number of digits.",
				"Test should return: Invalid card number. Please review the number of digits., Actual result:"
						+ actualResult);
	}
}
