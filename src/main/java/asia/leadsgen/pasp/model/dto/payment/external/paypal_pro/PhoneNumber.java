package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PhoneNumber {

	@JsonProperty(value = AppParams.NATIONAL_NUMBER)
	Money nationalNumber;
}
