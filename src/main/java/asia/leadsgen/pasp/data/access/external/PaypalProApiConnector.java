package asia.leadsgen.pasp.data.access.external;

import asia.leadsgen.pasp.model.dto.payment.PaymentRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.PaypalAccessTokenResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.PaypalRefundSaleRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Amount;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.AmountBreakdown;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Item;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Money;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.NameDetail;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalAppContext;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalProAuth2TokenResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalProCreateOrderRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalProCreateOrderResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalProRefundRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalProRefundResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PurchaseUnit;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Shipping;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.ShippingDetail;
import asia.leadsgen.pasp.model.dto.payment.refund.PaymentRefundRequest;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.GetterUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@Data
public class PaypalProApiConnector {

	@Value("${paypal.pro.client.id}")
	String clientId;
	@Value("${paypal.pro.client.secret}")
	String clientSecret;
	@Value("${paypal.pro.access.token.endpoint}")
	String accessTokenEndpoint;
	@Value("${paypal.pro.generate.client.token.endpoint}")
	String clientTokenEndpoint;
	@Value("${paypal.pro.order.capture.endpoint}")
	String orderCaptureEndpoint;
	@Value("${paypal.pro.service.account_name}")
	String paypalProAccountName;

	public PaypalAccessTokenResponse createAccessToken() throws IOException {

		RequestBody formBody = new FormBody.Builder()
				.add(AppConstants.GRANT_TYPE, AppConstants.CLIENT_CREDENTIALS)
				.add(AppConstants.RESPONSE_TYPE, AppConstants.TOKEN)
				.build();
		String basicAuth = java.util.Base64.getEncoder().encodeToString(String.format("%s:%s", clientId, clientSecret).getBytes());
		String tokenUri = accessTokenEndpoint;
		log.info("clientId=" + clientId + ", clientSecret=" + clientSecret);

		Request request = new Request.Builder()
				.url(tokenUri)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.ACCEPT, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.ACCEPT_LANGUAGE, AppConstants.EN_US)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BASIC_ + basicAuth)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		ObjectMapper mapper = new ObjectMapper();
		PaypalAccessTokenResponse accessTokenResponse = mapper.readValue(response.body().string(), PaypalAccessTokenResponse.class);
		log.info("PaypalAccessTokenResponse result========:" + accessTokenResponse);
		log.info("Generated paypal_pro access token = " + accessTokenResponse.getAccessToken());

		return accessTokenResponse;
	}


	public PaypalAccessTokenResponse createAccessToken(String xClientId, String xClientSecret) throws IOException {

		RequestBody formBody = new FormBody.Builder()
				.add(AppConstants.GRANT_TYPE, AppConstants.CLIENT_CREDENTIALS)
				.add(AppConstants.RESPONSE_TYPE, AppConstants.TOKEN)
				.build();
		String basicAuth = java.util.Base64.getEncoder().encodeToString(String.format("%s:%s", xClientId, xClientSecret).getBytes());
		String tokenUri = accessTokenEndpoint;
		log.info("clientId=" + xClientId + ", clientSecret=" + xClientSecret);

		Request request = new Request.Builder()
				.url(tokenUri)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.ACCEPT, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.ACCEPT_LANGUAGE, AppConstants.EN_US)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BASIC_ + basicAuth)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		ObjectMapper mapper = new ObjectMapper();
		PaypalAccessTokenResponse accessTokenResponse = mapper.readValue(response.body().string(), PaypalAccessTokenResponse.class);
		log.info("PaypalAccessTokenResponse result========:" + accessTokenResponse);
		log.info("Generated paypal_pro access token = " + accessTokenResponse.getAccessToken());

		return accessTokenResponse;
	}

	public PaypalProCreateOrderRequest createOrderRequest(PaymentRequest paymentRequest) throws JsonProcessingException {
		//set payer
		Payer payer = new Payer();
		Shipping billing = ObjectUtils.isEmpty(paymentRequest.getBilling()) ? paymentRequest.getBilling():paymentRequest.getShipping();
		if (ObjectUtils.isEmpty(billing)) {
			ObjectMapper mapper = new ObjectMapper();
			ShippingAddress shippingAddress = mapper.readValue(billing.getAddress().toString(), ShippingAddress.class);
			payer.setAddress(shippingAddress);
			payer.setEmailAddress(billing.getEmailAddress());
		}

		//set items
		List<Item> purcharseUnitItemList = new ArrayList<>();

		int skuCount = 0;
		double shippingAmount = 0d;
		double breakdownItemsTotal = 0d;
		double breakdownTaxTotal = 0d;
		String currencyCode = "";

		List<PaymentRequest.ItemRequest> items = paymentRequest.getItems();
		for (PaymentRequest.ItemRequest item : items) {
			log.info("order item =" + item.toString());
			currencyCode = currencyCode.isEmpty() ? item.getCurrency():currencyCode;

			int itemQuantity = GetterUtil.getInteger(item.getQuantity());
			double itemPrice = GetterUtil.getDouble(item.getPrice());
			double itemShipping = GetterUtil.getDouble(item.getShippingFee());

			shippingAmount += itemShipping;
			Money unitAmount = new Money();
			unitAmount.setCurrency(currencyCode);
			unitAmount.setValue(String.format("%.2f", itemPrice));

			breakdownItemsTotal += (itemPrice * itemQuantity);
			breakdownTaxTotal += GetterUtil.getDouble(item.getTaxAmount());

			Money tax = new Money();
			tax.setCurrency(currencyCode);
			tax.setValue(item.getTaxAmount());

			Item purchaseUnitItem = new Item();
			purchaseUnitItem.setName(item.getVariantName() + "-" + item.getSizeName());
			purchaseUnitItem.setSku("SKU" + "-" + skuCount++);
			purchaseUnitItem.setUnitAmount(unitAmount);
			purchaseUnitItem.setTax(tax);
			purchaseUnitItem.setQuantity(String.valueOf(itemQuantity));

			purcharseUnitItemList.add(purchaseUnitItem);
		}

		//set breakdown
		AmountBreakdown breakdown = new AmountBreakdown();

		if (shippingAmount > 0) {
			Money breakdownShippingFee = new Money();
			breakdownShippingFee.setCurrency(currencyCode);
			breakdownShippingFee.setValue(String.format("%.2f", shippingAmount));
			breakdown.setShippingFee(breakdownShippingFee);
		}

		double discountAmount = GetterUtil.getDouble(paymentRequest.getDiscount());
		if (discountAmount > 0) {
			Money breakdownDiscount = new Money();
			breakdownDiscount.setCurrency(currencyCode);
			breakdownDiscount.setValue(String.format("%.2f", discountAmount));
			breakdown.setDiscount(breakdownDiscount);
		}

		//set amount
		Money breakdownItem = new Money();
		breakdownItem.setCurrency(currencyCode);
		breakdownItem.setValue(String.format("%.2f", breakdownItemsTotal));
		breakdown.setItemTotal(breakdownItem);

		Money breakdownTax = new Money();
		breakdownTax.setCurrency(currencyCode);
		breakdownTax.setValue(String.format("%.2f", breakdownTaxTotal));
		breakdown.setTax(breakdownTax);

		Amount unitAmount = new Amount();
		unitAmount.setCurrency(currencyCode);
		unitAmount.setValue(String.format("%.2f", breakdownItemsTotal + breakdownTaxTotal + shippingAmount - discountAmount));
		unitAmount.setBreakDown(breakdown);

		//set shipping
		ShippingDetail shippingDetail = new ShippingDetail();
		Shipping shipping = paymentRequest.getShipping();
		ShippingAddress shippingAddress = shipping.getAddress();
		shippingDetail.setAddress(shippingAddress);
		NameDetail name = new NameDetail();
		name.setFullName(shipping.getName());
		shippingDetail.setName(name);

		//set list purchase unit
		PurchaseUnit unit = new PurchaseUnit();
		unit.setInvoiceId(paymentRequest.getOrderId());
		unit.setAmount(unitAmount);
		unit.setItems(purcharseUnitItemList);
		unit.setShippingDetail(shippingDetail);

		List<PurchaseUnit> purchaseUnits = new ArrayList<>();
		purchaseUnits.add(unit);

		//set app context
		String domain = paymentRequest.getDomain();
		PaypalAppContext applicationContext = new PaypalAppContext();
		applicationContext.setBrandName(domain);
		applicationContext.setShippingPreference("SET_PROVIDED_ADDRESS");

		//return
		PaypalProCreateOrderRequest paypalProOrder = new PaypalProCreateOrderRequest();
		paypalProOrder.setIntent("CAPTURE");
		paypalProOrder.setPaypalAppContext(applicationContext);
		paypalProOrder.setPayer(payer);
		paypalProOrder.setPurchaseUnits(purchaseUnits);

		return paypalProOrder;
	}

	public PaypalProCreateOrderResponse createOrder(String accessToken, PaypalProCreateOrderRequest paymentCreateOrderRequest) throws IOException {
		PaypalProCreateOrderResponse responseBody = new PaypalProCreateOrderResponse();
		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, paymentCreateOrderRequest.toJson());
		String createOrderURI = "/v2/checkout/orders";

		Request request = new Request.Builder()
				.url(createOrderURI)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BEARER_ + accessToken)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL PRO CREATE ORDER RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL PRO CREATE ORDER RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalProCreateOrderResponse.class);
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());
		return responseBody;
	}


	public PaypalProCreateOrderResponse createOrderCapture(String payId, String accessToken) throws IOException {
		PaypalProCreateOrderResponse responseBody = new PaypalProCreateOrderResponse();
		RequestBody formBody = new FormBody.Builder()
				.build();
		String captureURL = String.format(orderCaptureEndpoint, payId);
		log.info("clientId=" + clientId + ", clientSecret=" + clientSecret);

		Request request = new Request.Builder()
				.url(captureURL)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BASIC_ + accessToken)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL PRO ORDER CAPTURE RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL PRO ORDER CAPTURE RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalProCreateOrderResponse.class);
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());

		return responseBody;
	}


	public PaypalProAuth2TokenResponse generateClientToken(String accessToken) throws IOException {
		PaypalProAuth2TokenResponse auth2Token = new PaypalProAuth2TokenResponse();
		Request request = new Request.Builder()
				.url(clientTokenEndpoint)
//				.post()
				.addHeader(AppConstants.ACCEPT, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.ACCEPT_LANGUAGE, AppConstants.EN_US)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BEARER_ + accessToken)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL PRO AUTH2 TOKEN RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL PRO AUTH2 TOKEN RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			auth2Token = mapper.readValue(response.body().string(), PaypalProAuth2TokenResponse.class);
		}

		auth2Token.setResponseCode(response.code());
		auth2Token.setMessage(response.message());

		return auth2Token;
	}

	public PaypalProRefundRequest createRefundRequest(PaymentRefundRequest requestbody) {
		PaypalProRefundRequest request = new PaypalProRefundRequest();
		Amount amount = new Amount();
		amount.setValue(requestbody.getRefundInfo().getAmount().getTotal());
		amount.setCurrency(requestbody.getRefundInfo().getAmount().getCurrency());
		request.setAmount(amount);
		return request;
	}

	public PaypalProRefundResponse refundSale(String accessToken, String captureId, PaypalProRefundRequest refundInfo) throws IOException {
		PaypalProRefundResponse responseBody = new PaypalProRefundResponse();
		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, refundInfo.toJson());

		String captureURL = "/v2/payments/captures/" + captureId + "/refund";

		Request request = new Request.Builder()
				.url(captureURL)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BEARER_ + accessToken)
				.addHeader(AppConstants.PAYPAL_REQUEST_ID, UUID.randomUUID().toString())
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL PRO REFUND CAPTURE RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL PRO REFUND CAPTURE RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalProRefundResponse.class);
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());

		return responseBody;

	}

	public PaypalProRefundResponse getRefundDetail(String xClientId, String xClientSecret, String refundLink) throws IOException {
		PaypalProRefundResponse responseBody = new PaypalProRefundResponse();

		String basicAuth = Base64.getEncoder().encodeToString(String.format("%s:%s", xClientId, xClientSecret).getBytes());

		log.info("[PAYPAL PRO REFUND DETAIL URL] : " + refundLink);

		Request request = new Request.Builder()
				.url(refundLink)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BASIC_ + basicAuth)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL PRO REFUND DETAIL RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL PRO REFUND DETAIL RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalProRefundResponse.class);
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());

		return responseBody;
	}
}
