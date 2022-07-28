package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.Money;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalPayment {
	@JsonProperty(value = AppParams.PAID_AMOUNT)
	Money paidAmount;
	@JsonProperty(value = AppParams.TRANSACTIONS)
	List<PaypalPaymentDetail> transactions;
}