package asia.leadsgen.pasp.model.dto.stripe;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChargeStripeRequest implements JsonInterface {

	@JsonProperty(value = AppParams.CUSTOMER_ID)
	String customerId;
	@JsonProperty(value = AppParams.AMOUNT)
	double amount;
	@JsonProperty(value = AppParams.CURRENCY)
	String currency;
	@JsonProperty(value = AppParams.REFERENCE)
	String reference;
	@JsonProperty(value = AppParams.ORDER_ID)
	String orderId;
	@JsonProperty(value = AppParams.CODE)
	String code;

}
