package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Payer;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.JsonNullToEmptySerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaypalProCreateOrderResponse implements JsonInterface {
	@JsonProperty(value = AppParams.ID)
	private String id;
	@JsonProperty(value = AppParams.INTENT)
	private String intent;
	@JsonProperty(value = AppParams.CREATE_TIME)
	private String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	private String updateTime;
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