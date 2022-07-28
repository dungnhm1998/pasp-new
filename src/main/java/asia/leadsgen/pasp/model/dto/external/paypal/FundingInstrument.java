package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FundingInstrument {
	@JsonProperty(value = AppParams.CREDIT_CARD)
	CreditCard creditCard;
	@JsonProperty(value = AppParams.CREDIT_CARD_TOKEN)
	CreditCardToken creditCardToken;
}