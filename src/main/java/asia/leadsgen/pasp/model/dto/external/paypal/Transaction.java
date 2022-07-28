package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.Payee;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
	@JsonProperty(value = AppParams.AMOUNT)
	Amount amount;
	@JsonProperty(value = AppParams.DESCRIPTION)
	String description;
	@JsonProperty(value = AppParams.INVOICE_NUMBER)
	String invoiceNumber;
	@JsonProperty(value = AppParams.PAYMENT_OPTIONS)
	PaymentOptions payment_options;
	@JsonProperty(value = AppParams.ITEM_LIST)
	ItemList itemList;

	@JsonProperty(value = AppParams.CUSTOM)
	String custom;
	@JsonProperty(value = AppParams.NOTE_TO_PAYEE)
	String note_to_payee;
	@JsonProperty(value = AppParams.NOTIFY_URL)
	String notify_url;
	@JsonProperty(value = AppParams.ORDER_URL)
	String order_url;
	@JsonProperty(value = AppParams.PAYEE)
	Payee payee;
	@JsonProperty(value = AppParams.PAYMENT_OPTIONS)
	PaymentOptions paymentOptions;
	@JsonProperty(value = AppParams.REFERENCE_ID)
	String reference_id;
	@JsonProperty(value = AppParams.RELATED_RESOURCES)
	List<RelatedResources> relatedResources;
	@JsonProperty(value = AppParams.SOFT_DESCRIPTOR)
	String soft_descriptor;

}
