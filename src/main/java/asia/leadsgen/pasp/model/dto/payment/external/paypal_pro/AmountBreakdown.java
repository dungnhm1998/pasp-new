package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmountBreakdown {
	@JsonProperty(value = AppParams.DISCOUNT)
	Money discount;
	@JsonProperty(value = AppParams.HANDLING)
	Money handingFee;
	@JsonProperty(value = AppParams.INSURANCE)
	Money insurance;
	@JsonProperty(value = AppParams.ITEM_TOTAL)
	Money itemTotal;
	@JsonProperty(value = AppParams.SHIPPING)
	Money shippingFee;
	@JsonProperty(value = AppParams.SHIPPING_DISCOUNT)
	Money shippingDiscount;
	@JsonProperty(value = AppParams.TAX_TOTAL)
	Money tax;
}
