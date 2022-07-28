package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalSendInvoiceRequest implements JsonInterface {
	@JsonProperty(value = AppParams.ADDITIONAL_RECIPIENTS)
	List<String> additionalRecipients;
	@JsonProperty(value = AppParams.NOTE)
	String note;
	@JsonProperty(value = AppParams.SEND_TO_INVOICER)
	boolean sendToInvoicer;
	@JsonProperty(value = AppParams.SEND_TO_RECIPIENT)
	boolean sendToRecipient;
	@JsonProperty(value = AppParams.SUBJECT)
	String subject;
}