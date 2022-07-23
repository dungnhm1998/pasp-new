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
@Entity(name = DBParams.TB_PAYMENT_ACCOUNT)
public class PaymentAccount {
	@Id
	@Column(name = DBParams.S_ID)
	private String id;
	@Column(name = DBParams.S_NAME)
	private String name;
	@Column(name = DBParams.S_TYPE)
	private String type;
	@Column(name = DBParams.S_PP_MERCHANT_ID)
	private String ppMerchantId;
	@Column(name = DBParams.S_PP_CLIENT_ID)
	private String ppClientId;
	@Column(name = DBParams.S_PP_CLIENT_SECRET)
	private String ppClientSecret;
	@Column(name = DBParams.S_STRIPE_API_KEY)
	private String stripeApiKey;
	@Column(name = DBParams.S_ANET_API_KEY)
	private String anetApiKey;
	@Column(name = DBParams.S_ANET_TRANSACTION_KEY)
	private String anetTransactionKey;
	@Column(name = DBParams.S_STATE)
	private String state;
	@Column(name = DBParams.D_CREATE)
	private Date createDate;
	@Column(name = DBParams.D_UPDATE)
	private Date updateDate;
	@Column(name = DBParams.S_PP_BNCODE)
	private String ppBncode;
	@Column(name = DBParams.S_BOA_GW_ID)
	private String boaGwId;
	@Column(name = DBParams.S_BOA_GW_PW)
	private String boaGwPw;
	@Column(name = DBParams.S_BOA_HMAC_KEY)
	private String boaHmacKey;

}
