package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.external.paypal.Link;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Capture {
	@JsonProperty(value = AppParams.STATUS)
	String status;
	@JsonProperty(value = AppParams.STATUS_DETAILS)
	StatusDetail statusDetails;
	@JsonProperty(value = AppParams.AMOUNT)
	Money amount;
	@JsonProperty(value = AppParams.CUSTOM_ID)
	String customId;
	@JsonProperty(value = AppParams.DISBURSEMENT_MODE)
	String disbursement_mode;
	@JsonProperty(value = AppParams.FINAL_CAPTURE)
	boolean final_capture;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.INVOICE_ID)
	String invoice_id;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
	@JsonProperty(value = AppParams.PROCESSOR_RESPONSE)
	ProcessorResponse processor_response;
	@JsonProperty(value = AppParams.SELLER_PROTECTION)
	SellerProtection seller_protection;
	@JsonProperty(value = AppParams.SELLER_RECEIVABLE_BREAKDOWN)
	SellerReceivableBreakdown seller_receivable_breakdown;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;
	@JsonProperty(value = AppParams.EXCHANGE_RATE)
	String exchange_rate;
	@JsonProperty(value = AppParams.INVOICE_NUMBER)
	String invoice_number;
	@JsonProperty(value = AppParams.IS_FINAL_CAPTURE)
	boolean is_final_capture;
	@JsonProperty(value = AppParams.NOTE_TO_PAYER)
	String note_to_payer;
	@JsonProperty(value = AppParams.PARENT_PAYMENT)
	String parent_payment;
	@JsonProperty(value = AppParams.REASON_CODE)
	String reason_code;
	@JsonProperty(value = AppParams.RECEIVABLE_AMOUNT)
	Money receivable_amount;
	@JsonProperty(value = AppParams.STATE)
	String state;
	@JsonProperty(value = AppParams.TRANSACTION_FEE)
	Money transaction_fee;
	@JsonProperty(value = AppParams.TRANSACTION_FEE_IN_RECEIVABLE_CURRENCY)
	Money transaction_fee_in_receivable_currency;
}
