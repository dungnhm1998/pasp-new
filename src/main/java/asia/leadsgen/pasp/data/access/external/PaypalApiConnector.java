package asia.leadsgen.pasp.data.access.external;

import asia.leadsgen.pasp.model.dto.payment.PaymentRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.Amount;
import asia.leadsgen.pasp.model.dto.external.paypal.Details;
import asia.leadsgen.pasp.model.dto.external.paypal.Item;
import asia.leadsgen.pasp.model.dto.external.paypal.ItemList;
import asia.leadsgen.pasp.model.dto.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalAccessTokenResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreateInvoiceRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreateInvoiceResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreatePaymentExecuteUrlRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreatePaymentExecuteUrlResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreatePaymentUrlRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreatePaymentUrlResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalRefundSaleRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalRefundSaleResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalSendInvoiceRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalSendInvoiceResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.RedirectUrls;
import asia.leadsgen.pasp.model.dto.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.model.dto.external.paypal.Transaction;
import asia.leadsgen.pasp.model.dto.payment_execute.PaymentExecuteRequest;
import asia.leadsgen.pasp.model.dto.payppal.InvoicePaypalRequest;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.GetterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
@Data
public class PaypalApiConnector {

	@Value("${paypal.service.merchant_id}")
	String restApiMerchantId;
	@Value("${paypal.service.client_id}")
	String restAPIClientId;
	@Value("${paypal.service.client_secret}")
	String restAPIClientSecret;
	@Value("${paypal.service.sbn_code}")
	String restBNCode;
	@Value("${paypal.service.ssl_version_to_use}")
	String sslVersionToUse;
	@Value("${paypal.service.account_name}")
	String paypalAccountName;

	public String getBase64encodedCredentials() {
		String userpass = restAPIClientId + ":" + restAPIClientSecret;
		String base64encodedUserPass = new String(Base64.encodeBase64(userpass.getBytes()));
		return AppConstants.BASIC_ + base64encodedUserPass;
	}

	public String getBase64encodedCredentials(String clientID, String clientSecret) {
		String userpass = clientID + ":" + clientSecret;
		String base64encodedUserPass = new String(Base64.encodeBase64(userpass.getBytes()));
		return AppConstants.BASIC_ + base64encodedUserPass;
	}

	public PaypalAccessTokenResponse createAccessToken() throws IOException {
		RequestBody formBody = new FormBody.Builder()
				.add(AppConstants.GRANT_TYPE, AppConstants.CLIENT_CREDENTIALS)
				.build();

		String tokenUri = "/v1/oauth2/token";

		Request request = new Request.Builder()
				.url(tokenUri)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.AUTHORIZATION, getBase64encodedCredentials())
				.addHeader(AppConstants.PAYPAL_PARTNER_ATTRIBUTION_ID, restBNCode)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = null;
		response = client.newCall(request).execute();

		ObjectMapper mapper = new ObjectMapper();
		PaypalAccessTokenResponse responseBody = mapper.readValue(response.body().string(), PaypalAccessTokenResponse.class);
		log.info("PaypalAccessTokenResponse result========:" + responseBody);
		return responseBody;
	}

	public PaypalAccessTokenResponse createAccessToken(String auth, String bnCode) throws IOException {
		RequestBody formBody = new FormBody.Builder()
				.add(AppConstants.GRANT_TYPE, AppConstants.CLIENT_CREDENTIALS)
				.build();

		String tokenUri = "/v1/oauth2/token";

		Request request = new Request.Builder()
				.url(tokenUri)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.AUTHORIZATION, auth)
				.addHeader(AppConstants.PAYPAL_PARTNER_ATTRIBUTION_ID, bnCode)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = null;
		response = client.newCall(request).execute();

		ObjectMapper mapper = new ObjectMapper();
		PaypalAccessTokenResponse responseBody = mapper.readValue(response.body().string(), PaypalAccessTokenResponse.class);
		log.info("PaypalAccessTokenResponse result========:" + responseBody);
		return responseBody;
	}

	public PaypalCreatePaymentUrlRequest createPaymentUrlRequest(PaymentRequest paymentRequest) {

		PaypalCreatePaymentUrlRequest paypalCreatePaymentUrlRequest = new PaypalCreatePaymentUrlRequest();

		paypalCreatePaymentUrlRequest.setIntent("sale");

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		paypalCreatePaymentUrlRequest.setPayer(payer);

		RedirectUrls redirectUrl = new RedirectUrls();
		String domainUrl = "https://" + paymentRequest.getDomain() + "/shop/checkout";

		redirectUrl.setCancel_url(domainUrl.replaceAll("\"", ""));
		redirectUrl.setReturn_url(domainUrl.replaceAll("\"", ""));

		paypalCreatePaymentUrlRequest.setRedirect_urls(redirectUrl);

		Transaction transaction = new Transaction();
		Amount amount1 = new Amount();
		amount1.setCurrency(paymentRequest.getCurrency());
		amount1.setTotal(paymentRequest.getAmount());
		Details amountdetail = new Details();
		amountdetail.setShipping("0.0");
		amountdetail.setTax("0.0");
		amountdetail.setSubtotal("0.0");
		List<PaymentRequest.ItemRequest> items = paymentRequest.getItems();
		ArrayList<Item> listItemsCreatePaymentUrl = new ArrayList<>();
		double subTotalShip;
		double tax;
		double totalDiscount = 0d, subTotal = 0d;
		double totalShip = 0d, total_tax = 0d;
		NumberFormat formatter = new DecimalFormat("#0.00");

		for (PaymentRequest.ItemRequest item : items) {
			Item itemCreatePaymentUrl = new Item();
			itemCreatePaymentUrl.setCurrency(item.getCurrency());
			itemCreatePaymentUrl.setName(item.getVariantName() + " - " + item.getSizeName());
			itemCreatePaymentUrl.setQuantity(String.valueOf(item.getQuantity()));
			itemCreatePaymentUrl.setPrice(item.getPrice());
			itemCreatePaymentUrl.setSku("1");
			subTotalShip = GetterUtil.getDouble(item.getShippingFee()) + GetterUtil.getDouble(amountdetail.getShipping());
			totalShip += subTotalShip;

			subTotal += GetterUtil.getDouble(itemCreatePaymentUrl.getPrice()) * GetterUtil.getInteger(item.getQuantity());

			itemCreatePaymentUrl.setTax(item.getTaxAmount());
			listItemsCreatePaymentUrl.add(itemCreatePaymentUrl);

			total_tax += GetterUtil.getDouble(item.getTaxAmount(), 0d);
		}


		amountdetail.setTax(formatter.format(total_tax));
		amountdetail.setShipping(formatter.format(totalShip));

		totalDiscount = GetterUtil.getDouble(paymentRequest.getDiscount(), 0d);
		if (totalDiscount > 0) {
			subTotal = subTotal - totalDiscount;
			totalDiscount = -totalDiscount;
			Item itemCreatePaymentUrl = new Item();
			itemCreatePaymentUrl.setCurrency(paymentRequest.getCurrency());
			itemCreatePaymentUrl.setName("discount");
			itemCreatePaymentUrl.setQuantity("1");
			itemCreatePaymentUrl.setPrice(formatter.format(totalDiscount));
			listItemsCreatePaymentUrl.add(itemCreatePaymentUrl);
		}
		amountdetail.setSubtotal(formatter.format(subTotal));
		amount1.setDetails(amountdetail);


		transaction.setAmount(amount1);
		transaction.setDescription("Pay on " + paymentRequest.getDomain());
		transaction.setInvoiceNumber(paymentRequest.getReference());
		ItemList itemList = new ItemList();
		itemList.setItems(listItemsCreatePaymentUrl);

		String typeExpress = paymentRequest.getType();
		if (StringUtils.isEmpty(typeExpress) || !typeExpress.equalsIgnoreCase(AppParams.PAYPAL_EXPRESS)) {
			ShippingAddress shipping = paymentRequest.getShippingAddress().convertTpPaypal();

			itemList.setShippingAddress(shipping);
			transaction.setItemList(itemList);
		}

		ArrayList<Transaction> listTransaction = new ArrayList<>();
		listTransaction.add(transaction);
		paypalCreatePaymentUrlRequest.setTransactions(listTransaction);

		return paypalCreatePaymentUrlRequest;
	}

	public PaypalCreatePaymentUrlResponse createPaymentUrl(String accessToken, PaypalCreatePaymentUrlRequest paymentUrlRequest) throws IOException {
		PaypalCreatePaymentUrlResponse responseBody = new PaypalCreatePaymentUrlResponse();
		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, paymentUrlRequest.toJson()); // new
		String approvalRequestURI = "/v1/payments/payment";

		Request request = new Request.Builder()
				.url(approvalRequestURI)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BEARER_ + accessToken)
				.addHeader(AppConstants.PAYPAL_PARTNER_ATTRIBUTION_ID, restBNCode)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL APPROVAL URL RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL APPROVAL URL RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalCreatePaymentUrlResponse.class);
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());

		return responseBody;
	}

	public PaypalCreatePaymentExecuteUrlRequest createPaymentExecuteUrlRequest(PaymentExecuteRequest paymentExeRq) {

		PaypalCreatePaymentExecuteUrlRequest request = new PaypalCreatePaymentExecuteUrlRequest();
		request.setPayerId(paymentExeRq.getPayerId());
		return request;
	}


	public PaypalCreatePaymentExecuteUrlResponse createPaymentExecuteUrl(String accessToken, PaypalCreatePaymentExecuteUrlRequest executeUrlRequest, PaymentExecuteRequest paymentExeRq) throws IOException {
		PaypalCreatePaymentExecuteUrlResponse responseBody = new PaypalCreatePaymentExecuteUrlResponse();
		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, executeUrlRequest.toJson()); // new
		String approvalRequestURI = "/v1/payments/payment" + paymentExeRq.getPayId() + "/execute";

		Request request = new Request.Builder()
				.url(approvalRequestURI)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BEARER_ + accessToken)
				.addHeader(AppConstants.PAYPAL_PARTNER_ATTRIBUTION_ID, restBNCode)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL APPROVAL URL RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL APPROVAL URL RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalCreatePaymentExecuteUrlResponse.class);
			return responseBody;
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());

		return responseBody;
	}

	public PaypalRefundSaleResponse refundSale(String accessToken, String saleId, PaypalRefundSaleRequest refundInfo, String bnCode) throws IOException {
		PaypalRefundSaleResponse responseBody = new PaypalRefundSaleResponse();
		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, refundInfo.toJson()); // new
		String approvalRequestURI = "/v1/payments/sale/" + saleId + "/refund";

		Request request = new Request.Builder()
				.url(approvalRequestURI)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_FORM_URLENCODED)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BEARER_ + accessToken)
				.addHeader(AppConstants.PAYPAL_PARTNER_ATTRIBUTION_ID, bnCode)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL REFUND SALE RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL REFUND SALE RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalRefundSaleResponse.class);
			return responseBody;
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());

		return responseBody;
	}

	public PaypalCreateInvoiceRequest createInvoiceRequest(InvoicePaypalRequest invoicePaypalRequest) {
		return invoicePaypalRequest.getInvoiceInfo();
	}

	public PaypalCreateInvoiceResponse createInvoice(String accessToken, PaypalCreateInvoiceRequest paypalCreateInvoiceRequest) throws IOException {
		PaypalCreateInvoiceResponse responseBody = new PaypalCreateInvoiceResponse();
		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, paypalCreateInvoiceRequest.toJson()); // new
		String createInvoiceUrl = "/v2/invoicing/invoices";

		Request request = new Request.Builder()
				.url(createInvoiceUrl)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BEARER_ + accessToken)
				.addHeader(AppConstants.PREFER, AppConstants.RETURN_REPRESENTATION)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL CREATE INVOICE RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL CREATE INVOICE RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalCreateInvoiceResponse.class);
			return responseBody;
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());

		return responseBody;
	}

	public PaypalSendInvoiceRequest createSendInvoiceRequest(InvoicePaypalRequest invoicePaypalRequest) {
		return new PaypalSendInvoiceRequest();
	}

	public PaypalSendInvoiceResponse sendInvoice(String accessToken, String invoiceId, PaypalSendInvoiceRequest paypalSendInvoiceRequest) throws IOException {
		PaypalSendInvoiceResponse responseBody = new PaypalSendInvoiceResponse();
		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, paypalSendInvoiceRequest.toJson()); // new
		String createInvoiceUrl = "/v2/invoicing/invoices" + invoiceId + "/send";

		Request request = new Request.Builder()
				.url(createInvoiceUrl)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.BEARER_ + accessToken)
				.addHeader(AppConstants.PREFER, AppConstants.RETURN_REPRESENTATION)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[PAYPAL SEND INVOICE RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[PAYPAL SEND INVOICE RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), PaypalSendInvoiceResponse.class);
			return responseBody;
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());

		return responseBody;
	}
}
