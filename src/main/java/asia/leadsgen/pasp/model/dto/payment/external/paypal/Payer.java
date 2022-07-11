package asia.leadsgen.pasp.model.dto.payment.external.paypal;

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
public class Payer {
	@JsonProperty(value = AppParams.PAYMENT_METHOD)
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	String paymentMethod;
	@JsonProperty(value = AppParams.NAME)
	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	String name;

	//paypal pro
	@JsonProperty(value = AppParams.EMAIL_ADDRESS)
	String emailAddress;
	@JsonProperty(value = AppParams.PAYER_ID)
	String payerId;
	@JsonProperty(value = AppParams.ADDRESS)
	ShippingAddress address;
	@JsonProperty(value = AppParams.BIRTH_DATE)
	String birthDate;
	@JsonProperty(value = AppParams.NAME)
	NameDetail payerName;
	@JsonProperty(value = AppParams.PHONE)
	Phone phone;
	@JsonProperty(value = AppParams.TAX_INFO)
	TaxInfo taxInfo;
}
