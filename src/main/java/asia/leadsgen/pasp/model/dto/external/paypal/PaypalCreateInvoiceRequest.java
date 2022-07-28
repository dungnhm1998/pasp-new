package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.AmountPro;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.ItemPro;
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
public class PaypalCreateInvoiceRequest implements JsonInterface {
	@JsonProperty(value = AppParams.DETAIL)
	InvoiceDetail detail;
	@JsonProperty(value = AppParams.ADDITIONAL_RECIPIENTS)
	List<String> additionalRecipients;
	@JsonProperty(value = AppParams.AMOUNT)
	AmountPro amount;
	@JsonProperty(value = AppParams.CONFIGURATION)
	Configuration configuration;
	@JsonProperty(value = AppParams.DUE_AMOUNT)
	Money dueAmount;
	@JsonProperty(value = AppParams.GRATUITY)
	Money gratuity;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.INVOICER)
	InvoicerInfo invoicer;
	@JsonProperty(value = AppParams.ITEMS)
	List<ItemPro> items;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
	@JsonProperty(value = AppParams.PARENT_ID)
	String parentId;
	@JsonProperty(value = AppParams.PAYMENTS)
	PaypalPayment payments;
	@JsonProperty(value = AppParams.PRIMARY_RECIPIENTS)
	List<Recepients> primaryRecipients;
	@JsonProperty(value = AppParams.REFUNDS)
	InvoiceRefund refunds;
	@JsonProperty(value = AppParams.STATUS)
	String status;
}
