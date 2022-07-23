package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Amount;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FmfDetail {
	@JsonProperty(value = AppParams.FILTER_ID)
	String filter_id;
	@JsonProperty(value = AppParams.FILTER_TYPE)
	String filter_type;
	@JsonProperty(value = AppParams.DESCRIPTION)
	String description;
	@JsonProperty(value = AppParams.NAME)
	String name;
}
