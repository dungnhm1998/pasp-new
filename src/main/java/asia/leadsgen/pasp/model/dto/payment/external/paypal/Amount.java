package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Amount {
	@JsonProperty(value = AppParams.CURRENCY)
	public String currency;
	@JsonProperty(value = AppParams.TOTAL)
	public String total;
	@JsonProperty(value = AppParams.DETAILS)
	public Details details;
}