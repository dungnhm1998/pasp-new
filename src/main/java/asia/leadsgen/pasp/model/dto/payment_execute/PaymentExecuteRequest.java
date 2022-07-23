package asia.leadsgen.pasp.model.dto.payment_execute;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PaymentExecuteRequest implements JsonInterface {

	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.PAYER_ID)
	String payerId;
	@JsonProperty(value = AppParams.PAY_ID)
	String payId;
	@JsonProperty(value = AppParams.TOKEN_ID)
	String tokenId;
	@JsonProperty(value = AppParams.METHOD)
	String method;

	@JsonProperty(value = AppParams.AMOUNT)
	String amount;
	@JsonProperty(value = AppParams.CURRENCY)
	String currency;
	@JsonProperty(value = AppParams.REFERENCE)
	String reference;

	@JsonProperty(value = AppParams.PAYMENT)
	PaymentInfo payment;

	@Data
	public static class PaymentInfo{
		@JsonProperty(value = AppParams.TOKEN)
		String token;
	}

}
