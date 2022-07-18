package asia.leadsgen.pasp.model.dto.payment;

import asia.leadsgen.pasp.model.dto.common.CommonResponse;
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
	private String method;
	@JsonProperty(value = AppParams.TOKEN_ID)
	private String tokenId;
	@JsonProperty(value = AppParams.AMOUNT)
	private String amount;
	@JsonProperty(value = AppParams.CURRENCY)
	private String currency;
	@JsonProperty(value = AppParams.REFERENCE)
	private String reference;
	@JsonProperty(value = AppParams.MESSAGE)
	private String message;
	@JsonProperty(value = AppParams.DATA)
	private List<CommonResponse.Data> data;

	@JsonProperty(value = AppParams.DOMAIN)
	private String domain;
	@JsonProperty(value = AppParams.ITEMS)
	private List<ItemRequest> items;
	@JsonProperty(value = AppParams.DISCOUNT)
	private String discount;
	@JsonProperty(value = AppParams.TYPE)
	private String type;
	@JsonProperty(value = AppParams.SHIPPING_ADDRESS)
	private ShippingAddress shippingAddress;

	//paypal pro
	@JsonProperty(value = AppParams.SHIPPING)
	private Shipping shipping;
	@JsonProperty(value = AppParams.BILLING)
	private Shipping billing;
	@JsonProperty(value = AppParams.ORDER_ID)
	private String orderId;

	//anet
	@JsonProperty(value = AppParams.IP)
	private String ip;

	@Data
	public static class ItemRequest {
		@JsonProperty(value = AppParams.CURRENCY)
		private String currency;
		@JsonProperty(value = AppParams.VARIANT_NAME)
		private String variantName;
		@JsonProperty(value = AppParams.SIZE_NAME)
		private String sizeName;
		@JsonProperty(value = AppParams.QUANTITY)
		private String quantity;
		@JsonProperty(value = AppParams.PRICE)
		private String price;
		@JsonProperty(value = AppParams.SHIPPING_FEE)
		private String shippingFee;
		@JsonProperty(value = AppParams.PR_TYPE)
		private String prType;
		@JsonProperty(value = AppParams.TAX_AMOUNT)
		private String taxAmount;
	}


}
