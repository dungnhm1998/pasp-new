package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PaymentSource {
	@JsonProperty(value = AppParams.BANCONTACT)
	PaymentSourceLocation bancontact;
	@JsonProperty(value = AppParams.BLIK)
	PaymentSourceLocation blink;
	@JsonProperty(value = AppParams.EPS)
	PaymentSourceLocation eps;
	@JsonProperty(value = AppParams.GIROPAY)
	PaymentSourceLocation giropay;
	@JsonProperty(value = AppParams.IDEAL)
	PaymentSourceLocation ideal;
	@JsonProperty(value = AppParams.MYBANK)
	PaymentSourceLocation mybank;
	@JsonProperty(value = AppParams.P24)
	PaymentSourceLocation p24;
	@JsonProperty(value = AppParams.SOFORT)
	PaymentSourceLocation sofort;
	@JsonProperty(value = AppParams.TRUSTLY)
	PaymentSourceLocation trustly;
	@JsonProperty(value = AppParams.TOKEN)
	Token token;

}
