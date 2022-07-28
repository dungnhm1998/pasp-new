package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.external.paypal.Payer;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalProCreateOrderRequest implements JsonInterface {
	@JsonProperty(value = AppParams.INTENT)
	String intent;
	@JsonProperty(value = AppParams.PURCHASE_UNITS)
	List<PurchaseUnit> purchaseUnits;
	@JsonProperty(value = AppParams.APPLICATION_CONTEXT)
	PaypalAppContext paypalAppContext;
	@JsonProperty(value = AppParams.PAYER)
	Payer payer;
	@JsonProperty(value = AppParams.PROCESSING_INSTRUCTION)
	String processingInstruction;
}
