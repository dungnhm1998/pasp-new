package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PaymentInstruction {
	@JsonProperty(value = AppParams.DISBURSEMENT_MODE)
	Money disbursementMode;
	@JsonProperty(value = AppParams.PAYEE_PRICING_TIER_ID)
	Money payeePricingTierId;
	@JsonProperty(value = AppParams.PAYEE_RECEIVABLE_FX_RATE_ID)
	Money payeeReceivableFxRateId;
	@JsonProperty(value = AppParams.PLATFORM_FEES)
	List<PlatformFee> platformFees;
}
