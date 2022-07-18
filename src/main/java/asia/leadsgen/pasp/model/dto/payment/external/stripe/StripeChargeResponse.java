package asia.leadsgen.pasp.model.dto.payment.external.stripe;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stripe.model.Charge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StripeChargeResponse {

	@JsonProperty(value = AppParams.DATA)
	Charge data;
	@JsonProperty(value = AppParams.MESSAGE)
	String message;
}
