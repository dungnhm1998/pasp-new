package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.AmountPro;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.ProcessorResponse;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authorization {
	@JsonProperty(value = AppParams.AMOUNT)
	AmountPro amountPro;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;
	@JsonProperty(value = AppParams.FMF_DETAILS)
	FmfDetail fmfDetail;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
	@JsonProperty(value = AppParams.PARENT_PAYMENT)
	String parent_payment;
	@JsonProperty(value = AppParams.PAYMENT_MODE)
	String payment_mode;
	@JsonProperty(value = AppParams.PENDING_REASON)
	String pending_reason;
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
	@JsonProperty(value = AppParams.STATE)
	String state;
	@JsonProperty(value = AppParams.VALID_UNTIL)
	String valid_until;
}