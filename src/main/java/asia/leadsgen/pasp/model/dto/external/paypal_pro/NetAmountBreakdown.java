package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetAmountBreakdown {
	@JsonProperty(value = AppParams.CONVERTED_AMOUNT)
	Money converted_amount;
	@JsonProperty(value = AppParams.PAYABLE_AMOUNT)
	Money payable_amount;
	@JsonProperty(value = AppParams.EXCHANGE_RATE)
	ExchangeRate exchangeRate;
}
