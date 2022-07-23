package asia.leadsgen.pasp.model.dto.payment.external.BankOfUSA;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankUSARefundResponse {

	//
	int responseCode;
	String message;

}