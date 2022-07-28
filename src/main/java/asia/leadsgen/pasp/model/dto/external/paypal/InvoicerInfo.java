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
public class InvoicerInfo {
	@JsonProperty(value = AppParams.ADDITIONAL_NOTES)
	String additionalNotes;
	@JsonProperty(value = AppParams.EMAIL_ADDRESS)
	String emailAddress;
	@JsonProperty(value = AppParams.LOGO_URL)
	String logoUrl;
	@JsonProperty(value = AppParams.PHONES)
	List<PhoneDetail> phones;
	@JsonProperty(value = AppParams.TAX_ID)
	String taxId;
	@JsonProperty(value = AppParams.WEBSITE)
	String website;
}