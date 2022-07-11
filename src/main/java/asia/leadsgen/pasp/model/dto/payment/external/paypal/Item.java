package asia.leadsgen.pasp.model.dto.payment.external.paypal;

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
	@JsonProperty(value = AppParams.DESCRIPTION)
	private String description;
	@JsonProperty(value = AppParams.QUANTITY)
	private String quantity;
	@JsonProperty(value = AppParams.PRICE)
	private String price;
	@JsonProperty(value = AppParams.TAX)
	private String tax;
	@JsonProperty(value = AppParams.SKU)
	private String sku;
	@JsonProperty(value = AppParams.CURRENCY)
	private String currency;
}