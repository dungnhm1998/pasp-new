package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.payment.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShippingDetail {
	@JsonProperty(value = AppParams.ADDRESS)
	ShippingAddress address;
	@JsonProperty(value = AppParams.NAME)
	NameDetail name;
	@JsonProperty(value = AppParams.TYPE)
	Money type;
}
