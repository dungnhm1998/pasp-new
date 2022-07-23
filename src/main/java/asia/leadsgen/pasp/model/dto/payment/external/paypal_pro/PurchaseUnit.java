package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseUnit {
	@JsonProperty(value = AppParams.AMOUNT)
	Amount amount;
	@JsonProperty(value = AppParams.CUSTOM_ID)
	String customId;
	@JsonProperty(value = AppParams.DESCRIPTION)
	String description;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.INVOICE_ID)
	String invoiceId;
	@JsonProperty(value = AppParams.ITEMS)
	List<Item> items;
	@JsonProperty(value = AppParams.PAYEE)
	Payee payee;
	@JsonProperty(value = AppParams.PAYMENT_INSTRUCTION)
	PaymentInstruction paymentInstruction;
	@JsonProperty(value = AppParams.PAYMENTS)
	List<PurchaseUnitPayment> payments;
	@JsonProperty(value = AppParams.REFERENCE_ID)
	String reference_id;
	@JsonProperty(value = AppParams.SHIPPING)
	ShippingDetail shippingDetail;
	@JsonProperty(value = AppParams.SOFT_DESCRIPTOR)
	String softDescriptor;

}
