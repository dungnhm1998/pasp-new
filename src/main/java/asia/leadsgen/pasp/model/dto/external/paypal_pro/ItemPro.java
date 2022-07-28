package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemPro {
	@JsonProperty(value = AppParams.NAME)
	String name;
	@JsonProperty(value = AppParams.QUANTITY)
	String quantity;
	@JsonProperty(value = AppParams.UNIT_AMOUNT)
	Money unitAmount;
	@JsonProperty(value = AppParams.CATEGORY)
	String category;
	@JsonProperty(value = AppParams.DESCRIPTION)
	String description;
	@JsonProperty(value = AppParams.TAX)
	Money tax;
	@JsonProperty(value = AppParams.SKU)
	String sku;
	@JsonProperty(value = AppParams.DISCOUNT)
	Money discount;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.ITEM_DATE)
	String itemDate;
	@JsonProperty(value = AppParams.UNIT_OF_MEASURE)
	String unitOfMeasure;
}