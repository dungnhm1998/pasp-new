package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.AmountPro;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.Money;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.ProcessorResponse;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sale {
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.AMOUNT)
	AmountPro amountPro;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;
	@JsonProperty(value = AppParams.CLEARING_TIME)
	String clearing_time;
	@JsonProperty(value = AppParams.BILLING_AGREEMENT_ID)
	String billing_agreement_id;
	@JsonProperty(value = AppParams.FMF_DETAILS)
	FmfDetail fmf_details;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
	@JsonProperty(value = AppParams.PAYMENT_HOLD_REASONS)
	List<String> payment_hold_reasons;
	@JsonProperty(value = AppParams.PAYMENT_HOLD_STATUS)
	String payment_hold_status;
	@JsonProperty(value = AppParams.PAYMENT_MODE)
	String payment_mode;
	@JsonProperty(value = AppParams.PROCESSOR_RESPONSE)
	ProcessorResponse processor_response;
	@JsonProperty(value = AppParams.PROTECTION_ELIGIBILITY)
	String protection_eligibility;
	@JsonProperty(value = AppParams.PROTECTION_ELIGIBILITY_TYPE)
	String protection_eligibility_type;
	@JsonProperty(value = AppParams.REASON_CODE)
	String reason_code;
	@JsonProperty(value = AppParams.RECEIPT_ID)
	String receipt_id;
	@JsonProperty(value = AppParams.RECEIVABLE_AMOUNT)
	Money receivable_amount;
	@JsonProperty(value = AppParams.TRANSACTION_FEE)
	Money transaction_fee;
	@JsonProperty(value = AppParams.TRANSACTION_FEE_IN_RECEIVABLE_CURRENCY)
	Money transaction_fee_in_receivable_currency;
}