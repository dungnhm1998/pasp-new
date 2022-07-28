package asia.leadsgen.pasp.model.dto.payment.refund;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefundResponse {

	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.MESSAGE)
	String message;
	@JsonProperty(value = AppParams.STATUS)
	String status;
}
