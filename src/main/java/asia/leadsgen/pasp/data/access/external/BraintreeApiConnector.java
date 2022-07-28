package asia.leadsgen.pasp.data.access.external;

import asia.leadsgen.pasp.model.dto.external.braintree.BraintreeChargeResponse;
import asia.leadsgen.pasp.util.AppParams;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.stripe.exception.StripeException;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Log4j2
@Component
@Data
public class BraintreeApiConnector {

	@Value("${braintree.service.merchant_id}")
	private String merchantId;
	@Value("${braintree.service.public_key}")
	private String publicKey;
	@Value("${braintree.service.private_key}")
	private String privateKey;
	@Value("${braintree.service.environment}")
	private String environment;
	@Value("${braintree.service.tokenization}")
	private String tokenization;

	public BraintreeChargeResponse charge(String token, String amount, String currency) throws StripeException {
		BraintreeChargeResponse chargeResponse = new BraintreeChargeResponse();
		BraintreeGateway gateway;
		if (tokenization == null) {
			tokenization = "";
		}
		if (!tokenization.equalsIgnoreCase("")) {
			gateway = new BraintreeGateway(tokenization);
		} else {
			if (environment.contains(AppParams.SANDBOX)) {
				gateway = new BraintreeGateway(Environment.SANDBOX, merchantId, publicKey, privateKey);
			} else {
				gateway = new BraintreeGateway(Environment.PRODUCTION, merchantId, publicKey, privateKey);
			}
		}
		TransactionRequest request = new TransactionRequest()
				.amount(new BigDecimal(amount))
				.paymentMethodNonce(token)
				.options()
				.submitForSettlement(true)
				.done();

		Result<Transaction> result = gateway.transaction().sale(request);
		if (result.isSuccess()) {
			Transaction transaction = result.getTarget();
			if (transaction != null) {
				log.info("[BRAINTREE CHARGE RESPONSE] body = " + transaction);
				chargeResponse.setData(transaction);
			}
		}
		chargeResponse.setMessage(result.getMessage());

		return chargeResponse;
	}

	public BraintreeGateway getToken() {
		BraintreeGateway gateway = null;
		try {
			if (tokenization == null) {
				tokenization = "";
			}
			if (!tokenization.equalsIgnoreCase("")) {
				gateway = new BraintreeGateway(tokenization);
			} else {
				if (environment.contains(AppParams.SANDBOX)) {
					gateway = new BraintreeGateway(Environment.SANDBOX, merchantId, publicKey, privateKey);
				} else {
					gateway = new BraintreeGateway(Environment.PRODUCTION, merchantId, publicKey, privateKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gateway;
	}
}
