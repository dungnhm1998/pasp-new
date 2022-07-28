package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.external.AnetApiConnector;
import asia.leadsgen.pasp.data.access.external.PaypalApiConnector;
import asia.leadsgen.pasp.data.access.external.PaypalProApiConnector;
import asia.leadsgen.pasp.data.access.external.StripeApiConnector;
import asia.leadsgen.pasp.data.access.repository.PaymentAccountRepository;
import asia.leadsgen.pasp.data.access.repository.PaymentRepository;
import asia.leadsgen.pasp.entity.Payment;
import asia.leadsgen.pasp.entity.PaymentAccount;
import asia.leadsgen.pasp.error.SystemError;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.external.BankOfUSA.BankUSARefundRequest;
import asia.leadsgen.pasp.model.dto.external.BankOfUSA.BankUSARefundResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalAccessTokenResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalRefundSaleResponse;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PaypalProRefundRequest;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PaypalProRefundResponse;
import asia.leadsgen.pasp.model.dto.payment.refund.PaymentRefundRequest;
import asia.leadsgen.pasp.model.dto.payment.refund.PaymentRefundResponse;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.AppUtil;
import asia.leadsgen.pasp.util.BankOfUSAAPiConnector;
import asia.leadsgen.pasp.util.ResourceStates;
import com.stripe.exception.StripeException;
import com.stripe.model.Refund;
import lombok.extern.log4j.Log4j2;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.TransactionResponse;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Log4j2
public class PaymentRefundService {

	@Autowired
	PaypalApiConnector paypalApiConnector;
	@Autowired
	PaypalProApiConnector paypalProApiConnector;
	@Autowired
	StripeApiConnector stripeApiConnector;
	@Autowired
	BankOfUSAAPiConnector boaApiconnector;
	@Autowired
	AnetApiConnector anetApiConnector;
	@Autowired
	PaymentAccountRepository paymentAccountRepository;

	@Autowired
	PaymentRepository paymentRepository;

	SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);

	public ResponseData<PaymentRefundResponse> refund(PaymentRefundRequest requestbody) {
		PaymentRefundResponse refundResponse = new PaymentRefundResponse();

		String id = requestbody.getId();
		String method = requestbody.getMethod();
		if (StringUtils.isEmpty(id)) {
			return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
		}

		String accountName = requestbody.getAccountName();
		PaymentAccount account = null;
		if (StringUtils.isNotEmpty(accountName)) {
			account = paymentAccountRepository.findByName(accountName).orElse(null);
		}


		if (AppParams.PAYPAL.matches(method)) {
			return refundPaypal(refundResponse, requestbody, account);
		} else if (AppParams.PAYPAL_PRO.matches(method)) {
			return refundPaypalPaypalPro(refundResponse, requestbody, account);
		} else if (AppParams.STRIPE.matches(method)) {
			return refundStripe(refundResponse, requestbody, account);
		} else if (AppParams.BRAINTREE.matches(method)) {
//			return refundBraintree(refundResponse, requestbody, account);
		} else if (AppParams.ANET.matches(method)) {
			return refundAnet(refundResponse, requestbody, account);
		} else if (AppParams.BANK_OF_USA.matches(method)) {
			return refundBankOfUSA(refundResponse, requestbody, account);
		} else {
			return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
		}

		return ResponseData.ok(refundResponse);
	}

	private ResponseData<PaymentRefundResponse> refundPaypal(PaymentRefundResponse refundResponse, PaymentRefundRequest requestbody, PaymentAccount account) {
		try {
			String authorization = "";
			String bnCode;
			if (account == null) {
				authorization = paypalApiConnector.getBase64encodedCredentials();
				bnCode = paypalApiConnector.getRestBNCode();
			} else {
				authorization = paypalApiConnector.getBase64encodedCredentials(account.getPpClientId(), account.getPpClientSecret());
				bnCode = account.getPpBncode();
			}

			PaypalAccessTokenResponse accessTokenResponse = paypalApiConnector.createAccessToken(authorization, bnCode);
			String accessToken = accessTokenResponse.getAccessToken();
			if ((accessToken == null) || accessToken.equalsIgnoreCase("")) {
				return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
			}

			PaypalRefundSaleResponse refundSaleResponse = paypalApiConnector.refundSale(accessToken, requestbody.getId(), requestbody.getRefundInfo(), bnCode);
			if (refundSaleResponse.getResponseCode() == 400) {
				return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
			}

			String state = "";
			String id = "";
			String total = refundSaleResponse.getAmount().getTotal();
			String currency = refundSaleResponse.getAmount().getCurrency();
			String message = refundSaleResponse.getMessage();


			if (ResourceStates.COMPLETED.equals(refundSaleResponse.getState())) {
				id = refundSaleResponse.getId();
				state = ResourceStates.APPROVED;
			} else {
				id = AppUtil.genRandomId();
				state = ResourceStates.FAIL;
			}

			Payment insertPayment = Payment.builder()
					.id(id)
					.accessToken(accessToken)
					.state(state)
					.reference("")
					.method(AppParams.PAYPAL)
					.amount(total)
					.currency(currency)
					.token("")
					.payerId("")
					.createDate(new Date())
					.dataClob(refundResponse.toString())
					.build();
			paymentRepository.save(insertPayment);//insert into db

			refundResponse.setId(insertPayment.getId());
			refundResponse.setStatus(refundSaleResponse.getState());

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseData.failed(SystemError.PAYMENT_ERROR, null);
		}
		return ResponseData.ok(refundResponse);
	}

	private ResponseData<PaymentRefundResponse> refundPaypalPaypalPro(PaymentRefundResponse refundResponse, PaymentRefundRequest requestbody, PaymentAccount account) {
		try {

			String xClientId = "";
			String xClientSecret = "";
			if (account == null) {
				xClientId = paypalProApiConnector.getClientId();
				xClientSecret = paypalProApiConnector.getClientSecret();
			} else {
				xClientId = account.getPpClientId();
				xClientSecret = account.getPpClientSecret();
			}

			PaypalAccessTokenResponse accessTokenResponse = paypalProApiConnector.createAccessToken(xClientId, xClientSecret);
			String accessToken = accessTokenResponse.getAccessToken();
			if (StringUtils.isEmpty(accessToken)) {
				return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
			}

			PaypalProRefundRequest paypalProRefundRequest = paypalProApiConnector.createRefundRequest(requestbody);
			PaypalProRefundResponse paypalProRefundResponse = paypalProApiConnector.refundSale(accessToken, requestbody.getId(), paypalProRefundRequest);
			if (paypalProRefundResponse.getResponseCode() == 201 && ResourceStates.COMPLETED.equals(paypalProRefundResponse.getStatus())) {
				List<Link> links = paypalProRefundResponse.getLinks();
				Link link = links.stream().filter(o -> o.getHref().contains("payments/refunds")).findFirst().orElse(null); // example: https://api-m.paypal.com/v2/payments/refunds/1JU08902781691411

				if (link != null) {
					String refundLink = link.getHref();
					PaypalProRefundResponse refundDetail = paypalProApiConnector.getRefundDetail(xClientId, xClientSecret, refundLink);
					if (refundDetail.getResponseCode() == 200) {
						String total = refundDetail.getAmountPro().getValue();
						String currency = refundDetail.getAmountPro().getCurrency();
						Payment insertPayment = Payment.builder()
								.id(paypalProRefundResponse.getId())
								.accessToken(accessToken)
								.state(ResourceStates.APPROVED)
								.reference("")
								.method(AppParams.PAYPAL)
								.amount(total)
								.currency(currency)
								.token("")
								.payerId("")
								.createDate(new Date())
								.dataClob(refundResponse.toString())
								.build();
						paymentRepository.save(insertPayment);//insert into db

						refundResponse.setId(insertPayment.getId());
						refundResponse.setStatus(ResourceStates.SUCCEEDED);
					} else {
						return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
					}
				} else {
					return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
				}
			} else {
				return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
			}


		} catch (IOException e) {
			e.printStackTrace();
			return ResponseData.failed(SystemError.PAYMENT_ERROR, null);
		}
		return ResponseData.ok(refundResponse);
	}

	private ResponseData<PaymentRefundResponse> refundStripe(PaymentRefundResponse refundResponse, PaymentRefundRequest requestbody, PaymentAccount account) {
		try {
			long amount = 0l;
			if (!ObjectUtils.isEmpty(requestbody.getRefundInfo())) {
				amount = Long.parseLong(requestbody.getRefundInfo().getAmount().getTotal());
			}

			Refund stripeRefundResponse = stripeApiConnector.createRefund(account, requestbody.getId(), amount);

			String id = "";
			String state = "";
			if (StringUtils.isNotEmpty(stripeRefundResponse.getStatus())) {
				id = stripeRefundResponse.getId();
				state = ResourceStates.APPROVED;
			} else {
				id = AppUtil.genRandomId();
				state = ResourceStates.FAIL;
			}

			Payment insertPayment = Payment.builder()
					.id(id)
					.accessToken("")
					.state(state)
					.reference("")
					.method(AppParams.STRIPE)
					.amount(String.valueOf(stripeRefundResponse.getAmount()))
					.currency(stripeRefundResponse.getCurrency())
					.token("")
					.payerId("")
					.createDate(new Date())
					.dataClob(refundResponse.toString())
					.build();
			paymentRepository.save(insertPayment);//insert into db

			refundResponse.setId(insertPayment.getId());
			refundResponse.setStatus(stripeRefundResponse.getStatus());

		} catch (StripeException e) {
			e.printStackTrace();
			return ResponseData.failed(SystemError.PAYMENT_ERROR, null);
		}
		return ResponseData.ok(refundResponse);
	}

	private ResponseData<PaymentRefundResponse> refundAnet(PaymentRefundResponse refundResponse, PaymentRefundRequest requestbody, PaymentAccount account) {
		String transactionId = requestbody.getId();
		Payment payment = paymentRepository.findByPayId(transactionId).orElse(null);
		if (payment == null) {
			return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
		}

		String currency = payment.getCurrency();
		String last4 = payment.getLast4();
		String expireDate = payment.getExpire();
		String note = requestbody.getNote();

		String transactionAmount = "";
		if (ObjectUtils.isEmpty(requestbody.getRefundInfo())) {
			transactionAmount = payment.getAmount();
		} else {
			transactionAmount = requestbody.getRefundInfo().getAmount().getTotal();
		}

		String id = "";
		String state = "";
		String status = "";
		String message = "";
		JSONObject res = new JSONObject();

		CreateTransactionResponse response = anetApiConnector.refundTransaction(transactionId, transactionAmount, last4, expireDate, account);
		if (response != null) {
			// If API Response is ok, go ahead and check the transaction response
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
				TransactionResponse transactionResponse = response.getTransactionResponse();
				if (transactionResponse.getMessages() != null) {
					id = transactionId;
					state = ResourceStates.APPROVED;
					status = ResourceStates.SUCCEEDED;
					res.put("transaction_id", transactionResponse.getTransId());
					res.put("response_code", transactionResponse.getResponseCode());
					res.put("message_code", transactionResponse.getMessages().getMessage().get(0).getCode());
					res.put("description", transactionResponse.getMessages().getMessage().get(0).getDescription());
					res.put("auth_code", transactionResponse.getAuthCode());
					res.put("success", true);
					message = res.toString();

				} else {
					id = AppUtil.genRandomId();
					state = ResourceStates.FAIL;
					status = ResourceStates.FAIL;
					if (response.getTransactionResponse().getErrors() != null) {
						res.put("transaction_id", transactionResponse.getTransId());
						res.put("error_code", response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
						res.put("error_message", response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
						res.put("success", false);
						message = res.toString();
					}
				}
			} else {
				id = AppUtil.genRandomId();
				state = ResourceStates.FAIL;
				status = ResourceStates.FAIL;
				if (response.getTransactionResponse() != null && response.getTransactionResponse().getErrors() != null) {
					res.put("transaction_id", response.getTransactionResponse().getTransId());
					res.put("error_code", response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
					res.put("error_message", response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
					res.put("success", false);
					message = res.toString();
				} else {
					res.put("error_code", response.getMessages().getMessage().get(0).getCode());
					res.put("error_message", response.getMessages().getMessage().get(0).getText());
					res.put("success", false);
					message = res.toString();

				}
			}
		} else {
			res.put("error_message", "Null Response.");
			res.put("success", false);
			message = res.toString();
		}
		log.info("message : " + message);

		Payment insertPayment = Payment.builder()
				.id(id)
				.accessToken("")
				.state(state)
				.reference("")
				.method(AppParams.ANET)
				.amount(transactionAmount)
				.currency(currency)
				.token("")
				.payerId("")
				.createDate(new Date())
				.dataClob(message.toString())
				.build();
		paymentRepository.save(insertPayment);//insert into db

		refundResponse.setId(insertPayment.getId());
		refundResponse.setStatus(status);


		return ResponseData.ok(refundResponse);
	}

	private ResponseData<PaymentRefundResponse> refundBankOfUSA(PaymentRefundResponse refundResponse, PaymentRefundRequest requestbody, PaymentAccount account) {
		try {
			String transactionId = requestbody.getId();
			if (!transactionId.contains("|")) {
				return errorResponse(refundResponse, requestbody, SystemError.INLVAID_PAYMENT_TRANSACTION.getMessage());
			}

			String transactionTag = transactionId.split("\\|")[0];
			String authorizationNum = transactionId.split("\\|")[1];

			Payment payment = paymentRepository.findByPayId(transactionId).orElse(null);
			if (payment == null) {
				return errorResponse(refundResponse, requestbody, SystemError.PAYMENT_ERROR.getMessage());
			}

			String transactionAmount = "";
			String currency = "";
			if (ObjectUtils.isEmpty(requestbody.getRefundInfo())) {
				transactionAmount = payment.getAmount();
				currency = payment.getCurrency();
			} else {
				transactionAmount = requestbody.getRefundInfo().getAmount().getTotal();
				currency = requestbody.getRefundInfo().getAmount().getCurrency();
			}

			String xClientId = "";
			String xClientSecret = "";
			if (account == null) {
				xClientId = boaApiconnector.getGatewayId();
				xClientSecret = boaApiconnector.getGatewayPassword();
			} else {
				xClientId = account.getBoaGwId();
				xClientSecret = account.getBoaGwPw();
			}

			String id = "";
			String state = "";
			String status = "";
			String dataMessage = "";
			BankUSARefundRequest boaRefundRequest = new BankUSARefundRequest(transactionAmount, transactionTag, authorizationNum, xClientId, xClientSecret);
			BankUSARefundResponse boaRefundResoponse = boaApiconnector.refund(boaRefundRequest, account);
			if (boaRefundResoponse.getResponseCode() == 201){
				id = transactionId;
				status = ResourceStates.SUCCEEDED;
				state = ResourceStates.APPROVED;
				dataMessage = "true";
			}else{
				id = AppUtil.genRandomId();
				status = ResourceStates.FAIL;
				state = ResourceStates.FAIL;
				dataMessage = "false";
			}
			JSONObject res = new JSONObject();
			res.put("success", dataMessage);

			Payment insertPayment = Payment.builder()
					.id(id)
					.accessToken("")
					.state(state)
					.reference("")
					.method(AppParams.BANK_OF_USA)
					.amount(transactionAmount)
					.currency(currency)
					.token("")
					.payerId("")
					.createDate(new Date())
					.dataClob(res.toString())
					.build();
			paymentRepository.save(insertPayment);//insert into db

			refundResponse.setId(insertPayment.getId());
			refundResponse.setStatus(status);
			refundResponse.setMessage(dataMessage);

		} catch (NoSuchAlgorithmException | InvalidKeyException | IOException e) {
			e.printStackTrace();
			return ResponseData.failed(SystemError.PAYMENT_ERROR, null);
		}


		return ResponseData.ok(refundResponse);
	}

	private ResponseData<PaymentRefundResponse> errorResponse(PaymentRefundResponse refundResponse, PaymentRefundRequest paymentRequest, String message) {

		Payment insertPayment = Payment.builder()
				.id(AppUtil.genRandomId())
				.state(ResourceStates.FAIL)
				.method(paymentRequest.getMethod())
				.amount(paymentRequest.getRefundInfo().getAmount().getTotal())
				.currency(paymentRequest.getRefundInfo().getAmount().getCurrency())
				.createDate(new Date())
				.build();
		paymentRepository.save(insertPayment);//insert into db

		refundResponse.setId(insertPayment.getId());
		refundResponse.setStatus(ResourceStates.FAIL);

		return ResponseData.ok(refundResponse);
	}
}
