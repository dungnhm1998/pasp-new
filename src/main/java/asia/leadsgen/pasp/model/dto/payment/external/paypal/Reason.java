package asia.leadsgen.pasp.model.dto.payment.external.paypal;


import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reason {
	@JsonProperty(value = AppParams.NAME)
	String name;
	@JsonProperty(value = AppParams.MESSAGE)
	String message;
	@JsonProperty(value = AppParams.DETAILS)
	String details;
	@JsonProperty(value = AppParams.CODE)
	Integer code;
	@JsonProperty(value = AppParams.METHOD)
	String method;
}