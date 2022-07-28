package asia.leadsgen.pasp.model.dto.external.BankOfUSA;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankUSARefundRequest implements JsonInterface {
	@JsonProperty(value = AppParams.AMOUNT)
	String amount;
	@JsonProperty(value = AppParams.TRANSACTION_TAG)
	String transactionTag;
	@JsonProperty(value = AppParams.AUTHORIZATION_NUM)
	String authorizationNum;
	@JsonProperty(value = AppParams.TRANSACTION_TYPE)
	String transaction_type = "34";

	@JsonProperty(value = AppParams.GATEWAY_ID)
	String gatewayId;
	@JsonProperty(value = AppParams.PASSWORD)
	String password;


	public BankUSARefundRequest(String amount, String transactionTag, String authorizationNum, String gatewayId, String password) {
		this.amount = amount;
		this.transactionTag = transactionTag;
		this.authorizationNum = authorizationNum;
		this.gatewayId = gatewayId;
		this.password = password;
	}
}