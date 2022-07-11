package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item {
	@JsonProperty(value = AppParams.NAME)
	private String name;
	@JsonProperty(value = AppParams.QUANTITY)
	private String quantity;
	@JsonProperty(value = AppParams.UNIT_AMOUNT)
	private Money unitAmount;
	@JsonProperty(value = AppParams.CATEGORY)
	private String category;
	@JsonProperty(value = AppParams.DESCRIPTION)
	private String description;
	@JsonProperty(value = AppParams.TAX)
	private Money tax;
	@JsonProperty(value = AppParams.SKU)
	private String sku;
}