package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CreditCard {
	@JsonProperty(value = AppParams.EXPIRE_MONTH)
	int expire_month;
	@JsonProperty(value = AppParams.EXPIRE_YEAR)
	int expire_year;
	@JsonProperty(value = AppParams.NUMBER)
	String number;
	@JsonProperty(value = AppParams.TYPE)
	String type;
	@JsonProperty(value = AppParams.BILLING_ADDRESS)
	ShippingAddress billing_address;
	@JsonProperty(value = AppParams.CVV2)
	String cvv2;
	@JsonProperty(value = AppParams.FIRST_NAME)
	String first_name;
	@JsonProperty(value = AppParams.LAST_NAME)
	String last_name;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
}
