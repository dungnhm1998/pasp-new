package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.Money;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalRefdundDetail {
	@JsonProperty(value = AppParams.METHOD)
	String method;
	@JsonProperty(value = AppParams.AMOUNT)
	Money amount;
	@JsonProperty(value = AppParams.REFUND_DATE)
	String refundDate;
	@JsonProperty(value = AppParams.REFUND_ID)
	String refundId;
	@JsonProperty(value = AppParams.TYPE)
	String type;
}