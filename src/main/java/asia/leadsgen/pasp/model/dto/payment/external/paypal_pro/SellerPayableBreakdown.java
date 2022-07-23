package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerPayableBreakdown {

	@JsonProperty(value = AppParams.GROSS_AMOUNT)
	Money grossAmount;
	@JsonProperty(value = AppParams.NET_AMOUNT)
	Money netAmount;
	@JsonProperty(value = AppParams.NET_AMOUNT_BREAKDOWN)
	List<NetAmountBreakdown> netAmountBreakdowns;
	@JsonProperty(value = AppParams.NET_AMOUNT_IN_RECEIVABLE_CURRENCY)
	Money netAmountInReceivableCurrency;
	@JsonProperty(value = AppParams.PAYPAL_FEE)
	Money paypalFee;
	@JsonProperty(value = AppParams.PAYPAL_FEE_IN_RECEIVABLE_CURRENCY)
	Money paypal_fee_in_receivable_currency;
	@JsonProperty(value = AppParams.PLATFORM_FEES)
	List<PlatformFee> platformFees;
	@JsonProperty(value = AppParams.TOTAL_REFUNDED_AMOUNT)
	Money totalRefundedAmount;

}
