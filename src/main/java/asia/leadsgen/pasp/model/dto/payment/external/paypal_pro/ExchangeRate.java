package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExchangeRate {
	@JsonProperty(value = AppParams.SOURCE_CURRENCY)
	String cvvCode;
	@JsonProperty(value = AppParams.TARGET_CURRENCY)
	String target_currency;
	@JsonProperty(value = AppParams.VALUE)
	String value;
}
