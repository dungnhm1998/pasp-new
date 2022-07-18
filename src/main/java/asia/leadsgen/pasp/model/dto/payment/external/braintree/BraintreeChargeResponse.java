package asia.leadsgen.pasp.model.dto.payment.external.braintree;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.braintreegateway.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BraintreeChargeResponse implements JsonInterface {

	@JsonProperty(value = AppParams.DATA)
	Transaction data;
	@JsonProperty(value = AppParams.MESSAGE)
	String message;
}
