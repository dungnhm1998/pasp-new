 package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

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
public class PaypalProRefundRequest implements JsonInterface {
	@JsonProperty(value = AppParams.AMOUNT)
	Amount amount;
	@JsonProperty(value = AppParams.INVOICE_ID)
	String invoiceId;
	@JsonProperty(value = AppParams.NOTE_TO_PAYER)
	String noteToPayer;

	//
	int responseCode;
	String message;
}
