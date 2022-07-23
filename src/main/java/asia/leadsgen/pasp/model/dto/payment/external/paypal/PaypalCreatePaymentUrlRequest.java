package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalCreatePaymentUrlRequest implements JsonInterface {
	@JsonProperty(value = AppParams.INTENT)
	String intent;
	@JsonProperty(value = AppParams.PAYER)
	Payer payer;
	@JsonProperty(value = AppParams.TRANSACTIONS)
	ArrayList<Transaction> transactions;
	@JsonProperty(value = AppParams.REDIRECT_URLS)
	RedirectUrls redirect_urls;
}
