package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetail {
	@JsonProperty(value = AppParams.CURRENCY)
	String currency;
	@JsonProperty(value = AppParams.ATTACHMENTS)
	List<FileReference> attachments;
	@JsonProperty(value = AppParams.MEMO)
	String memo;
	@JsonProperty(value = AppParams.NOTE)
	String note;
	@JsonProperty(value = AppParams.REFERENCE)
	String reference;
	@JsonProperty(value = AppParams.TERMS_AND_CONDITIONS)
	String terms_and_conditions;
	@JsonProperty(value = AppParams.INVOICE_DATE)
	String invoice_date;
	@JsonProperty(value = AppParams.INVOICE_NUMBER)
	String invoice_number;
	@JsonProperty(value = AppParams.METADATA)
	PaypalMetaData metaData;
	@JsonProperty(value = AppParams.PAYMENT_TERM)
	PaypalPaymentTerm paypalMetaData;
}