package asia.leadsgen.pasp.model.dto.stripe;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceStripeResponse {

	@JsonProperty(value = AppParams.BALANCE)
	double balance;
	@JsonProperty(value = AppParams.ACCOUNT_NAME)
	String accountName;

}
