package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.payment.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Shipping {
	@JsonProperty(value = AppParams.ADDRESS)
	public ShippingAddress address;
	@JsonProperty(value = AppParams.EMAIL_ADDRESS)
	public String emailAddress;
}
