package asia.leadsgen.pasp.error;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by DungNHM on 05-07-2022.
 */
@Setter
@Getter
@NoArgsConstructor
public class SystemCode {

	private int code;
	private String reason;
	private String name;
	private String message;
	private String details;
	private String informationLink;

	public static final SystemCode RESPONSE_OK = new SystemCode(HttpResponseStatus.OK.code(), HttpResponseStatus.OK.reasonPhrase(), "");
	public static final SystemCode RESPONSE_CREATED = new SystemCode(HttpResponseStatus.CREATED.code(), HttpResponseStatus.CREATED.reasonPhrase(), "");
	public static final SystemCode RESPONSE_BAD_REQUEST = new SystemCode(HttpResponseStatus.BAD_REQUEST.code(), HttpResponseStatus.BAD_REQUEST.reasonPhrase(), "");
	public static final SystemCode INVALID_HEADER_X_DATE = new SystemCode(400001, "Request header X_DATE not found", "");
	public static final SystemCode INVALID_TOKEN = new SystemCode(401001, "Invalid token", "");
	public static final SystemCode UNAUTHORIZED = new SystemCode(HttpResponseStatus.UNAUTHORIZED.code(), HttpResponseStatus.UNAUTHORIZED.reasonPhrase(), "");
	public static final SystemCode OPERATION_NOT_PERMITTED = new SystemCode(401002, "Operation not permitted", "");
	public static final SystemCode PAYMENT_ERROR = new SystemCode(400002, "Payment error", "");
	public static final SystemCode INVALID_ENCRYPTION = new SystemCode(400002, "Invalid encryption", "");
	public static final SystemCode INLVAID_PAYMENT_TRANSACTION = new SystemCode(400002, "Inlvaid payment transaction", "");
	public static final SystemCode PAYMENT_CREATE_INVOICE_FAILED = new SystemCode(400002, "Payment create invoice failed", "");
	public static final SystemCode INLVAID_CLIENT_INFO = new SystemCode(400002, "Inlvaid client info", "");
	public static final SystemCode INLVAID_CARD_INFO = new SystemCode(400002, "Inlvaid card info", "");
	public static final SystemCode INVALID_REFUND_INFO = new SystemCode(400002, "Inlvaid refund info", "");
	public static final SystemCode PAYPAL_CREATE_ORDER_FAIL  = new SystemCode(400002, "Paypal create order fail", "");
	public static final SystemCode INVALID_ACCESS_TOKEN = new SystemCode(401001, "Invalid access token", "");

	public SystemCode(int code, String message, String details) {
		this.code = code;
		this.message = message;
		this.details = details;
	}

}
