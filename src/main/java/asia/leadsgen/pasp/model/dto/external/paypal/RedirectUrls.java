package asia.leadsgen.pasp.model.dto.external.paypal;


import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RedirectUrls {
	@JsonProperty(value = AppParams.RETURN_URL)
	String return_url;
	@JsonProperty(value = AppParams.CANCEL_URL)
	String cancel_url;
}