package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalPaymentTerm {
	@JsonProperty(value = AppParams.TERM_TYPE)
	String typeTerm;
	@JsonProperty(value = AppParams.DUE_DATE)
	String dueDate;
}