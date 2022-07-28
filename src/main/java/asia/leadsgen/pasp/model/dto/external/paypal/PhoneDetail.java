package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDetail {
	@JsonProperty(value = AppParams.COUNTRY_CODE)
	String countryCode;
	@JsonProperty(value = AppParams.NATIONAL_NUMBER)
	String nationalNumber;
	@JsonProperty(value = AppParams.EXTENSION_NUMBER)
	String extensionNumbher;
	@JsonProperty(value = AppParams.PHONE_TYPE)
	String phoneType;
}