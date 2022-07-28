package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDetail {
	@JsonProperty(value = AppParams.ADDRESS)
	ShippingAddress address;
	@JsonProperty(value = AppParams.NAME)
	NameDetail name;
	@JsonProperty(value = AppParams.TYPE)
	String type;

	//stripe
	@JsonProperty(value = AppParams.PHONE)
	String phone;
	@JsonProperty(value = AppParams.EMAIL)
	String email;
}
