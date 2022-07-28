package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PaypalAppContext;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalCreatePaymentExecuteUrlResponse implements JsonInterface {
	@JsonProperty(value = AppParams.APPLICATION_CONTEXT)
	PaypalAppContext paypalAppContext;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;
	@JsonProperty(value = AppParams.EXPERIENCE_PROFILE_ID)
	String experienceProfileId;
	@JsonProperty(value = AppParams.FAILURE_REASON)
	String failureReason;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.INTENT)
	String intent;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
	@JsonProperty(value = AppParams.NOTE_TO_PAYER)
	String note_to_payer;
	@JsonProperty(value = AppParams.STATE)
	String state;
	@JsonProperty(value = AppParams.PAYER)
	Payer payer;
	@JsonProperty(value = AppParams.TRANSACTIONS)
	ArrayList<Transaction> transactions;
	@JsonProperty(value = AppParams.REDIRECT_URLS)
	RedirectUrls redirect_urls;

	//
	int responseCode;
	String message;
}
