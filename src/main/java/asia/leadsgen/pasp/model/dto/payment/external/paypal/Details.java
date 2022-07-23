package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Details {
	@JsonProperty(value = AppParams.SHIPPING)
	String shipping;
	@JsonProperty(value = AppParams.SUBTOTAL)
	String subtotal;
	@JsonProperty(value = AppParams.SHIPPING_DISCOUNT)
	String shippingDiscount;
	@JsonProperty(value = AppParams.INSURANCE)
	String insurance;
	@JsonProperty(value = AppParams.HANDLING_FEE)
	String handlingFee;
	@JsonProperty(value = AppParams.TAX)
	String tax;
}