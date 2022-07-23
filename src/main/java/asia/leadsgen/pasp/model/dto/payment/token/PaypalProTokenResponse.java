package asia.leadsgen.pasp.model.dto.payment.token;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalProTokenResponse {
	@JsonProperty(value = AppParams.SCOPE)
	String scope;
	@JsonProperty(value = AppParams.ACCESS_TOKEN)
	String accessToken;
	@JsonProperty(value = AppParams.TOKEN_TYPE)
	String tokenType;
	@JsonProperty(value = AppParams.APP_ID)
	String appId;
	@JsonProperty(value = AppParams.EXPIRES_IN)
	long expiresIn;
	@JsonProperty(value = AppParams.NONCE)
	String nonce;

	@JsonProperty(value = AppParams.ERROR)
	String error;
//	@JsonProperty(value = AppParams.ERROR_DESCRIPTION)
//	String errorDescription;
}
