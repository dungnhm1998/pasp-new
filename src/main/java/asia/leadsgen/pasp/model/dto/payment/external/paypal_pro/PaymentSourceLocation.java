package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentSourceLocation {
	@JsonProperty(value = AppParams.BIC)
	String bic;
	@JsonProperty(value = AppParams.CARD_LAST_DIGITS)
	String cardLastDigits;
	@JsonProperty(value = AppParams.COUNTRY_CODE)
	String countryCode;
	@JsonProperty(value = AppParams.IBAN_LAST_CHARS)
	String ibanLastChars;
	@JsonProperty(value = AppParams.NAME)
	String name;
	@JsonProperty(value = AppParams.EMAIL)
	String email;
}
