package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recepients {
	@JsonProperty(value = AppParams.BILLING_INFO)
	BillingInfo amountPro;
	@JsonProperty(value = AppParams.SHIPPING_INFO)
	BillingInfo shippingInfo;
}