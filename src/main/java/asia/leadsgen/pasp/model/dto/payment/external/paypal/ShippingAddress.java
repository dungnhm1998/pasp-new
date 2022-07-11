package asia.leadsgen.pasp.model.dto.payment.external.paypal;

import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.AddressDetail;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShippingAddress {
	@JsonProperty(value = AppParams.RECIPIENT_NAME)//paypal
	public String recipientName;
	@JsonProperty(value = AppParams.LINE1)//paypal
	String line1;
	@JsonProperty(value = AppParams.LINE2)//paypal
	String line2;
	@JsonProperty(value = AppParams.CITY)//paypal
	String city;
	@JsonProperty(value = AppParams.COUNTRY_CODE)//paypal
	String countryCode;
	@JsonProperty(value = AppParams.POSTAL_CODE)//paypal
	String postalCode;
	@JsonProperty(value = AppParams.STATE)	//paypal + paypal pro
	String state;
	@JsonProperty(value = AppParams.PHONE)//paypal
	String phone;

	@JsonProperty(value = AppParams.ADDRESS_LINE_1)//paypal pro
	String addressLine1;
	@JsonProperty(value = AppParams.ADDRESS_LINE_2)//paypal pro
	String addressLine2;
	@JsonProperty(value = AppParams.ADDRESS_LINE_3)//paypal pro
	String addressLine3;
	@JsonProperty(value = AppParams.ADMIN_AREA_1)//paypal pro
	String adminArea1;
	@JsonProperty(value = AppParams.ADMIN_AREA_2)//paypal pro
	String adminArea2;
	@JsonProperty(value = AppParams.ADMIN_AREA_3)//paypal pro
	String adminArea3;
	@JsonProperty(value = AppParams.ADMIN_AREA_4)//paypal pro
	String adminArea4;
	//

	@JsonProperty(value = AppParams.ADDRESS_DETAILS)//paypal pro
	AddressDetail addressDetail;
}
