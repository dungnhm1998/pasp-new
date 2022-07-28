package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
	@JsonProperty(value = AppParams.ALLOW_TIP)
	boolean allowTip;
	@JsonProperty(value = AppParams.PARTIAL_PAYMENT)
	PartialPayment partialPayment;
	@JsonProperty(value = AppParams.TAX_CALCULATED_AFTER_DISCOUNT)
	boolean tax_calculated_after_discount;
	@JsonProperty(value = AppParams.TAX_INCLUSIVE)
	boolean tax_inclusive;
	@JsonProperty(value = AppParams.TEMPLATE_ID)
	String template_id;
}