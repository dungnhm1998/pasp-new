package asia.leadsgen.pasp.model.dto.payment;

import asia.leadsgen.pasp.model.dto.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.external.paypal.Reason;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.PAY_ID)
	String payId;
	@JsonProperty(value = AppParams.TOKEN_ID)
	String tokenId;
	@JsonProperty(value = AppParams.REFERENCE)
	String reference;
	@JsonProperty(value = AppParams.AMOUNT)
	String amount;
	@JsonProperty(value = AppParams.CURRENCY)
	String currency;
	@JsonProperty(value = AppParams.METHOD)
	String method;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;
	@JsonProperty(value = AppParams.STATE)
	String state;
	@JsonProperty(value = AppParams.REASON)
	Reason reason;
	@JsonProperty(value = AppParams.PAYER)
	Payer payer;
	@JsonProperty(value = AppParams.LINKS)
	ArrayList<Link> links;
	@JsonProperty(value = AppParams.ACCOUNT_NAME)
	String accountName;

}
