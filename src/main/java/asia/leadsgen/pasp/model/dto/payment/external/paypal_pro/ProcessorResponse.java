package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessorResponse {
	@JsonProperty(value = AppParams.AVS_CODE)
	String avsCode;
	@JsonProperty(value = AppParams.CVV_CODE)
	String cvvCode;
	@JsonProperty(value = AppParams.PAYMENT_ADVICE_CODE)
	String paymentAdviceCode;
	@JsonProperty(value = AppParams.RESPONSE_CODE)
	String responseCode;
	@JsonProperty(value = AppParams.VPAS)
	String vpas;
	@JsonProperty(value = AppParams.ECI_SUBMITTED)
	String eci_submitted;
	@JsonProperty(value = AppParams.ADVICE_CODE)
	String advice_code;
	@JsonProperty(value = AppParams.RESPONSE_CODE)
	String response_code;
}
