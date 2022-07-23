package asia.leadsgen.pasp.model.dto.payment.external.BankOfUSA;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankUSAChargeRequest implements JsonInterface {
	@JsonProperty(value = AppParams.CC_NUMBER)
	String cc_number;
	@JsonProperty(value = AppParams.TRANSACTION_TYPE)
	String transaction_type = "00";
	@JsonProperty(value = AppParams.AMOUNT)
	String amount;
	@JsonProperty(value = AppParams.CC_EXPIRY)
	String cc_expiry;
	@JsonProperty(value = AppParams.CARDHOLDER_NAME)
	String cardholder_name;

	public BankUSAChargeRequest(String cc_number, String amount, String cc_expiry, String cardholder_name) {
		this.cc_number = cc_number;
		this.amount = amount;
		this.cc_expiry = cc_expiry;
		this.cardholder_name = cardholder_name;
	}
}