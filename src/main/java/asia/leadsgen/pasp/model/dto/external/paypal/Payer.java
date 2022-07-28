package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.model.dto.external.paypal_pro.NameDetail;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.Phone;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.TaxInfo;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Payer {
	@JsonProperty(value = AppParams.PAYMENT_METHOD)
	String paymentMethod;
	@JsonProperty(value = AppParams.NAME)
	String name;
	@JsonProperty(value = AppParams.FUNDING_INSTRUMENTS)
	List<FundingInstrument> fundingInstruments;
	@JsonProperty(value = AppParams.PAYER_INFO)
	PayerInfo payerInfo;
	@JsonProperty(value = AppParams.STATUS)
	String status;

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
