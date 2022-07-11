package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.payment.external.paypal.Payer;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalProCreateOrderRequest {
	@JsonProperty(value = AppParams.INTENT)
	Money intent;
	@JsonProperty(value = AppParams.PURCHASE_UNITS)
	List<PurchaseUnit> purchaseUnits;
	@JsonProperty(value = AppParams.APPLICATION_CONTEXT)
	PaypalAppContext paypalAppContext;
	@JsonProperty(value = AppParams.PAYER)
	Payer payer;
	@JsonProperty(value = AppParams.PROCESSING_INSTRUCTION)
	Money processingInstruction;
}
