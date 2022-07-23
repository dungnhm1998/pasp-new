package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Link;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalProRefundResponse implements JsonInterface {
	@JsonProperty(value = AppParams.AMOUNT)
	Amount amount;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.INVOICE_ID)
	String invoiceId;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
	@JsonProperty(value = AppParams.NOTE_TO_PAYER)
	String noteToPayer;
	@JsonProperty(value = AppParams.SELLER_PAYABLE_BREAKDOWN)
	SellerPayableBreakdown sellerPayableBreakdown;
	@JsonProperty(value = AppParams.STATUS)
	String status;
	@JsonProperty(value = AppParams.STATUS_DETAILS)
	List<StatusDetail> statusDetails;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;

	//
	int responseCode;
	String message;
}
