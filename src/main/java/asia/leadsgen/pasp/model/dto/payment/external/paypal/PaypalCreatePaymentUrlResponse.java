package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.JsonNullToEmptySerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;

@Data
public class PaypalCreatePaymentUrlResponse implements JsonInterface {
	@JsonProperty(value = AppParams.ID)
	private String id;
	@JsonProperty(value = AppParams.METHOD)
	private String method;
	@JsonProperty(value = AppParams.TYPE)
	private String type;
	@JsonProperty(value = AppParams.AMOUNT)
	private String amount;
	@JsonProperty(value = AppParams.CURRENCY)
	private String currency;
	@JsonProperty(value = AppParams.REFERENCE)
	private String reference;
	@JsonProperty(value = AppParams.STATE)
	@JsonSerialize(nullsUsing = JsonNullToEmptySerializer.class)
	private String state;
	@JsonProperty(value = AppParams.LINKS)
	ArrayList<Link> links;
}
