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
public class SystemError {

	private int code;
	private String reason;
	private String name;
	private String message;
	private String details;
	private String informationLink;

	public static final SystemError RESPONSE_OK = new SystemError(HttpResponseStatus.OK.code(), HttpResponseStatus.OK.reasonPhrase(), "");
	public static final SystemError INVALID_HEADER_X_DATE = new SystemError(400001, "Request header X_DATE not found", "");
	public static final SystemError INVALID_TOKEN = new SystemError(401001, "Invalid token", "");
	public static final SystemError UNAUTHORIZED = new SystemError(HttpResponseStatus.UNAUTHORIZED.code(), HttpResponseStatus.UNAUTHORIZED.reasonPhrase(), "");
	public static final SystemError OPERATION_NOT_PERMITTED = new SystemError(401002, "Operation not permitted", "");
	public static final SystemError PAYMENT_ERROR = new SystemError(400002, "Payment error", "");

	public SystemError(int code, String message, String details) {
		this.code = code;
		this.message = message;
		this.details = details;
	}

}
