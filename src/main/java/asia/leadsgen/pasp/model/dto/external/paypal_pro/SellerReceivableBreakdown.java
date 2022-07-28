package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SellerReceivableBreakdown {
	@JsonProperty(value = AppParams.GROSS_AMOUNT)
	Money gross_amount;
	@JsonProperty(value = AppParams.EXCHANGE_RATE)
	ExchangeRate exchange_rate;
	@JsonProperty(value = AppParams.NET_AMOUNT)
	Money net_amount;
	@JsonProperty(value = AppParams.PAYPAL_FEE)
	Money paypal_fee;
	@JsonProperty(value = AppParams.PAYPAL_FEE_IN_RECEIVABLE_CURRENCY)
	Money paypal_fee_in_receivable_currency;
	@JsonProperty(value = AppParams.PLATFORM_FEES)
	List<Money> platform_fees;
	@JsonProperty(value = AppParams.RECEIVABLE_AMOUNT)
	Money receivable_amount;
}
