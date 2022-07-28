package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlatformFee {
	@JsonProperty(value = AppParams.AMOUNT)
	Money amount;
	@JsonProperty(value = AppParams.PAYEE)
	Payee payee;
}
