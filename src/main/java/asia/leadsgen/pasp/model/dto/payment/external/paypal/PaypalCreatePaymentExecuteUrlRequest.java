package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalCreatePaymentExecuteUrlRequest implements JsonInterface {
	@JsonProperty(value = AppParams.PAYER_ID)
	String payerId;
	@JsonProperty(value = AppParams.TRANSACTIONS)
	List<Transaction> transaction;
}
