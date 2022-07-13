package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount {
	@JsonProperty(value = AppParams.CURRENCY_CODE)
	String currency;
	@JsonProperty(value = AppParams.VALUE)
	String value;
	@JsonProperty(value = AppParams.BREAKDOWN)
	AmountBreakdown breakDown;

}
