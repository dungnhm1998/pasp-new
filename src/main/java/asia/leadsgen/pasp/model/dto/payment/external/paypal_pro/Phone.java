package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Phone {
	@JsonProperty(value = AppParams.PHONE_NUMBER)
	PhoneNumber phone_number;
	@JsonProperty(value = AppParams.PHONE_TYPE)
	Money phone_type;
}
