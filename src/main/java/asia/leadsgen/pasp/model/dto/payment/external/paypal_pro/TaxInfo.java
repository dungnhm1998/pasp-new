package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TaxInfo {
	@JsonProperty(value = AppParams.TAX_ID)
	Money taxId;
	@JsonProperty(value = AppParams.TAX_ID_TYPE)
	PhoneNumber taxIdType;
}
