package asia.leadsgen.pasp.model.dto.affiliate;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerResponse {

	@JsonProperty(value = AppParams.STATE)
	String state;
	@JsonProperty(value = AppParams.MESSAGE)
	String message;
	@JsonProperty(value = AppParams.BRAND)
	String brand;
	@JsonProperty(value = AppParams.LAST4)
	String last4;
	@JsonProperty(value = AppParams.EXP_MONTH)
	long expMonth;
	@JsonProperty(value = AppParams.EXP_YEAR)
	long expYear;
	@JsonProperty(value = AppParams.CUSTOMER_ID)
	String customerId;

}
