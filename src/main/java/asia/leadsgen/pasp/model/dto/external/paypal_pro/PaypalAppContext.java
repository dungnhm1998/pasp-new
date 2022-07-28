package asia.leadsgen.pasp.model.dto.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaypalAppContext {
	@JsonProperty(value = AppParams.BRAND_NAME)
	String brandName;
	@JsonProperty(value = AppParams.CANCEL_URL)
	String cancel_url;
	@JsonProperty(value = AppParams.LANDING_PAGE)
	String landingPage;
	@JsonProperty(value = AppParams.LOCALE)
	String locale;
	@JsonProperty(value = AppParams.PAYMENT_METHOD)
	PaymentMethod payment_method;
	@JsonProperty(value = AppParams.RETURN_URL)
	String returnUrl;
	@JsonProperty(value = AppParams.SHIPPING_PREFERENCE)
	String shippingPreference;
	@JsonProperty(value = AppParams.STORED_PAYMENT_SOURCE)
	StoredPaymentSource storedPaymentSource;
	@JsonProperty(value = AppParams.USER_ACTION)
	String userAction;
	@JsonProperty(value = AppParams.PAYMENT_PATTERN)
	String paymentPattern;
	@JsonProperty(value = AppParams.PREFERRED_PAYMENT_SOURCE)
	String preferredPaymentSource;
}
