package asia.leadsgen.pasp.model.dto.affiliate;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInfo implements JsonInterface {

	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.AFF_ID)
	String affId;
	@JsonProperty(value = AppParams.SOURCE)
	String source;
	@JsonProperty(value = AppParams.REFERER)
	String reference;
	@JsonProperty(value = AppParams.EMAIL)
	String email;
	@JsonProperty(value = AppParams.PASSWORD)
	String password;
	@JsonProperty(value = AppParams.NAME)
	String naem;
	@JsonProperty(value = AppParams.AVATAR)
	String avatar;

}
