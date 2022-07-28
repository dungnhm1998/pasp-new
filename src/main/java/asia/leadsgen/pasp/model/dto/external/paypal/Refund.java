package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.AmountPro;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Refund {
	@JsonProperty(value = AppParams.AMOUNT)
	AmountPro amountPro;
	@JsonProperty(value = AppParams.CREATE_TIME)
	String createTime;
	@JsonProperty(value = AppParams.UPDATE_TIME)
	String updateTime;
	@JsonProperty(value = AppParams.CAPTURE_ID)
	String captureId;
	@JsonProperty(value = AppParams.DESCRIPTION)
	String description;
	@JsonProperty(value = AppParams.ID)
	String id;
	@JsonProperty(value = AppParams.SALE_ID)
	String saleId;
	@JsonProperty(value = AppParams.INVOICE_NUMBER)
	String invoice_number;
	@JsonProperty(value = AppParams.LINKS)
	List<Link> links;
	@JsonProperty(value = AppParams.PARENT_PAYMENT)
	String parent_payment;
	@JsonProperty(value = AppParams.STATE)
	String state;
}