package asia.leadsgen.pasp.model.dto.affiliate;

import asia.leadsgen.pasp.model.dto.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.external.paypal.Reason;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCardResponse {

	@JsonProperty(value = AppParams.TYPE)
	String type;
	@JsonProperty(value = AppParams.EMAIL)
	String email;
	@JsonProperty(value = AppParams.NAME)
	String name;
	@JsonProperty(value = AppParams.STREET)
	String street;
	@JsonProperty(value = AppParams.POSTCODE)
	String postcode;
	@JsonProperty(value = AppParams.CITY)
	String city;
	@JsonProperty(value = AppParams.COUNTRY_CODE)
	String country_code;
	@JsonProperty(value = AppParams.COUNTRY_NAME)
	String country_name;
	@JsonProperty(value = AppParams.AMOUNT)
	String amount;
	@JsonProperty(value = AppParams.CURRENCY)
	String currency;
	@JsonProperty(value = AppParams.TOKEN)
	String token;

}
