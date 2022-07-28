package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.NameDetail;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactInfo {
	@JsonProperty(value = AppParams.BUSINESS_NAME)
	String currency;
	@JsonProperty(value = AppParams.ADDRESS)
	ShippingAddress total;
	@JsonProperty(value = AppParams.NAME)
	NameDetail details;
}