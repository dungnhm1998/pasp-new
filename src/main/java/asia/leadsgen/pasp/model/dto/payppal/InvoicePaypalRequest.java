package asia.leadsgen.pasp.model.dto.payppal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreateInvoiceRequest;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InvoicePaypalRequest implements JsonInterface {

	@JsonProperty(value = AppParams.INVOICE_INFO)
	PaypalCreateInvoiceRequest invoiceInfo;

}
