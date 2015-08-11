package com.bvt.cashier.test.acceptance.deposit;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.bvt.cashier.test.acceptance.common.TestFixture;
import com.bvt.cashier.test.acceptance.pages.CreditCardDepositPage;

public class DepositTest extends TestFixture {
	final static Logger logger = Logger.getLogger(DepositTest.class);

	@Test(enabled = true, testName = "Test:shouldReturnSuccessfulTransactionOnDepositWithNon3DSPayment")
	@Parameters({ "siteUrl" })
	public void shouldReturnSuccessfulTransactionOnDepositWithNon3DSPayment(String siteUrl) {
		Map<String, String> paymentData = new HashMap<String, String>() {
			{
				put("cardNumber", " 4111111111111111");
				put("nameOnCard", "Peter Dobrosi");
				put("expiryYear", "2016");
				put("expiryMonth", "01");
				put("csc", "123");
				put("amount", "1");
			}
		};

		if (logger.isDebugEnabled()) {
			logger.info(
					"Starting test case: shouldReturnSuccessfulTransactionOnDepositWithNon3DSPayment with input data: "
							+ paymentData.toString());
		}

		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);

		creditCardDepositPage.navigateToPage(siteUrl);
		creditCardDepositPage.populatePageWithData(paymentData);
		creditCardDepositPage.submit("Successful Transaction");
		Assert.assertEquals(creditCardDepositPage.messageHeaderLabel.getText(), "Successful Transaction");
	}
}
