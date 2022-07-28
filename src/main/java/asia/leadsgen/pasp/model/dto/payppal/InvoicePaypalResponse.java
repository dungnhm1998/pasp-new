package asia.leadsgen.pasp.model.dto.payppal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePaypalResponse {

	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.ACCOUNT_NAME)
	String accountName;
	@JsonProperty(value = AppParams.HREF)
	String href;

}
