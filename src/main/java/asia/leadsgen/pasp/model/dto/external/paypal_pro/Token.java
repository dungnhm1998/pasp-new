package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Token {
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.TYPE)
	String type;
}
