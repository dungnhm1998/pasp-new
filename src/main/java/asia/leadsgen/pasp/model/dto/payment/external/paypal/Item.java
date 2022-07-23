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
	String name;
	@JsonProperty(value = AppParams.DESCRIPTION)
	String description;
	@JsonProperty(value = AppParams.QUANTITY)
	String quantity;
	@JsonProperty(value = AppParams.PRICE)
	String price;
	@JsonProperty(value = AppParams.TAX)
	String tax;
	@JsonProperty(value = AppParams.SKU)
	String sku;
	@JsonProperty(value = AppParams.CURRENCY)
	String currency;
}