package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.Money;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalPaymentDetail {
	@JsonProperty(value = AppParams.METHOD)
	String method;
	@JsonProperty(value = AppParams.AMOUNT)
	Money amount;
	@JsonProperty(value = AppParams.NOTE)
	String note;
	@JsonProperty(value = AppParams.PAYMENT_DATE)
	String paymentDate;
	@JsonProperty(value = AppParams.PAYMENT_ID)
	String paymentId;
	@JsonProperty(value = AppParams.SHIPPING_INFO)
	ContactInfo shippingInfo;
	@JsonProperty(value = AppParams.TYPE)
	String type;
}