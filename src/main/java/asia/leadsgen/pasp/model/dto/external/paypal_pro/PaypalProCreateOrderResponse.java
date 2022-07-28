package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.external.paypal.Payer;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaypalProCreateOrderResponse implements JsonInterface {
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.INTENT)
	String intent;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;
	@JsonProperty(value = AppParams.LINKS)
	ArrayList<Link> links;
	@JsonProperty(value = AppParams.PAYER)
	Payer payer;
	@JsonProperty(value = AppParams.PAYMENT_SOURCE)
	PaymentSource paymentSource;
	@JsonProperty(value = AppParams.PROCESSING_INSTRUCTION)
	String processingInstruction;
	@JsonProperty(value = AppParams.PURCHASE_UNITS)
	List<PurchaseUnit> purchaseUnits;
	@JsonProperty(value = AppParams.STATUS)
	String status;

	//
	int responseCode;
	String message;
}