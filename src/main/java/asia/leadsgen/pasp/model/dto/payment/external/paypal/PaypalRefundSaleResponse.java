package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Money;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalRefundSaleResponse implements JsonInterface {
	@JsonProperty(value = AppParams.AMOUNT)
	Amount amount;
	@JsonProperty(value = AppParams.CAPTURE_ID)
	String capture_id;
	@JsonProperty(value = AppParams.CUSTOM)
	String custom;
	@JsonProperty(value = AppParams.DESCRIPTION)
	String description;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.INVOICE_NUMBER)
	String invoiceNumber;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
	@JsonProperty(value = AppParams.PARENT_PAYMENT)
	String parentPayment;
	@JsonProperty(value = AppParams.REASON)
	String reason;
	@JsonProperty(value = AppParams.REFUND_FROM_RECEIVED_AMOUNT)
	Money refundFromReceivedAmount;
	@JsonProperty(value = AppParams.REFUND_FROM_TRANSACTION_FEE)
	Money refundFromTransactionFee;
	@JsonProperty(value = AppParams.SALE_ID)
	String saleId;
	@JsonProperty(value = AppParams.STATE)
	String state;
	@JsonProperty(value = AppParams.TOTAL_REFUNDED_AMOUNT)
	Money totalRefundedAmount;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;

	//
	int responseCode;
	String message;
}
