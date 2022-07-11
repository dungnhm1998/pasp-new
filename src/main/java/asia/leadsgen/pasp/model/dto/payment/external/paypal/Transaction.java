package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
	@JsonProperty(value = AppParams.AMOUNT)
	public Amount amount;
	@JsonProperty(value = AppParams.DESCRIPTION)
	public String description;
	@JsonProperty(value = AppParams.INVOICE_NUMBER)
	public String invoiceNumber;
	@JsonProperty(value = AppParams.PAYMENT_OPTIONS)
	public PaymentOptions payment_options;
	@JsonProperty(value = AppParams.ITEM_LIST)
	public ItemList itemList;
}