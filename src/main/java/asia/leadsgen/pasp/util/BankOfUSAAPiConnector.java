package asia.leadsgen.pasp.util;

import asia.leadsgen.pasp.entity.PaymentAccount;
import asia.leadsgen.pasp.model.dto.external.BankOfUSA.BankUSAChargeRequest;
import asia.leadsgen.pasp.model.dto.external.BankOfUSA.BankUSAChargeResponse;
import asia.leadsgen.pasp.model.dto.external.BankOfUSA.BankUSARefundRequest;
import asia.leadsgen.pasp.model.dto.external.BankOfUSA.BankUSARefundResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Log4j2
@Component
@Data
public class BankOfUSAAPiConnector {

	@Value("${global.gateway.client.uri}")
	private String uri;
	@Value("${global.gateway.client.gatewayId}")
	private String gatewayId;
	@Value("${global.gateway.client.gatewayPassword}")
	private String gatewayPassword;
	@Value("${global.gateway.client.hmacKey}")
	private String hmacKey;
	@Value("${global.gateway.client.host}")
	private String host;

	public BankUSAChargeResponse purchase(BankUSAChargeRequest bankUSAChargeRequest) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		BankUSAChargeResponse responseBody = new BankUSAChargeResponse();
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss\'Z\'");
		String payeezyGatewayDate = now.format(formatter);
		String method = "POST";

		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, bankUSAChargeRequest.toJson()); // new
		String contentDigest = DigestUtils.sha1Hex(bankUSAChargeRequest.toJson());
		SecretKeySpec keySpec = new SecretKeySpec(hmacKey.getBytes(), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(keySpec);
		String hmac = Base64.getEncoder().encodeToString(mac.doFinal((method + "\n" + AppConstants.CONTENT_TYPE_APPLICATION_JSON + "\n" + contentDigest + "\n" + payeezyGatewayDate + "\n" + uri).getBytes()));

		Request request = new Request.Builder()
				.url(host + uri)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.ACCEPT, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.X_GGE4_DATE, payeezyGatewayDate)
				.addHeader(AppConstants.AUTHORIZATION, "GGE4_API " + gatewayId + ":" + hmac)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[BANK_OF_USA PURCHASE RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[BANK_OF_USA PURCHASE RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), BankUSAChargeResponse.class);
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());
		return responseBody;

	}

	public BankUSARefundResponse refund(BankUSARefundRequest boaRefundRequest, PaymentAccount account) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
		BankUSARefundResponse responseBody = new BankUSARefundResponse();
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\'T\'HH:mm:ss\'Z\'");
		String payeezyGatewayDate = now.format(formatter);
		String method = "POST";

		RequestBody formBody = RequestBody.create(AppConstants.MEDIA_TYPE_JSON, boaRefundRequest.toJson()); // new
		String contentDigest = DigestUtils.sha1Hex(boaRefundRequest.toJson());
		SecretKeySpec keySpec = new SecretKeySpec(
				account != null ? account.getBoaHmacKey().getBytes():hmacKey.getBytes(), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(keySpec);
		String hmac = Base64.getEncoder().encodeToString(mac.doFinal((method + "\n" + AppConstants.CONTENT_TYPE_APPLICATION_JSON + "\n" + contentDigest + "\n" + payeezyGatewayDate + "\n" + uri).getBytes()));

		Request request = new Request.Builder()
				.url(host + uri)
				.post(formBody)
				.addHeader(AppConstants.CONTENT_TYPE, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.ACCEPT, AppConstants.CONTENT_TYPE_APPLICATION_JSON)
				.addHeader(AppConstants.X_GGE4_CONTENT_SHA1, contentDigest)
				.addHeader(AppConstants.X_GGE4_DATE, payeezyGatewayDate)
				.addHeader(AppConstants.AUTHORIZATION, AppConstants.GGE4_API_ +  (account != null ? account.getBoaGwId() : gatewayId ) + ":" + hmac)
				.build();

		OkHttpClient client = new OkHttpClient();
		okhttp3.Response response = client.newCall(request).execute();

		log.info("[BANK_OF_USA REFUND RESPONSE] code = " + response.code() + ", message = " + response.message());
		log.info("[BANK_OF_USA REFUND RESPONSE] body : " + response.body());

		if (response.body() != null) {
			ObjectMapper mapper = new ObjectMapper();
			responseBody = mapper.readValue(response.body().string(), BankUSARefundResponse.class);
		}

		responseBody.setResponseCode(response.code());
		responseBody.setMessage(response.message());
		return responseBody;
	}
}
