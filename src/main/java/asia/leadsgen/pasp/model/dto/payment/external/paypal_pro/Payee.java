package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Payee {
	@JsonProperty(value = AppParams.EMAIL_ADDRESS)
	Money emailAddress;
	@JsonProperty(value = AppParams.MERCHANT_ID)
	Money merchantId;
}
