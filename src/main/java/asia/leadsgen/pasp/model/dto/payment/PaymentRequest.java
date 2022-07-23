package asia.leadsgen.pasp.model.dto.payment;

import asia.leadsgen.pasp.model.dto.common.JsonInterface;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.model.dto.payment.external.paypal_pro.Shipping;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.JsonNullToEmptySerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.List;

@Data
public class PaymentRequest  implements JsonInterface {

	@JsonProperty(value = AppParams.METHOD)
	@JsonSerialize(nullsUsing = JsonNullToEmptySerializer.class)
	String method;
	@JsonProperty(value = AppParams.TOKEN_ID)
	String tokenId;
	@JsonProperty(value = AppParams.AMOUNT)
	String amount;
	@JsonProperty(value = AppParams.CURRENCY)
	String currency;
	@JsonProperty(value = AppParams.REFERENCE)
	String reference;
	@JsonProperty(value = AppParams.MESSAGE)
	String message;
//	@JsonProperty(value = AppParams.DATA)
//	List<CommonResponse.Data> data;

	@JsonProperty(value = AppParams.DOMAIN)
	String domain;
	@JsonProperty(value = AppParams.ITEMS)
	List<ItemRequest> items;
	@JsonProperty(value = AppParams.DISCOUNT)
	String discount;
	@JsonProperty(value = AppParams.TYPE)
	String type;
	@JsonProperty(value = AppParams.SHIPPING_ADDRESS)
	ShippingAddress shippingAddress;

	//paypal pro
	@JsonProperty(value = AppParams.SHIPPING)
	Shipping shipping;
	@JsonProperty(value = AppParams.BILLING)
	Shipping billing;
	@JsonProperty(value = AppParams.ORDER_ID)
	String orderId;

	//anet
	@JsonProperty(value = AppParams.IP)
	String ip;

	@Data
	public static class ItemRequest {
		@JsonProperty(value = AppParams.CURRENCY)
		String currency;
		@JsonProperty(value = AppParams.VARIANT_NAME)
		String variantName;
		@JsonProperty(value = AppParams.SIZE_NAME)
		String sizeName;
		@JsonProperty(value = AppParams.QUANTITY)
		String quantity;
		@JsonProperty(value = AppParams.PRICE)
		String price;
		@JsonProperty(value = AppParams.SHIPPING_FEE)
		String shippingFee;
		@JsonProperty(value = AppParams.PR_TYPE)
		String prType;
		@JsonProperty(value = AppParams.TAX_AMOUNT)
		String taxAmount;
	}


}
