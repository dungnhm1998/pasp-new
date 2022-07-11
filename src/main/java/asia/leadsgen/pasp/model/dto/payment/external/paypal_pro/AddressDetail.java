package asia.leadsgen.pasp.model.dto.payment.external.paypal_pro;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AddressDetail {
	@JsonProperty(value = AppParams.BUILDING_NAME)
	Money buildingName;
	@JsonProperty(value = AppParams.DELIVERY_SERVICE)
	Money deliveryService;
	@JsonProperty(value = AppParams.STREET_NAME)
	Money streetName;
	@JsonProperty(value = AppParams.STREET_TYPE)
	Money streetType;
	@JsonProperty(value = AppParams.SUB_BUILDING)
	Money subBuilding;
}
