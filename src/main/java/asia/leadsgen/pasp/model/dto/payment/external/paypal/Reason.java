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
	private String name;
	@JsonProperty(value = AppParams.MESSAGE)
	private String message;
	@JsonProperty(value = AppParams.DETAILS)
	private String details;
	@JsonProperty(value = AppParams.CODE)
	private Integer code;
	@JsonProperty(value = AppParams.METHOD)
	private String method;
}