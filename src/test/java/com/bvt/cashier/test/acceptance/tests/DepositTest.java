package com.bvt.cashier.test.acceptance.tests;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.bvt.cashier.test.acceptance.common.TestFixture;
import com.bvt.cashier.test.acceptance.pages.CreditCardDepositPage;
import com.bvt.cashier.test.acceptance.pages.SecureAuthenticationPage;

public class DepositTest extends TestFixture {
	final static Logger logger = Logger.getLogger(DepositTest.class);

	@Test(enabled = true, testName = "Test:shouldReturnSuccessfulTransactionOnDepositWithNon3DSPayment")
	@Parameters({ "siteUrl" })
	public void shouldReturnSuccessfulTransactionOnDepositWithNon3DSPayment(String siteUrl) {
		Map<String, String> paymentData = new HashMap<String, String>() {
			{
				put("cardNumber", "4111111111111111");
				put("nameOnCard", "Peter Dobrosi");
				put("expiryYear", "2021");
				put("expiryMonth", "01");
				put("csc", "123");
				put("amount", "1");
				put("useExistingCard", "false");
			}
		};
	
		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);
		creditCardDepositPage.performDeposit(siteUrl, paymentData);
		creditCardDepositPage.waitFortransationToBeCompleted();

		String actualResult = creditCardDepositPage.getTransactionResult();
		Assert.assertEquals(actualResult, "Successful Transaction",
				"Test should return: Successful Transaction, Actual result:" + actualResult);
	}

	@Test(enabled = true, testName = "Test:shouldReturnSuccessfulTransactionOnDepositWith3DSPayment")
	@Parameters({ "siteUrl" })
	public void shouldReturnSuccessfulTransactionOnDepositWith3DSPayment(String siteUrl) {

		Map<String, String> paymentData = new HashMap<String, String>() {
			{
				put("cardNumber", "xxxx-xxxx-xxxx-1111");
				put("csc", "123");
				put("amount", "7");
				put("useExistingCard", "true");
				put("authenticationStatus", "Y");
			}
		};

		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);
		creditCardDepositPage.performDeposit(siteUrl, paymentData);

		SecureAuthenticationPage secureAuthenticationPage = new SecureAuthenticationPage(driver);
		secureAuthenticationPage.authenticateWith(paymentData.get("authenticationStatus"));

		String actualResult = creditCardDepositPage.getTransactionResult();
		Assert.assertEquals(actualResult, "Successful Transaction",
				"Test should return: Successful Transaction, Actual result:" + actualResult);
	}

	@Test(enabled = true, testName = "Test:shouldReturnFailedTransactionOnDepositWith3DSPayment")
	@Parameters({ "siteUrl" })
	public void shouldReturnFailedTransactionOnDepositWith3DSPayment(String siteUrl) {

		Map<String, String> paymentData = new HashMap<String, String>() {
			{
				put("cardNumber", "5555555555554444 ");
				put("nameOnCard", "Peter Dobrosi");
				put("expiryYear", "2016");
				put("expiryMonth", "01");
				put("csc", "123");
				put("amount", "7");
				put("useExistingCard", "false");
				put("authenticationStatus", "N");
			}
		};
	
		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);
		creditCardDepositPage.performDeposit(siteUrl, paymentData);
		
		SecureAuthenticationPage secureAuthenticationPage = new SecureAuthenticationPage(driver);
		secureAuthenticationPage.authenticateWith(paymentData.get("authenticationStatus"));

		String actualResult = creditCardDepositPage.getTransactionResult();
		Assert.assertEquals(actualResult, "Unsuccessful Transaction",
				"Test should return: Unsuccessful Transaction, Actual result:" + actualResult);
	}
}
