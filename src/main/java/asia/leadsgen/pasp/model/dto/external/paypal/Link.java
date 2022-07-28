package asia.leadsgen.pasp.model.dto.external.paypal;


import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Link {
	@JsonProperty(value = AppParams.METHOD)
	String method;
	@JsonProperty(value = AppParams.REL)
	String rel;
	@JsonProperty(value = AppParams.HREF)
	String href;
}
