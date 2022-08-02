package asia.leadsgen.pasp.util;

import okhttp3.MediaType;

import java.util.TimeZone;

public class AppConstants {
	public static final String DEFAULT_TIME_ZONE = "GMT-05:00";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String DEFAULT_CURRENTCY_PATTERN = "###,##0.00";
	public static final String DEFAULT_DATE_TIME_FORMAT_PATTERN = "yyyyMMdd\'T\'HHmmss\'Z\'";
	public static final String DEFAULT_DATE_TIME_FORMAT_PATTERN_WITHOUT_TZ = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_DATE_FORMAT_DD_MM_YY = "dd/MM/yyyy";
	public static final String DD_MM_YY = "dd-MM-yy";
	public static final String YYMMDD = "yyMMdd";
	public static final String YYYYMMDD = "yyyyMMdd";

	public static final String BIMONTHLY = "bi-monthly";
	public static final String MONTHLY = "monthly";
	public static final String WEEKLY = "weekly";

	public static final String BG_CONTEXT = "bg_context";
	public static final String AUTHORIZE_HEADER = "X-Authorization";
	public static final String DEFAULT_TIME_ZONE_ID = "America/New_York";
	public static final String LOCAL_TIME_ZONE = TimeZone.getDefault().getID();

	public static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String AUTHORIZATION = "Authorization";
	public static final String CONTENT_TYPE_APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
	public static final String PAYPAL_PARTNER_ATTRIBUTION_ID = "PayPal-Partner-Attribution-Id";
	public static final String ACCEPT_LANGUAGE = "Accept-Language";
	public static final String EN_US = "en_US";
	public static final String ACCEPT = "Accept";
	public static final String X_GGE4_DATE = "x-gge4-date";
	public static final String GRANT_TYPE = "grant_type";
	public static final String CLIENT_CREDENTIALS = "client_credentials";
	public static final String RESPONSE_TYPE = "response_type";
	public static final String TOKEN = "token";
	public static final String BASIC_ = "Basic ";
	public static final String BEARER_ = "Bearer ";
	public static final String PAYPAL_REQUEST_ID = "PayPal-Request-Id";
	public static final String X_GGE4_CONTENT_SHA1 = "x-gge4-content-sha1";
	public static final String GGE4_API_ = "GGE4_API ";
	public static final String PREFER = "Prefer";
	public static final String RETURN_REPRESENTATION = "return=representation";
	public static final long DEFAULT_SAVE_CARD_CHARGE = 50;
	public static final String DEFAULT_CURRENCY = "USD";

}