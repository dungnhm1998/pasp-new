package asia.leadsgen.pasp.entity;

import asia.leadsgen.pasp.util.DBParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = DBParams.TB_PAYMENT)
public class Payment {
	@Id
	@Column(name = DBParams.S_ID)
	private String id;
	@Column(name = DBParams.S_PAY_ID)
	private String payId;
	@Column(name = DBParams.S_ACCESS_TOKEN)
	private String accessToken;
	@Column(name = DBParams.S_STATE)
	private String state;
	@Column(name = DBParams.S_REFERENCE)
	private String reference;
	@Column(name = DBParams.S_AMOUNT)
	private String amount;
	@Column(name = DBParams.S_CURRENCY)
	private String currency;
	@Column(name = DBParams.S_TOKEN)
	private String token;
	@Column(name = DBParams.S_PAYER_ID)
	private String payerId;
	@Column(name = DBParams.S_DATA)
	private String data;
	@Column(name = DBParams.D_CREATE)
	private Date createDate;
	@Column(name = DBParams.D_UPDATE)
	private Date updateDate;
	@Column(name = DBParams.S_METHOD)
	private String method;
	@Column(name = DBParams.S_PAYPAL_SALE_ID)
	private String paypalSaleId;
	@Column(name = DBParams.S_PAYPAL_INVOICE_NUMBER)
	private String paypalInvoiceNumber;
	@Column(name = DBParams.S_LAST_4)
	private String last4;
	@Column(name = DBParams.S_EXPIRE)
	private String expire;
	@Column(name = DBParams.S_DATA_CLOB)
	private String dataClob;


}
