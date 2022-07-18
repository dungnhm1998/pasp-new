package asia.leadsgen.pasp.data.access.external;

import asia.leadsgen.pasp.model.dto.payment.external.stripe.StripeChargeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class StripeApiConnector {

	@Value("${stripe.service.apikey}")
	private static String apiKey;
	@Value("${stripe.service.connected.account.id}")
	private static String connectedStripeAccountId;

	public Charge charge(String token, long amount, String currency, String desc) throws StripeException {
		// Stripe.apiKey = "sk_test_XhM2l1VjXjmbujg6lWsYaqgN";
		StripeChargeResponse chargeResponse = new StripeChargeResponse();
		Stripe.apiKey = apiKey;

		// Charge the user's card:
		Map<String, Object> params = new HashMap();
		params.put("amount", amount);
		params.put("currency", currency);
		params.put("description", desc);
		params.put("source", token);

		if (connectedStripeAccountId != null && connectedStripeAccountId.trim().isEmpty() == false) {
			Map<String, Object> transferDataParams = new HashMap<String, Object>();
			transferDataParams.put("destination", connectedStripeAccountId);
			params.put("transfer_data", transferDataParams);
		}
		log.info("[STRIPE CHARGE REQUEST] " + params);
		Charge charge = Charge.create(params);
		log.info("[STRIPE CHARGE RESPONSE] body = " + charge);

		if (charge != null) {
			chargeResponse.setData(charge);
		}

		return charge;

	}
}
