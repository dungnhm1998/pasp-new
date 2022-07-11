package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StoredPaymentSource {
	@JsonProperty(value = AppParams.PAYMENT_INITIATOR)
	Money paymentInitiator;
	@JsonProperty(value = AppParams.PAYMENT_TYPE)
	Money paymentType;
	@JsonProperty(value = AppParams.PREVIOUS_NETWORK_TRANSACTION_REFERENCE)
	Money previousNetworkTransactionReference;
	@JsonProperty(value = AppParams.USAGE)
	Money usage;
}
