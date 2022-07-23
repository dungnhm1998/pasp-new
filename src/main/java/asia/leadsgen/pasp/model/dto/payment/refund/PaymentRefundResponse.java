package asia.leadsgen.pasp.model.dto.payment.refund;

import asia.leadsgen.pasp.model.dto.payment.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Reason;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefundResponse {

	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.MESSAGE)
	String message;
	@JsonProperty(value = AppParams.STATUS)
	String status;
}
