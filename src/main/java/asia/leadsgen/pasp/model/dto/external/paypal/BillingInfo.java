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
public class BillingInfo {
	@JsonProperty(value = AppParams.ADDITIONAL_INFO)
	String additionalInfo;
	@JsonProperty(value = AppParams.EMAIL_ADDRESS)
	String emailAddress;
	@JsonProperty(value = AppParams.LANGUAGE)
	String language;
	@JsonProperty(value = AppParams.PHONES)
	List<PhoneDetail> phones;
}