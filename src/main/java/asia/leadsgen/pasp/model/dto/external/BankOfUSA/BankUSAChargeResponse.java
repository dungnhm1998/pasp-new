package asia.leadsgen.pasp.model.dto.external.BankOfUSA;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankUSAChargeResponse {
	@JsonProperty(value = AppParams.TRANSACTION_TAG)
	Long transactionTag;
	@JsonProperty(value = AppParams.AUTHORIZATION_NUM)
	String authorizationNum;
	@JsonProperty(value = AppParams.CTR)
	String ctr;

	//
	int responseCode;
	String message;

}
