package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Money {
	@JsonProperty(value = AppParams.CURRENCY_CODE)
	public java.lang.String currency;
	@JsonProperty(value = AppParams.VALUE)
	public java.lang.String value;
}
