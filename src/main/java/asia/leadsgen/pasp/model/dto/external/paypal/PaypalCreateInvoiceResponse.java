package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.JsonNullToEmptySerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;

@Data
public class PaypalCreateInvoiceResponse implements JsonInterface {
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.METHOD)
	String method;
	@JsonProperty(value = AppParams.TYPE)
	String type;
	@JsonProperty(value = AppParams.AMOUNT)
	String amount;
	@JsonProperty(value = AppParams.CURRENCY)
	String currency;
	@JsonProperty(value = AppParams.REFERENCE)
	String reference;
	@JsonProperty(value = AppParams.STATE)
	@JsonSerialize(nullsUsing = JsonNullToEmptySerializer.class)
	String state;
	@JsonProperty(value = AppParams.LINKS)
	ArrayList<Link> links;

	//
	int responseCode;
	String message;
}
