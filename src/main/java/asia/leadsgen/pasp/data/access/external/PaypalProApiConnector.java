package asia.leadsgen.pasp.data.access.external;

import asia.leadsgen.pasp.model.dto.payment.PaymentRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.PaypalAccessTokenResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.PaypalCreatePaymentUrlRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.PaypalCreatePaymentUrlResponse;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Item;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Money;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.PaypalProCreateOrderRequest;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Shipping;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.GetterUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
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
import java.util.List;

@Log4j2
@Component
public class PaypalProApiConnector {

	@Value("${paypal.pro.client.id}")
	private static String clientId;
	@Value("${paypal.pro.client.secret}")
	private static String clientSecret;
	@Value("${paypal.pro.access.token.endpoint}")
	private static String accessTokenEndpoint;
	@Value("${paypal.pro.generate.client.token.endpoint}")
	private static String clientTokenEndpoint;
	@Value("${paypal.pro.order.capture.endpoint}")
	private static String orderCaptureEndpoint;

	public PaypalAccessTokenResponse createAccessToken() throws IOException {

		RequestBody formBody = new FormBody.Builder()
				.add("grant_type", "client_credentials")
				.add("response_type", "token")
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
				.addHeader(AppConstants.AUTHORIZATION, "Basic " + basicAuth)
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
		PaypalProCreateOrderRequest paypalProOrder = new PaypalProCreateOrderRequest();

		//set payer
		Shipping billing = ObjectUtils.isEmpty(paymentRequest.getBilling()) ? paymentRequest.getBilling():paymentRequest.getShipping();
		if (ObjectUtils.isEmpty(billing)) {
			ObjectMapper mapper = new ObjectMapper();
			ShippingAddress shippingAddress = mapper.readValue(billing.getAddress().toString(), ShippingAddress.class);

			Payer payer = new Payer();
			payer.setAddress(shippingAddress);
			payer.setEmailAddress(billing.getEmailAddress());
			paypalProOrder.setPayer(payer);
		}

		//set items
		List<Item> purcharseUnitItemList = new ArrayList<>();

		List<PaymentRequest.ItemRequest> items = paymentRequest.getItems();
		int skuCount = 0;
		double shippingAmount = 0d;
		double breakdownItemsTotal = 0d;
		double breakdownTaxTotal = 0d;

		for (PaymentRequest.ItemRequest item : items) {
			log.info("order item =" + item.toString());
			String currencyCode = item.getCurrency();
			Item purchaseUnitItem = new Item();
			purchaseUnitItem.setName(item.getVariantName() + "-" + item.getSizeName());
			purchaseUnitItem.setSku("SKU" + "-" + skuCount++);


			int itemQuantity = GetterUtil.getInteger(item.getQuantity());
			double itemPrice = GetterUtil.getDouble(item.getPrice());
			double itemShipping = GetterUtil.getDouble(item.getShippingFee());

			shippingAmount += itemShipping;
			Money unitAmount = new Money();
			unitAmount.setCurrency(currencyCode);
			unitAmount.setValue(String.format("%.2f", itemPrice));
			breakdownItemsTotal += (itemPrice * itemQuantity);

			Money tax = new Money();
			tax.setCurrency(currencyCode);
			double taxAmount = GetterUtil.getDouble(item.getTaxAmount());
			tax.setValue(item.getTaxAmount());
			breakdownTaxTotal += taxAmount;

			purchaseUnitItem.setUnitAmount(unitAmount);
			purchaseUnitItem.setTax(tax);
			purchaseUnitItem.setQuantity(String.valueOf(itemQuantity));

			purcharseUnitItemList.add(purchaseUnitItem);
		}
		//return
		return paypalProOrder;
	}

	public PaypalCreatePaymentUrlResponse createOrder(String accessToken, PaypalProCreateOrderRequest paymentUrlRequest) throws IOException {
		return null;
	}
}
