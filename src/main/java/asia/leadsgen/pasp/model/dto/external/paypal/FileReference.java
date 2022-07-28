package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileReference {
	@JsonProperty(value = AppParams.CONTENT_TYPE)
	String contentType;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.SIZE)
	String size;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.REFERENCE_URL)
	String referenceUrl;
}
