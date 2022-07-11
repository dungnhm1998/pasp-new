package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NameDetail {
	@JsonProperty(value = AppParams.NAME)
	Money name;
	@JsonProperty(value = AppParams.FULL_NAME)
	Money fullName;
	@JsonProperty(value = AppParams.SURNAME)
	Money surName;
	@JsonProperty(value = AppParams.PREFIX)
	Money prefix;
	@JsonProperty(value = AppParams.SUFFIX)
	Money suffix;
	@JsonProperty(value = AppParams.MIDDLE_NAME)
	Money middleName;
	@JsonProperty(value = AppParams.GIVEN_NAME)
	Money givenName;
}
