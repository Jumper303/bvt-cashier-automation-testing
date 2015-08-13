package com.bvt.cashier.test.acceptance.common;

import java.util.HashMap;
import java.util.Map;

public class DtoHelper {

	public static Map<String, String> constructPaymentDto(String cardNumber, String nameOnCard, String expiryYear,
			String expiryMonth, String csc, String amount, String useExistingCard, String authenticationStatus) {
		Map<String, String> paymentData = new HashMap<String, String>();
		paymentData.put("cardNumber", cardNumber);
		paymentData.put("nameOnCard", nameOnCard);
		paymentData.put("expiryYear", expiryYear);
		paymentData.put("expiryMonth", expiryMonth);
		paymentData.put("csc", csc);
		paymentData.put("amount", amount);
		paymentData.put("useExistingCard", useExistingCard);
		paymentData.put("authenticationStatus", authenticationStatus);

		return paymentData;
	}
}
