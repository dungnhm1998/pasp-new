package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentMethod {
	@JsonProperty(value = AppParams.PAYEE_PREFERRED)
	Money payeePreferred;
	@JsonProperty(value = AppParams.STANDARD_ENTRY_CLASS_CODE)
	Money standardEntryClassCode;
}
