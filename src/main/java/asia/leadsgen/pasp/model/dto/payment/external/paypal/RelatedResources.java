package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Capture;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.NameDetail;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Phone;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.TaxInfo;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RelatedResources {
	@JsonProperty(value = AppParams.AUTHORIZATION)
	Authorization authorization;
	@JsonProperty(value = AppParams.CAPTURE)
	Capture capture;
	@JsonProperty(value = AppParams.ORDER)
	Authorization order;
	@JsonProperty(value = AppParams.REFUND)
	Refund refund;
	@JsonProperty(value = AppParams.SALE)
	Sale sale;
}
