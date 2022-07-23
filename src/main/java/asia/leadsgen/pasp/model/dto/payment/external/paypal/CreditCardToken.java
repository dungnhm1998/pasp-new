package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreditCardToken {
	@JsonProperty(value = AppParams.EXPIRE_MONTH)
	int expire_month;
	@JsonProperty(value = AppParams.EXPIRE_YEAR)
	int expire_year;
	@JsonProperty(value = AppParams.TYPE)
	String type;
	@JsonProperty(value = AppParams.PAYER_ID)
	String payer_id;
	@JsonProperty(value = AppParams.LAST4)
	String last4;
	@JsonProperty(value = AppParams.CREDIT_CARD_ID)
	String credit_card_id;
}
