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
	public String shipping;
	@JsonProperty(value = AppParams.SUBTOTAL)
	public String subtotal;
	@JsonProperty(value = AppParams.SHIPPING_DISCOUNT)
	public String shippingDiscount;
	@JsonProperty(value = AppParams.INSURANCE)
	public String insurance;
	@JsonProperty(value = AppParams.HANDLING_FEE)
	public String handlingFee;
	@JsonProperty(value = AppParams.TAX)
	public String tax;
}