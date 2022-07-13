package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.external.PaypalApiConnector;
import asia.leadsgen.pasp.data.access.external.PaypalProApiConnector;
import asia.leadsgen.pasp.data.access.repository.PaymentRepository;
import asia.leadsgen.pasp.entity.Payment;
import asia.leadsgen.pasp.error.SystemError;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.payment.PaymentRequest;
import asia.leadsgen.pasp.model.dto.payment.PaymentResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.PaypalAccessTokenResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.PaypalCreatePaymentUrlRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.PaypalCreatePaymentUrlResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Reason;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalProCreateOrderRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalProCreateOrderResponse;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.AppUtil;
import asia.leadsgen.pasp.util.GetterUtil;
import asia.leadsgen.pasp.util.ResourceStates;
import com.sun.xml.bind.v2.TODO;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Log4j2
public class PaymentService {

	@Value("${paypal.service.merchant_id}")
	private static String aesEncryptionKey;
	@Value("${paypal.service.merchant_id}")
	private static String paypalAccountName;
	@Value("${paypal.service.merchant_id}")
	private static String paypalProAccountName;
	@Value("${paypal.service.merchant_id}")
	private static String stripeAccountName;
	@Value("${paypal.service.merchant_id}")
	private static String anetAccountName;
	@Value("${paypal.service.merchant_id}")
	private static String bankOfUSAAccountName;

	@Autowired
	PaypalApiConnector paypalApiConnector;
	@Autowired
	PaypalProApiConnector paypalProApiConnector;
	@Autowired
	PaymentRepository paymentRepository;

	SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);

	public ResponseData<PaymentResponse> paymentCharge(String userId, PaymentRequest paymentRequest) {

		ResponseData<PaymentResponse> paymentResponse = new ResponseData<>();
		String method = paymentRequest.getMethod();
		if (AppParams.PAYPAL.matches(method)) {
			return processPaypal(paymentRequest);
		} else if (AppParams.PAYPAL_PRO.matches(method)) {
			return processPaypalPro(paymentRequest);
		}else if (AppParams.STRIPE.matches(method)) {
			return processPaypalPro(paymentRequest);
		}else if (AppParams.BRAINTREE.matches(method)) {
//			TODO;
		}else if (AppParams.ANET.matches(method)) {
//			TODO;
		} else if (AppParams.BANK_OF_USA.matches(method)) {
//			TODO;
		} else {
//			TODO;
		}

		return paymentResponse;
	}

	private ResponseData<PaymentResponse> processPaypal(PaymentRequest paymentRequest) {
		PaymentResponse paymentResponse = new PaymentResponse();
		try {
			PaypalAccessTokenResponse accessTokenResponse = paypalApiConnector.createAccessToken();

			String accessToken = accessTokenResponse.getAccessToken();
			if ((accessToken == null) || accessToken.equalsIgnoreCase("")) {
				errorResponse(paymentResponse, paymentRequest, SystemError.PAYMENT_ERROR.getMessage());
				return ResponseData.ok(paymentResponse);
			}

			PaypalCreatePaymentUrlRequest paymentUrlRequest = paypalApiConnector.createPaymentUrlRequest(paymentRequest);
			PaypalCreatePaymentUrlResponse paymentUrlResponse = paypalApiConnector.createPaymentUrl(accessToken, paymentUrlRequest);
			if ((paymentUrlResponse == null)) {
				errorResponse(paymentResponse, paymentRequest, SystemError.PAYMENT_ERROR.getMessage());
				return ResponseData.ok(paymentResponse);
			}

			String state = "";
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

			paymentResponse.setAccountName(paypalAccountName);
			paymentResponse.setId(insertPayment.getId());
			paymentResponse.setPayId(insertPayment.getPayId());
			paymentResponse.setReference(insertPayment.getReference());
			paymentResponse.setAmount(insertPayment.getAmount());
			paymentResponse.setCurrency(insertPayment.getCurrency());
			paymentResponse.setMethod(insertPayment.getMethod());
			paymentResponse.setPayer(new Payer());
			paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			paymentResponse.setState(insertPayment.getState());

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseData.failed(SystemError.PAYMENT_ERROR, null);
		}

		return ResponseData.ok(paymentResponse);
	}

//	private ResponseData<PaymentResponse> processStripe(PaymentRequest paymentRequest) {
//		String token = paymentRequest.getTokenId();
//		String amountstripe = String.format("%.2f", GetterUtil.getDouble(paymentRequest.getAmount()));
//		long amount = Common.parseLong(amountstripe.replace(".", ""), 0l);
//		String currency = paymentRequest.getString(AppParams.CURRENCY);
//		String reference = paymentRequest.getString(AppParams.REFERENCE);
//		StripeService stripeService = new StripeService();
//		Map result = stripeService.charge(token, amount, currency, "Pay with order " + reference);
//		String id = ParamUtil.getString(result, AppParams.ID);
//		String status = ParamUtil.getString(result, AppParams.STATUS, "");
//
//		Map insertData;
//		String state;
//		if (status.matches(AppParams.SUCCEEDED)) {
//			state = AppParams.APPROVED;
//			insertData = PaymentService.insert(id, token, AppParams.APPROVED, reference, samount, currency,
//					null, null, result.toString(), method);
//		} else {
//			state = AppParams.FAIL;
//			insertData = PaymentService.insert(null, token, AppParams.FAIL, reference, samount, currency,
//					null, null, result.toString(), method);
//		}
//
//		// response
//		PaymentResponse paymentResponse = new PaymentResponse();
//		paymentResponse.put("account_name", stripeAccountName);
//		paymentResponse.put(AppParams.ID, insertData.get(AppParams.ID));
//		paymentResponse.put(AppParams.PAY_ID, id);
//		paymentResponse.put(AppParams.TOKEN_ID, token);
//		paymentResponse.put(AppParams.REFERENCE, insertData.get(AppParams.REFERENCE));
//		paymentResponse.put(AppParams.AMOUNT, insertData.get(AppParams.AMOUNT));
//		paymentResponse.put(AppParams.CURRENCY, insertData.get(AppParams.CURRENCY));
//		paymentResponse.put(AppParams.METHOD, insertData.get(AppParams.METHOD));
//		Map payer = new HashMap();
//		paymentResponse.put(AppParams.PAYER, payer);
//		paymentResponse.put(AppParams.CREATE_TIME, insertData.get(AppParams.CREATE_TIME));
//		paymentResponse.put(AppParams.UPDATE_TIME, null);
//		paymentResponse.put(AppParams.STATE, state);
//		if (state.matches(AppParams.FAIL)) {
////						responeMap.put(AppParams.REASON, SystemError.PAYMENT_ERROR);
//			paymentResponse.put(AppParams.REASON, result);
//		} else {
//			paymentResponse.put(AppParams.REASON, null);
//		}
//
//		Map link = new HashMap();
//		ArrayList links = new ArrayList();
//		links.add(link);
//		paymentResponse.put(AppParams.LINKS, links);
//
//		return ResponseData.ok(paymentResponse);
//	}
	private ResponseData<PaymentResponse> processPaypalPro(PaymentRequest paymentRequest) {
		PaymentResponse paymentResponse = new PaymentResponse();
		try {
			PaypalAccessTokenResponse accessTokenResponse = paypalProApiConnector.createAccessToken();

			String accessToken = accessTokenResponse.getAccessToken();
			if ((accessToken == null) || accessToken.equalsIgnoreCase("")) {
				errorResponse(paymentResponse, paymentRequest, SystemError.PAYMENT_ERROR.getMessage());
				return ResponseData.ok(paymentResponse);
			}

			PaypalProCreateOrderRequest paymentCreateOrderRequest = paypalProApiConnector.createOrderRequest(paymentRequest);
			PaypalProCreateOrderResponse paymentCreateOrderResponse = paypalProApiConnector.createOrder(accessToken, paymentCreateOrderRequest);
			if ((paymentCreateOrderResponse == null)) {
				errorResponse(paymentResponse, paymentRequest, SystemError.PAYMENT_ERROR.getMessage());
				return ResponseData.ok(paymentResponse);
			}


			String state = "";
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


			paymentResponse.setAccountName(paypalProAccountName);
			paymentResponse.setId(insertPayment.getId());
			paymentResponse.setPayId(insertPayment.getPayId());
			paymentResponse.setReference(insertPayment.getReference());
			paymentResponse.setAmount(insertPayment.getAmount());
			paymentResponse.setCurrency(insertPayment.getCurrency());
			paymentResponse.setMethod(insertPayment.getMethod());
			paymentResponse.setPayer(new Payer());
			paymentResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			paymentResponse.setState(insertPayment.getState());

		} catch (IOException e) {
			e.printStackTrace();
			return ResponseData.failed(SystemError.PAYMENT_ERROR, null);
		}

		return ResponseData.ok(paymentResponse);
	}

	private PaymentResponse errorResponse(PaymentResponse paymentResponse, PaymentRequest paymentRequest, String message) {

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
		reason.setMessage(message);

		// response
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

		return paymentResponse;
	}
}
