package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalMetaData {
	@JsonProperty(value = AppParams.CREATE_TIME)
	String create_time;
	@JsonProperty(value = AppParams.CREATED_BY)
	String created_by;
	@JsonProperty(value = AppParams.LAST_UPDATE_TIME)
	String lastUpdateTime;
	@JsonProperty(value = AppParams.LAST_UPDATED_BY)
	String lastUpdatedBy;
	@JsonProperty(value = AppParams.CANCEL_TIME)
	String cancelTime;
	@JsonProperty(value = AppParams.CANCELLED_BY)
	String cancelledBy;
	@JsonProperty(value = AppParams.CREATED_BY_FLOW)
	String createdByFlow;
	@JsonProperty(value = AppParams.FIRST_SENT_TIME)
	String firstSentTime;
	@JsonProperty(value = AppParams.INVOICER_VIEW_URL)
	String invoicerViewUrl;
	@JsonProperty(value = AppParams.LAST_SENT_BY)
	String lastSentBy;
	@JsonProperty(value = AppParams.LAST_SENT_TIME)
	String lastSentTime;
	@JsonProperty(value = AppParams.RECIPIENT_VIEW_URL)
	String recipientViewUrl;
}