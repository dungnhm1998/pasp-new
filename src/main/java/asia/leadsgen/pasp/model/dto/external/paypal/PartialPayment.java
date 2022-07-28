package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.Money;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialPayment {
	@JsonProperty(value = AppParams.ALLOW_PARTIAL_PAYMENT)
	boolean allowPartialPayment;
	@JsonProperty(value = AppParams.MINIMUM_AMOUNT_DUE)
	Money minimumAmountDue;
}