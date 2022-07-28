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
@Entity(name = DBParams.TB_USER)
public class User {
	@Id
	@Column(name = DBParams.S_ID)
	private String id;
	@Column(name = DBParams.S_EMAIL)
	private String email;
	@Column(name = DBParams.S_REFERER)
	private String referer;
	@Column(name = DBParams.S_NAME)
	private String name;
	@Column(name = DBParams.S_STATE)
	private String state;
	@Column(name = DBParams.S_AVATAR)
	private String avatar;
	@Column(name = DBParams.S_PASSWORD)
	private String password;
	@Column(name = DBParams.S_SOURCE)
	private String source;
	@Column(name = DBParams.D_CREATE)
	private Date createDate;
	@Column(name = DBParams.D_UPDATE)
	private Date updateDate;
	@Column(name = DBParams.S_MOBILE)
	private String mobile;
	@Column(name = DBParams.S_ADDR_LINE1)
	private String addrLine1;
	@Column(name = DBParams.S_ADDR_LINE2)
	private String addrLine2;
	@Column(name = DBParams.S_ADDR_CITY)
	private String addrCity;
	@Column(name = DBParams.S_ADDR_STATE)
	private String addrState;
	@Column(name = DBParams.S_ADDR_POS_CODE)
	private String addrPosCode;
	@Column(name = DBParams.S_ADDR_COUNTRY)
	private String addrCountry;
	@Column(name = DBParams.S_LANGUAGE_ID)
	private String languageId;
	@Column(name = DBParams.S_TIMEZONE)
	private String timezone;
	@Column(name = DBParams.S_PARTNER_ID)
	private String partnerId;
	@Column(name = DBParams.S_NOTE)
	private String note;
	@Column(name = DBParams.S_WEBSITE)
	private String website;
	@Column(name = DBParams.N_VERIFY)
	private Integer nVerify;
	@Column(name = DBParams.N_2STEP_VERIFICATION)
	private Integer n2stepVerification;
	@Column(name = DBParams.S_INTERNATIONAL_CALLING_CODE)
	private String internationalCallingCode;
	@Column(name = DBParams.S_AFF_ID)
	private String affId;
	@Column(name = DBParams.N_OWNER)
	private Integer nOwner;
	@Column(name = DBParams.S_LEGAL_NAME)
	private String legalName;
	@Column(name = DBParams.S_TIMEZONE_ID)
	private String timezoneId;
	@Column(name = DBParams.S_AFF_DEFAULT_PAGE)
	private String affDefaultPage;
	@Column(name = DBParams.S_REF_CODE)
	private String refCode;
	@Column(name = DBParams.N_DISPLAY)
	private Integer nDisplay;

}
