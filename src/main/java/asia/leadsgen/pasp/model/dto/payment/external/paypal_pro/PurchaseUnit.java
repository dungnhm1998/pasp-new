package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PurchaseUnit {
	@JsonProperty(value = AppParams.AMOUNT)
	Money amount;
	@JsonProperty(value = AppParams.CUSTOM_ID)
	java.lang.String customId;
	@JsonProperty(value = AppParams.DESCRIPTION)
	java.lang.String description;
	@JsonProperty(value = AppParams.INVOICE_ID)
	java.lang.String invoiceId;
	@JsonProperty(value = AppParams.ITEMS)
	List<Item> items;
	@JsonProperty(value = AppParams.PAYEE)
	Payee payee;
	@JsonProperty(value = AppParams.PAYMENT_INSTRUCTION)
	PaymentInstruction paymentInstruction;
	@JsonProperty(value = AppParams.REFERENCE_ID)
	java.lang.String reference_id;
	@JsonProperty(value = AppParams.SHIPPING)
	ShippingDetail shippingDetail;
	@JsonProperty(value = AppParams.SOFT_DESCRIPTOR)
	java.lang.String softDescriptor;

}
