package com.bvt.cashier.test.acceptance.tests;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.bvt.cashier.test.acceptance.common.TestFixture;
import com.bvt.cashier.test.acceptance.pages.CreditCardDepositPage;

public class FormValidationTest extends TestFixture {

	@Test(enabled = true, testName = "Test:shouldReturnWarningForInvalidCardNumber")
	@Parameters({ "siteUrl", "carNumber" })
	public void shouldReturnWarningForInvalidCardNumber(String siteUrl, String cardNumber) {
		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);
		creditCardDepositPage.navigateToPage(siteUrl);
		creditCardDepositPage.fillCardNumber(cardNumber);
		

		String actualResult = creditCardDepositPage.getValidationError();

		Assert.assertEquals(actualResult, "Invalid card number. Please review the number of digits.",
				"Test should return: Invalid card number. Please review the number of digits., Actual result:"
						+ actualResult);
	}
}
