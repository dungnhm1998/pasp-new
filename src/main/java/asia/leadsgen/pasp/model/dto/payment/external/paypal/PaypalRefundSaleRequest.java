package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalRefundSaleRequest implements JsonInterface {
	@JsonProperty(value = AppParams.AMOUNT)
	Amount amount;
	@JsonProperty(value = AppParams.DESCRIPTION)
	String description;
	@JsonProperty(value = AppParams.INVOICE_NUMBER)
	String invoiceNumber;
	@JsonProperty(value = AppParams.REASON)
	String reason;
}
