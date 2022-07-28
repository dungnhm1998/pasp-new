package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SellerProtection {
	@JsonProperty(value = AppParams.DISPUTE_CATEGORIES)
	List<String> dispute_categories;
	@JsonProperty(value = AppParams.STATUS)
	String status;
}
