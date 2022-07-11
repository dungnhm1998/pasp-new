package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaypalAppContext {
	@JsonProperty(value = AppParams.BRAND_NAME)
	Money brandName;
	@JsonProperty(value = AppParams.CANCEL_URL)
	Money cancel_url;
	@JsonProperty(value = AppParams.LANDING_PAGE)
	Money landing_page;
	@JsonProperty(value = AppParams.LOCALE)
	Money locale;
	@JsonProperty(value = AppParams.PAYMENT_METHOD)
	PaymentMethod payment_method;
	@JsonProperty(value = AppParams.RETURN_URL)
	Money returnUrl;
	@JsonProperty(value = AppParams.SHIPPING_PREFERENCE)
	Money shippingPreference;
	@JsonProperty(value = AppParams.STORED_PAYMENT_SOURCE)
	StoredPaymentSource storedPaymentSource;
	@JsonProperty(value = AppParams.USER_ACTION)
	Money userAction;
}
