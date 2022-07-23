package asia.leadsgen.pasp.model.dto.payment_execute;

import asia.leadsgen.pasp.model.dto.payment.external.paypal.Link;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Reason;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentExecuteResponse {

//	@JsonProperty(value = AppParams.DATA)
//	PaymentExecuteDto commonData;
//
//	@lombok.Data
//	@AllArgsConstructor
//	@NoArgsConstructor
//	public static class CommonData {
//		@JsonProperty(value = AppParams.TOTAL)
//		@JsonInclude(value = JsonInclude.Include.NON_NULL)
//		int total;
//		@JsonProperty(value = AppParams.PAGE)
//		@JsonInclude(value = JsonInclude.Include.NON_NULL)
//		int page;
//		@JsonProperty(value = AppParams.PAGE_SIZE)
//		@JsonInclude(value = JsonInclude.Include.NON_NULL)
//		int pageSize;
//		@JsonProperty(value = AppParams.RESULT)
//		PaymentExecuteDto result;
//	}

//	@lombok.Data
//	@AllArgsConstructor
//	@NoArgsConstructor
//	public static class PaymentExecuteDto {
		@JsonProperty(value = AppParams.ID)
		String id;
		@JsonProperty(value = AppParams.PAY_ID)
		String payId;
		@JsonProperty(value = AppParams.TOKEN_ID)
		String tokenId;
		@JsonProperty(value = AppParams.REFERENCE)
		String reference;
		@JsonProperty(value = AppParams.AMOUNT)
		String amount;
		@JsonProperty(value = AppParams.CURRENCY)
		String currency;
		@JsonProperty(value = AppParams.METHOD)
		String method;
		@JsonProperty(value = AppParams.CREATE_TIME)
		String createTime;
		@JsonProperty(value = AppParams.UPDATE_TIME)
		String updateTime;
		@JsonProperty(value = AppParams.STATE)
		String state;
		@JsonProperty(value = AppParams.REASON)
		Reason reason;
		@JsonProperty(value = AppParams.PAYER)
		Payer payer;
		@JsonProperty(value = AppParams.LINKS)
		ArrayList<Link> links;
		@JsonProperty(value = AppParams.ACCOUNT_NAME)
		String accountName;
		@JsonProperty(value = AppParams.SALE_ID)
		String saleId;
		@JsonProperty(value = AppParams.INVOICE_NUMBER)
		String invoiceNumber;
		@JsonProperty(value = AppParams.TYPE)
		String type;

		@JsonProperty(value = AppParams.SHIPPING)
		ShippingAddress shipping;
//	}
}
