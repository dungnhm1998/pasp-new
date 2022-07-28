package asia.leadsgen.pasp.model.dto.payment.refund;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalRefundSaleRequest;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentRefundRequest implements JsonInterface {

	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.METHOD)
	String method;
	@JsonProperty(value = AppParams.ACCOUNT_NAME)
	String accountName;
	@JsonProperty(value = AppParams.DATA)
	PaypalRefundSaleRequest refundInfo;

	@JsonProperty(value = AppParams.NOTE)
	@JsonIgnore
	String note;

}
