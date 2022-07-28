package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameDetail {
	@JsonProperty(value = AppParams.NAME)
	String name;
	@JsonProperty(value = AppParams.FULL_NAME)
	String fullName;
	@JsonProperty(value = AppParams.SURNAME)
	String surName;
	@JsonProperty(value = AppParams.PREFIX)
	String prefix;
	@JsonProperty(value = AppParams.SUFFIX)
	String suffix;
	@JsonProperty(value = AppParams.MIDDLE_NAME)
	String middleName;
	@JsonProperty(value = AppParams.GIVEN_NAME)
	String givenName;
}
