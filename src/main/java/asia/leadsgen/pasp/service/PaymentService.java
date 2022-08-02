package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.external.AnetApiConnector;
import asia.leadsgen.pasp.data.access.external.BraintreeApiConnector;
import asia.leadsgen.pasp.data.access.external.PaypalApiConnector;
import asia.leadsgen.pasp.data.access.external.PaypalProApiConnector;
import asia.leadsgen.pasp.data.access.external.StripeApiConnector;
import asia.leadsgen.pasp.data.access.repository.PaymentRepository;
import asia.leadsgen.pasp.entity.Payment;
import asia.leadsgen.pasp.error.SystemCode;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.external.BankOfUSA.BankUSAChargeRequest;
import asia.leadsgen.pasp.model.dto.external.BankOfUSA.BankUSAChargeResponse;
import asia.leadsgen.pasp.model.dto.external.braintree.BraintreeChargeResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalAccessTokenResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreatePaymentUrlRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreatePaymentUrlResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.Reason;
import asia.leadsgen.pasp.model.dto.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PaypalProCreateOrderRequest;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PaypalProCreateOrderResponse;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.Shipping;
import asia.leadsgen.pasp.model.dto.payment.PaymentRequest;
import asia.leadsgen.pasp.model.dto.payment.PaymentResponse;
import asia.leadsgen.pasp.util.AesUtil;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.AppUtil;
import asia.leadsgen.pasp.util.BankOfUSAAPiConnector;
import asia.leadsgen.pasp.util.GetterUtil;
import asia.leadsgen.pasp.util.ResourceStates;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.CustomerAddressType;
import net.authorize.api.contract.v1.CustomerDataType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.TransactionResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
@Transactional
@Log4j2
public class PaymentService {

	@Value("${aes.encryption.key}")
	private static String aesEncryptionKey;
	@Value("${global.gateway.client.account_name}")
	private static String bankOfUSAAccountName;

	@Autowired
	PaypalApiConnector paypalApiConnector;
	@Autowired
	PaypalProApiConnector paypalProApiConnector;
	@Autowired
	StripeApiConnector stripeApiConnector;
	@Autowired
	BraintreeApiConnector braintreeApiConnector;
	@Autowired
	AnetApiConnector anetApiConnector;
	@Autowired
	BankOfUSAAPiConnector boaApiconnector;

	@Autowired
	PaymentRepository paymentRepository;

	SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);

	public ResponseData<PaymentResponse> paymentCharge(PaymentRequest paymentRequest) {

		ResponseData<PaymentResponse> responseData = new ResponseData<>();

		String method = paymentRequest.getMethod();
		if (AppParams.PAYPAL.matches(method)) {
			processPaypal(responseData, paymentRequest);
		} else if (AppParams.PAYPAL_PRO.matches(method)) {
			processPaypalPro(responseData, paymentRequest);
		} else if (AppParams.STRIPE.matches(method)) {
			processStripe(responseData, paymentRequest);
		} else if (AppParams.BRAINTREE.matches(method)) {
			processBraintree(responseData, paymentRequest);
		} else if (AppParams.ANET.matches(method)) {
			processAnet(responseData, paymentRequest);
		} else if (AppParams.BANK_OF_USA.matches(method)) {
			processBankOfUSA(responseData, paymentRequest);
		} else {
			errorResponse(responseData, paymentRequest, SystemCode.PAYMENT_ERROR);
		}

		return responseData;
	}

	private void processPaypal(ResponseData<PaymentResponse> responseData, PaymentRequest paymentRequest) {
		try {
			PaypalAccessTokenResponse accessTokenResponse = paypalApiConnector.createAccessToken();

			String accessToken = accessTokenResponse.getAccessToken();
			if (StringUtils.isEmpty(accessToken)) {
				errorResponse(responseData, paymentRequest, SystemCode.INVALID_ACCESS_TOKEN);
			}

			PaypalCreatePaymentUrlRequest paymentUrlRequest = paypalApiConnector.createPaymentUrlRequest(paymentRequest);
			PaypalCreatePaymentUrlResponse paymentUrlResponse = paypalApiConnector.createPaymentUrl(accessToken, paymentUrlRequest);
			if (HttpResponseStatus.CREATED.code() != paymentUrlResponse.getResponseCode()) {
				errorResponse(responseData, paymentRequest, SystemCode.PAYMENT_ERROR);
			}

			String state = "";
			PaymentResponse paymentResponse = responseData.getCommonData().getResult();

			if (ResourceStates.CREATED.equals(paymentUrlResponse.getState()) && paymentUrlResponse.getLinks().size() > 0) {
				state = ResourceStates.CREATED;
				Link link = paymentUrlResponse.getLinks().get(0);
				link.setRel("approval_url");
				ArrayList<Link> links = new ArrayList<>();
				links.add(link);
				paymentResponse.setLinks(links);

				String approvalUrl = link.getHref();
				if (approvalUrl.indexOf("token=") > 0) {
					String tokenId = approvalUrl.substring(approvalUrl.indexOf("token=") + "token=".length());
					paymentResponse.setTokenId(tokenId);
				}
			} else {
				state = ResourceStates.FAIL;
			}

			Payment insertPayment = Payment.builder()
					.id(paymentUrlResponse.getId())
					.accessToken(accessToken)
					.state(state)
					.reference(paymentUrlResponse.getReference())
					.method(paymentUrlResponse.getMethod())
					.amount(paymentUrlResponse.getAmount())
					.currency(paymentUrlResponse.getCurrency())
					.token(paymentUrlResponse.getType())
					.payerId("")
					.createDate(new Date())
					.dataClob(paymentUrlResponse.toString())
					.build();
			paymentRepository.save(insertPayment);//insert into db

			paymentResponse.setAccountName(paypalApiConnector.getPaypalAccountName());
			paymentResponse.setId(insertPayment.getId());
			paymentResponse.setPayId(insertPayment.getPayId());
			paymentResponse.setReference(insertPayment.getReference());
			paymentResponse.setAmount(insertPayment.getAmount());
			paymentResponse.setCurrency(insertPayment.getCurrency());
			paymentResponse.setMethod(insertPayment.getMethod());
			paymentResponse.setPayer(new Payer());
			paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			paymentResponse.setState(insertPayment.getState());
			responseData.getCommonData().setResult(paymentResponse);
		} catch (IOException e) {
			e.printStackTrace();
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.PAYMENT_ERROR.getCode(), SystemCode.PAYMENT_ERROR.getMessage());
			responseData.getError().add(error);
		}
	}

	private void processPaypalPro(ResponseData<PaymentResponse> responseData, PaymentRequest paymentRequest) {
		try {
			PaypalAccessTokenResponse accessTokenResponse = paypalProApiConnector.createAccessToken();

			String accessToken = accessTokenResponse.getAccessToken();
			if (StringUtils.isEmpty(accessToken)) {
				errorResponse(responseData, paymentRequest, SystemCode.PAYMENT_ERROR);
			}

			PaypalProCreateOrderRequest paymentCreateOrderRequest = paypalProApiConnector.createOrderRequest(paymentRequest);
			PaypalProCreateOrderResponse paymentCreateOrderResponse = paypalProApiConnector.createOrder(accessToken, paymentCreateOrderRequest);

			String state = "";
			PaymentResponse paymentResponse = responseData.getCommonData().getResult();

			if (paymentCreateOrderResponse.getResponseCode() == HttpResponseStatus.CREATED.code() && paymentCreateOrderResponse.getLinks().size() > 0) {
				state = ResourceStates.CREATED;
				Link link = paymentCreateOrderResponse.getLinks().get(0);
				link.setRel("approval_url");
				ArrayList<Link> links = new ArrayList<>();
				links.add(link);
				paymentResponse.setLinks(links);
			} else {
				state = ResourceStates.FAIL;
				Reason errorMap = new Reason();
				errorMap.setMessage(paymentCreateOrderResponse.getMessage());
				paymentResponse.setReason(errorMap);
			}

			Payment insertPayment = Payment.builder()
					.id(paymentCreateOrderResponse.getId())
					.accessToken(accessToken)
					.state(state)
					.reference(paymentRequest.getReference())
					.method(paymentRequest.getMethod())
					.amount(paymentRequest.getAmount())
					.currency(paymentRequest.getCurrency())
					.token(null)
					.payerId(null)
					.createDate(new Date())
					.dataClob(paymentCreateOrderResponse.toString())
					.build();
			paymentRepository.save(insertPayment);//insert into db


			paymentResponse.setAccountName(paypalProApiConnector.getPaypalProAccountName());
			paymentResponse.setId(insertPayment.getId());
			paymentResponse.setPayId(insertPayment.getPayId());
			paymentResponse.setReference(insertPayment.getReference());
			paymentResponse.setAmount(insertPayment.getAmount());
			paymentResponse.setCurrency(insertPayment.getCurrency());
			paymentResponse.setMethod(insertPayment.getMethod());
			paymentResponse.setPayer(new Payer());
			paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			paymentResponse.setState(insertPayment.getState());
			responseData.getCommonData().setResult(paymentResponse);
		} catch (IOException e) {
			e.printStackTrace();
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.PAYMENT_ERROR.getCode(), SystemCode.PAYMENT_ERROR.getMessage());
			responseData.getError().add(error);
		}

	}

	private void processStripe(ResponseData<PaymentResponse> responseData, PaymentRequest paymentRequest) {
		try {
			String token = paymentRequest.getTokenId();
			String amountStripe = String.format("%.2f", GetterUtil.getDouble(paymentRequest.getAmount()));
			long amount = GetterUtil.getLong(amountStripe.replace(".", ""));
			String currency = paymentRequest.getCurrency();
			String reference = paymentRequest.getReference();
			String method = paymentRequest.getMethod();
			Charge charge = stripeApiConnector.charge(token, amount, currency, "Pay with order " + reference);

			String state = "";
			String id = "";
			PaymentResponse paymentResponse = responseData.getCommonData().getResult();
			if (ResourceStates.SUCCEEDED.equals(charge.getStatus())) {
				state = ResourceStates.APPROVED;
				id = charge.getId();

				Link link = new Link();
				ArrayList<Link> links = new ArrayList<>();
				links.add(link);
				paymentResponse.setLinks(links);

			} else {
				id = AppUtil.genRandomId();
				state = ResourceStates.FAIL;
				Reason reason = new Reason();
				reason.setDetails(charge.toString());
				paymentResponse.setReason(reason);
			}

			Payment insertPayment = Payment.builder()
					.id(id)
					.accessToken(token)
					.state(state)
					.reference(reference)
					.method(method)
					.amount(amountStripe)
					.currency(currency)
					.token("")
					.payerId("")
					.createDate(new Date())
					.dataClob(charge.toString())
					.build();
			paymentRepository.save(insertPayment);//insert into db

			// response
			paymentResponse.setAccountName(stripeApiConnector.getStripeAccountName());
			paymentResponse.setId(insertPayment.getId());
			paymentResponse.setPayId(id);
			paymentResponse.setReference(insertPayment.getReference());
			paymentResponse.setAmount(insertPayment.getAmount());
			paymentResponse.setCurrency(insertPayment.getCurrency());
			paymentResponse.setMethod(insertPayment.getMethod());
			paymentResponse.setPayer(new Payer());
			paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			paymentResponse.setState(insertPayment.getState());
			paymentResponse.setTokenId(token);
			responseData.getCommonData().setResult(paymentResponse);

		} catch (StripeException e) {
			e.printStackTrace();
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.PAYMENT_ERROR.getCode(), SystemCode.PAYMENT_ERROR.getMessage());
			responseData.getError().add(error);
		}

	}

	private void processBraintree(ResponseData<PaymentResponse> responseData, PaymentRequest paymentRequest) {
		try {
			String token = paymentRequest.getTokenId();
			String amountstripe = String.format("%.2f", GetterUtil.getDouble(paymentRequest.getAmount()));
			long amount = GetterUtil.getLong(amountstripe.replace(".", ""));
			String currency = paymentRequest.getCurrency();
			String reference = paymentRequest.getReference();
			String method = paymentRequest.getMethod();
			BraintreeChargeResponse charge = braintreeApiConnector.charge(token, amountstripe, currency);
			String state = "";
			String id = "";

			PaymentResponse paymentResponse = responseData.getCommonData().getResult();

			if (charge.getData() != null) {
				state = ResourceStates.APPROVED;
				id = charge.getData().getId();

				Link link = new Link();
				ArrayList<Link> links = new ArrayList<>();
				links.add(link);
				paymentResponse.setLinks(links);

			} else {
				id = AppUtil.genRandomId();
				state = ResourceStates.FAIL;
				Reason reason = new Reason();
				reason.setDetails(charge.getMessage());
				paymentResponse.setReason(reason);
			}

			Payment insertPayment = Payment.builder()
					.id(id)
					.accessToken(token)
					.state(state)
					.reference(reference)
					.method(method)
					.amount(amountstripe)
					.currency(currency)
					.token("")
					.payerId("")
					.createDate(new Date())
					.dataClob(charge.toString())
					.build();
			paymentRepository.save(insertPayment);//insert into db

			// response
			paymentResponse.setId(insertPayment.getId());
			paymentResponse.setPayId(id);
			paymentResponse.setReference(insertPayment.getReference());
			paymentResponse.setAmount(insertPayment.getAmount());
			paymentResponse.setCurrency(insertPayment.getCurrency());
			paymentResponse.setMethod(insertPayment.getMethod());
			paymentResponse.setPayer(new Payer());
			paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			paymentResponse.setState(insertPayment.getState());
			paymentResponse.setTokenId(token);
			responseData.getCommonData().setResult(paymentResponse);

		} catch (StripeException e) {
			e.printStackTrace();
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.PAYMENT_ERROR.getCode(), SystemCode.PAYMENT_ERROR.getMessage());
			responseData.getError().add(error);
		}

	}

	private void processAnet(ResponseData<PaymentResponse> responseData, PaymentRequest paymentRequest) {
		String token = paymentRequest.getTokenId();
		String amountStripe = paymentRequest.getAmount();
		double amount = GetterUtil.getDouble(amountStripe);
		String currency = paymentRequest.getCurrency();
		String reference = paymentRequest.getReference();
		String ip = paymentRequest.getIp();
		String method = paymentRequest.getMethod();

//		JsonObject customerJson = requestBodyJson.getJsonObject(AppParams.CUSTOMER);
//		String email = customerJson.getString(AppParams.EMAIL);

		String decryptedPassword = new String(java.util.Base64.getDecoder().decode(token));
		AesUtil aesUtil = new AesUtil(128, 1000);

		CreditCardType creditCard = null;

		String last4 = null, expire = null;

//		log.info( "decryptedPassword=" + decryptedPassword);
//		log.info( "aesEncryptionKey=" + aesEncryptionKey);

		if (decryptedPassword != null && decryptedPassword.split("::").length == 3) {

			String password = aesUtil.decrypt(decryptedPassword.split("::")[1], decryptedPassword.split("::")[0],
					aesEncryptionKey, decryptedPassword.split("::")[2]);

//			log.info( "password=" + password);

			if (password != null && password.split("\\|").length == 3) {
				creditCard = new CreditCardType();
				String cardNumber = password.split("\\|")[0];
				creditCard.setCardNumber(cardNumber);
				expire = password.split("\\|")[1];
				creditCard.setExpirationDate(expire);
				creditCard.setCardCode(password.split("\\|")[2]);

				if (cardNumber.length() < 4) {
					throw new IllegalArgumentException("Invalid credit card number!");
				} else {
					last4 = cardNumber.substring(cardNumber.length() - 4);

				}

			} else {
				responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
				ResponseData.Error error = new ResponseData.Error(SystemCode.INVALID_ENCRYPTION.getCode(), SystemCode.INVALID_ENCRYPTION.getMessage());
				responseData.getError().add(error);
			}
		} else {
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.INVALID_ENCRYPTION.getCode(), SystemCode.INVALID_ENCRYPTION.getMessage());
			responseData.getError().add(error);
		}

		Shipping billing = paymentRequest.getBilling();
		Shipping shipping = paymentRequest.getShipping();


		if (ObjectUtils.isEmpty(billing)) {
			billing = shipping;
		}

		String email = billing.getEmailAddress();

		CustomerDataType customer = null;
		if (StringUtils.isNotEmpty(email)) {
			customer = new CustomerDataType();
			customer.setEmail(email);
		}

		ShippingAddress billAddress = billing.getAddress();
		CustomerAddressType billTo = new CustomerAddressType();
		billTo.setFirstName(billing.getName());
		if (StringUtils.isEmpty(billAddress.getAddrLine2())) {
			billTo.setAddress(billAddress.getAddrLine1());
		} else {
			billTo.setAddress(billAddress.getAddrLine1() + ", " + billAddress.getAddrLine2());
		}
		billTo.setCity(billAddress.getCity());
		billTo.setCountry(billAddress.getCountryCode());
		billTo.setPhoneNumber(billAddress.getPhone());
		billTo.setState(billAddress.getState());
		billTo.setZip(billAddress.getPostalCode());

		ShippingAddress shipAddress = shipping.getAddress();
		CustomerAddressType shipTo = new CustomerAddressType();
		shipTo.setFirstName(billing.getName());
		if (StringUtils.isEmpty(shipAddress.getAddrLine2())) {
			shipTo.setAddress(shipAddress.getAddrLine1());
		} else {
			shipTo.setAddress(shipAddress.getAddrLine1() + ", " + shipAddress.getAddrLine2());
		}
		shipTo.setCity(shipAddress.getCity());
		shipTo.setCountry(shipAddress.getCountryCode());
		shipTo.setPhoneNumber(shipAddress.getPhone());
		shipTo.setState(shipAddress.getState());
		shipTo.setZip(shipAddress.getPostalCode());

		CreateTransactionResponse response = anetApiConnector.chargeCreditCard(creditCard, customer, shipTo, billTo, amount, ip);

		boolean success = false;
		String chargeResponse = null, message = null, transactionId = null;

		if (response != null) {
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
				TransactionResponse result = response.getTransactionResponse();
				if (result.getMessages() != null) {
					success = true;
					transactionId = result.getTransId();
					log.info("Successfully created transaction with Transaction ID: " + result.getTransId());
					log.info("Response Code: " + result.getResponseCode());
					log.info("Message Code: " + result.getMessages().getMessage().get(0).getCode());
					log.info("Description: " + result.getMessages().getMessage().get(0).getDescription());
					log.info("Auth Code: " + result.getAuthCode());

					chargeResponse = result.getTransId() + "\r\n" + "Response Code: " + result.getResponseCode()
							+ "Message Code: " + result.getMessages().getMessage().get(0).getCode() + "Description: "
							+ result.getMessages().getMessage().get(0).getDescription() + "Auth Code: " + result.getAuthCode();

				} else {
					log.info("Failed Transaction.");
					if (response.getTransactionResponse().getErrors() != null) {
						log.info("Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
						log.info("Error message: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorText());

						success = false;
						message = response.getTransactionResponse().getErrors().getError().get(0).getErrorText();
						chargeResponse = "Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode() + "\r\n" + "Error message: " + message;
					}
				}
			} else {
				success = false;
				chargeResponse = "Failed Transaction.";
				log.info("Failed Transaction.");
				if (response.getTransactionResponse() != null && response.getTransactionResponse().getErrors() != null) {
					log.info("Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
					log.info("Error message: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorText());

					message = response.getTransactionResponse().getErrors().getError().get(0).getErrorText();

					chargeResponse += "\r\n" + "Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode() + "\r\n" + "Error message: " + message;
				} else {
					log.info("Error Code: " + response.getMessages().getMessage().get(0).getCode());
					log.info("Error message: " + response.getMessages().getMessage().get(0).getText());

					message = response.getMessages().getMessage().get(0).getText();
					chargeResponse = "Error Code: " + response.getMessages().getMessage().get(0).getCode() + "\r\n" + "Error message: " + message;
				}
			}
		} else {
			success = false;
			message = chargeResponse = "Failed to get response";
			log.info(chargeResponse);
		}

		String id = AppUtil.genRandomId();
		String state = success ? ResourceStates.APPROVED:ResourceStates.FAIL;

		Payment insertPayment = Payment.builder()
				.id(id)
				.accessToken(token)
				.state(state)
				.reference(reference)
				.method(method)
				.amount(amountStripe)
				.currency(currency)
				.token("")
				.payerId("")
				.createDate(new Date())
				.dataClob(chargeResponse.toString())
				.last4(last4)
				.expire(expire)
				.build();
		paymentRepository.save(insertPayment);//insert into db

		// response
		PaymentResponse paymentResponse = responseData.getCommonData().getResult();
		paymentResponse.setAccountName(anetApiConnector.getAnetAccountName());
		paymentResponse.setId(insertPayment.getId());
		paymentResponse.setPayId(id);
		paymentResponse.setReference(insertPayment.getReference());
		paymentResponse.setAmount(insertPayment.getAmount());
		paymentResponse.setCurrency(insertPayment.getCurrency());
		paymentResponse.setMethod(insertPayment.getMethod());
		paymentResponse.setPayer(new Payer());
		paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
		paymentResponse.setState(insertPayment.getState());
		paymentResponse.setTokenId(token);
		responseData.getCommonData().setResult(paymentResponse);
	}

	private void processBankOfUSA(ResponseData<PaymentResponse> responseData, PaymentRequest paymentRequest) {
		String token = paymentRequest.getTokenId();
		String amount = paymentRequest.getAmount();
		String currency = paymentRequest.getCurrency();
		String reference = paymentRequest.getReference();
		String ip = paymentRequest.getIp();
		String method = paymentRequest.getMethod();
		String decryptedPassword = new String(java.util.Base64.getDecoder().decode(token));
		AesUtil aesUtil = new AesUtil(128, 1000);

		String cardNumber = "";
		String expire = "";

		if (decryptedPassword.split("::").length == 3) {
			String password = aesUtil.decrypt(decryptedPassword.split("::")[1], decryptedPassword.split("::")[0],
					aesEncryptionKey, decryptedPassword.split("::")[2]);
			if (password != null && password.split("\\|").length == 3) {
				cardNumber = password.split("\\|")[0];
				expire = password.split("\\|")[1];
			} else {
				responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
				ResponseData.Error error = new ResponseData.Error(SystemCode.INVALID_ENCRYPTION.getCode(), SystemCode.INVALID_ENCRYPTION.getMessage());
				responseData.getError().add(error);
			}
		} else {
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.INVALID_ENCRYPTION.getCode(), SystemCode.INVALID_ENCRYPTION.getMessage());
			responseData.getError().add(error);
		}
		Shipping billing = paymentRequest.getBilling();
		Shipping shipping = paymentRequest.getShipping();


		if (ObjectUtils.isEmpty(billing)) {
			billing = shipping;
		}
		log.info("billing = " + billing.toString());
		String cardHolderName = billing.getName();

		BankUSAChargeRequest bankUSAChargeRequest = new BankUSAChargeRequest(cardNumber, amount, expire, cardHolderName);
		BankUSAChargeResponse chargeResponse = null;
		try {
			chargeResponse = boaApiconnector.purchase(bankUSAChargeRequest);

			String state = "";
			String dataClob = "";
			String id = "";
			if (HttpResponseStatus.CREATED.code() == chargeResponse.getResponseCode()) {
				state = ResourceStates.APPROVED;
				String transactionTag = String.valueOf(chargeResponse.getTransactionTag());
				String authorizationNum = chargeResponse.getAuthorizationNum();
				id = transactionTag + "|" + authorizationNum;
				dataClob = chargeResponse.getCtr();
			} else {
				id = AppUtil.genRandomId();
				state = ResourceStates.FAIL;
				dataClob = chargeResponse.toString();
			}

			Payment insertPayment = Payment.builder()
					.id(id)
					.accessToken(token)
					.state(state)
					.reference(reference)
					.method(method)
					.amount(amount)
					.currency(currency)
					.token("")
					.payerId("")
					.createDate(new Date())
					.dataClob(dataClob)
					.expire(expire)
					.build();
			paymentRepository.save(insertPayment);//insert into db

			// response
			PaymentResponse paymentResponse = responseData.getCommonData().getResult();
			paymentResponse.setAccountName(bankOfUSAAccountName);
			paymentResponse.setId(insertPayment.getId());
			paymentResponse.setPayId(id);
			paymentResponse.setReference(insertPayment.getReference());
			paymentResponse.setAmount(insertPayment.getAmount());
			paymentResponse.setCurrency(insertPayment.getCurrency());
			paymentResponse.setMethod(insertPayment.getMethod());
			paymentResponse.setPayer(new Payer());
			paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			paymentResponse.setState(insertPayment.getState());
			paymentResponse.setTokenId(token);
			Link link = new Link();
			ArrayList<Link> links = new ArrayList<>();
			links.add(link);
			paymentResponse.setLinks(links);
			responseData.getCommonData().setResult(paymentResponse);

		} catch (NoSuchAlgorithmException | InvalidKeyException | IOException e) {
			e.printStackTrace();
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.PAYMENT_ERROR.getCode(), SystemCode.PAYMENT_ERROR.getMessage());
			responseData.getError().add(error);
		}
	}

	private void errorResponse(ResponseData<PaymentResponse> responseData, PaymentRequest paymentRequest, SystemCode systemCode) {

		Payment insertPayment = Payment.builder()
				.id(AppUtil.genRandomId())
				.amount(paymentRequest.getAmount())
				.currency(paymentRequest.getCurrency())
				.reference(paymentRequest.getReference())
				.method(paymentRequest.getMethod())
				.state(ResourceStates.FAIL)
				.data(paymentRequest.toString())
				.createDate(new Date())
				.build();
		paymentRepository.save(insertPayment);//insert into db

		Reason reason = new Reason();
		reason.setMessage(systemCode.getMessage());

		// response
		PaymentResponse paymentResponse = responseData.getCommonData().getResult();
		paymentResponse.setId(insertPayment.getId());
		paymentResponse.setPayId(insertPayment.getPayId());
		paymentResponse.setReference(insertPayment.getReference());
		paymentResponse.setAmount(insertPayment.getAmount());
		paymentResponse.setCurrency(insertPayment.getCurrency());
		paymentResponse.setMethod(insertPayment.getMethod());
		paymentResponse.setPayer(new Payer());
		paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
		paymentResponse.setUpdateTime(null);
		paymentResponse.setState(ResourceStates.FAIL);
		paymentResponse.setReason(reason);
		paymentResponse.setLinks(new ArrayList<>());
		paymentResponse.setTokenId("");
		responseData.getCommonData().setResult(paymentResponse);

		responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
		ResponseData.Error error = new ResponseData.Error(systemCode.getCode(), systemCode.getMessage());
		responseData.getError().add(error);
	}

}
