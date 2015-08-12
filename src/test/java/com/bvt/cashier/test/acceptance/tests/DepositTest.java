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

	/**
	 * Test data driven from testng.xml	
	 */
	@Test(enabled = true, testName = "Test:shouldReturnSuccessfulTransactionOnDepositWithNon3DSPayment")
	@Parameters({ "siteUrl", "cardNumber", "nameOnCard", "expiryYear", "expiryMonth", "csc", "amount",
			"useExistingCard", "authenticationStatus" })
	public void shouldReturnSuccessfulTransactionOnDepositWithNon3DSPayment(String siteUrl, String cardNumber,
			String nameOnCard, String expiryYear, String expiryMonth, String csc, String amount, String useExistingCard,
			String authenticationStatus) {
		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);
		Map<String, String> paymentDataDto = creditCardDepositPage.constructPaymentDto(cardNumber, nameOnCard,
				expiryYear, expiryMonth, csc, amount, useExistingCard, authenticationStatus);
		creditCardDepositPage.performDeposit(siteUrl, paymentDataDto);
		creditCardDepositPage.waitFortransationToBeCompleted();

		String actualResult = creditCardDepositPage.getTransactionResult();
		Assert.assertEquals(actualResult, "Successful Transaction",
				"Test should return: Successful Transaction, Actual result:" + actualResult);
	}

	@Test(enabled = true, testName = "Test:shouldReturnSuccessfulTransactionOnDepositWith3DSPayment")
	@Parameters({ "siteUrl", "cardNumber", "nameOnCard", "expiryYear", "expiryMonth", "csc", "amount",
			"useExistingCard", "authenticationStatus" })
	public void shouldReturnSuccessfulTransactionOnDepositWith3DSPayment(String siteUrl, String cardNumber,
			String nameOnCard, String expiryYear, String expiryMonth, String csc, String amount, String useExistingCard,
			String authenticationStatus) {
		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);
		Map<String, String> paymentDataDto = creditCardDepositPage.constructPaymentDto(cardNumber, nameOnCard,
				expiryYear, expiryMonth, csc, amount, useExistingCard, authenticationStatus);
		creditCardDepositPage.performDeposit(siteUrl, paymentDataDto);

		SecureAuthenticationPage secureAuthenticationPage = new SecureAuthenticationPage(driver);
		secureAuthenticationPage.authenticateWith(authenticationStatus);

		String actualResult = creditCardDepositPage.getTransactionResult();
		Assert.assertEquals(actualResult, "Successful Transaction",
				"Test should return: Successful Transaction, Actual result:" + actualResult);
	}

	@Test(enabled = true, testName = "Test:shouldReturnFailedTransactionOnDepositWith3DSPayment")
	@Parameters({ "siteUrl", "cardNumber", "nameOnCard", "expiryYear", "expiryMonth", "csc", "amount",
			"useExistingCard", "authenticationStatus" })
	public void shouldReturnFailedTransactionOnDepositWith3DSPayment(String siteUrl, String cardNumber,
			String nameOnCard, String expiryYear, String expiryMonth, String csc, String amount, String useExistingCard,
			String authenticationStatus) {

		CreditCardDepositPage creditCardDepositPage = new CreditCardDepositPage(driver);
		Map<String, String> paymentDataDto = creditCardDepositPage.constructPaymentDto(cardNumber, nameOnCard,
				expiryYear, expiryMonth, csc, amount, useExistingCard, authenticationStatus);
		creditCardDepositPage.performDeposit(siteUrl, paymentDataDto);
		SecureAuthenticationPage secureAuthenticationPage = new SecureAuthenticationPage(driver);
		secureAuthenticationPage.authenticateWith(authenticationStatus);

		String actualResult = creditCardDepositPage.getTransactionResult();
		Assert.assertEquals(actualResult, "Unsuccessful Transaction",
				"Test should return: Unsuccessful Transaction, Actual result:" + actualResult);
	}
}
