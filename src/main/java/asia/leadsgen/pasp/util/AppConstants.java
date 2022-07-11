package asia.leadsgen.pasp.util;

import okhttp3.MediaType;

import java.util.TimeZone;

public class AppConstants {
	public static final String SHOPIFY_DOMAIN = "myshopify.com";
	public static final String WOOCOMMERCE = "woocommerce";
	public static final String ETSY = "etsy";
	public static final String SHOPBASE = "shopbase";

	public static final String SHOPIFY = "shopify";
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

	public static final String DEFAULT_INVOICE_TEMPLATE_CODE = "pdf_template_invoice";

	public static final int SHOPIFY_MAX_VARIANTS = 100;
	public static final String PARTNER_NOTIFY_SHOPIFY = "partner_notify_shopify";
	public static final String X_CUSTOMER_EMAIL = "X-CUSTOMER-EMAIL";

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

}