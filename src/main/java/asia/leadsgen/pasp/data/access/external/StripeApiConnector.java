package asia.leadsgen.pasp.data.access.external;

import asia.leadsgen.pasp.entity.PaymentAccount;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Balance;
import com.stripe.model.Charge;
import com.stripe.model.Refund;
import com.stripe.net.RequestOptions;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@Data
public class StripeApiConnector {

	@Value("${stripe.service.apikey}")
	String apiKey;
	@Value("${stripe.service.connected.account.id}")
	String connectedStripeAccountId;
	@Value("${stripe.service.account_name}")
	String stripeAccountName;

	public Charge charge(String token, long amount, String currency, String desc) throws StripeException {
		// Stripe.apiKey = "sk_test_XhM2l1VjXjmbujg6lWsYaqgN";
		Stripe.apiKey = apiKey;

		// Charge the user's card:
		Map<String, Object> params = new HashMap();
		params.put("amount", amount);
		params.put("currency", currency);
		params.put("description", desc);
		params.put("source", token);

		if (connectedStripeAccountId != null && !connectedStripeAccountId.trim().isEmpty()) {
			Map<String, Object> transferDataParams = new HashMap<String, Object>();
			transferDataParams.put("destination", connectedStripeAccountId);
			params.put("transfer_data", transferDataParams);
		}
		log.info("[STRIPE CHARGE REQUEST] " + params);
		Charge charge = Charge.create(params);
		log.info("[STRIPE CHARGE RESPONSE] body = " + charge);

		return charge;

	}

	public Charge createCharge(long amount, String currency, String customerId, String orderId, String code) throws StripeException {
		Stripe.apiKey = apiKey;
		Map<String, Object> customerParams = new HashMap<>();
		customerParams.put("amount", amount);
		customerParams.put("currency", currency);
		customerParams.put("customer", customerId);
		customerParams.put("description", orderId);
		if (StringUtils.isNotEmpty(code)) {
			customerParams.put("statement_descriptor_suffix", code);
		}

		Stripe.apiKey = apiKey;

		// Charge the user's card:
		Map<String, Object> params = new HashMap();
		params.put("amount", amount);
		params.put("currency", currency);
		params.put("customer", customerId);
		params.put("description", orderId);
		log.info("[STRIPE CHARGE REQUEST] " + params);
		Charge charge = Charge.create(customerParams);
		log.info("[STRIPE CHARGE RESPONSE] body = " + charge);
		return charge;
	}

	public Balance getAccountBalance(PaymentAccount account) throws StripeException {
		log.info("api key = " + account.getStripeApiKey());
		if (StringUtils.isNotEmpty(account.getStripeApiKey())) {
			Stripe.apiKey = account.getStripeApiKey();
		} else {
			Stripe.apiKey = apiKey;
		}
		Balance balance = Balance.retrieve(RequestOptions.getDefault());
		log.info("[STRIPE BALANCE RESPONSE] body = " + balance);

		return balance;
	}

	public Refund createRefund(PaymentAccount account, String chargeId, long amount) throws StripeException {
		log.info("api key = " + account.getStripeApiKey());
		if (StringUtils.isNotEmpty(account.getStripeApiKey())) {
			Stripe.apiKey = account.getStripeApiKey();
		} else {
			Stripe.apiKey = apiKey;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("charge", chargeId);
		if (amount > 0) {
			params.put("amount", amount);
		}

		Refund refund = Refund.create(params);
		log.info("[STRIPE REFUND RESPONSE] body = " + refund);

		return refund;
	}
}
