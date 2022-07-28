package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemList {

	@JsonProperty(value = AppParams.ITEMS)
	ArrayList<Item> items;
	@JsonProperty(value = AppParams.SHIPPING_ADDRESS)
	ShippingAddress shippingAddress;
}