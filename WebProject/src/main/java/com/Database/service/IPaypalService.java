package com.Database.service;

import com.Config.PaypalPaymentIntent;
import com.Config.PaypalPaymentMethod;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface IPaypalService {

	Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

	Payment createPayment(Double total, String currency, PaypalPaymentMethod method, PaypalPaymentIntent intent, String description, String cancelUrl, String successUrl)
			throws PayPalRESTException;

}
