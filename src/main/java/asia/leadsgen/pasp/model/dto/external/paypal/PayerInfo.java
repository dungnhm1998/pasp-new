package asia.leadsgen.pasp.model.dto.external.paypal;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PayerInfo {
	@JsonProperty(value = AppParams.BILLING_ADDRESS)
	ShippingAddress shippingAddress;
	@JsonProperty(value = AppParams.BIRTH_DATE)
	String birthDate;
	@JsonProperty(value = AppParams.EMAIL)
	String email;
	@JsonProperty(value = AppParams.FIRST_NAME)
	String first_name;
	@JsonProperty(value = AppParams.LAST_NAME)
	String lastName;
	@JsonProperty(value = AppParams.MIDDLE_NAME)
	String middleName;
	@JsonProperty(value = AppParams.PAYER_ID)
	String payerId;
	@JsonProperty(value = AppParams.SALUTATION)
	String salutation;
	@JsonProperty(value = AppParams.SUFFIX)
	String suffix;
	@JsonProperty(value = AppParams.TAX_ID)
	String taxId;
	@JsonProperty(value = AppParams.TAX_ID_TYPE)
	String taxIdType;
}
