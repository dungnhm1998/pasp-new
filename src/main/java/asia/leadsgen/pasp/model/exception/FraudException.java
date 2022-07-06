package asia.leadsgen.pasp.model.exception;


import asia.leadsgen.pasp.error.SystemError;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by HungDX on 22-Jan-16.
 */
public class FraudException extends SystemException {

	public FraudException(SystemError error) {
		super(error);
	}

	public FraudException(String fraudInformations) {

		super(new SystemError(HttpResponseStatus.FORBIDDEN.code(), HttpResponseStatus.FORBIDDEN.reasonPhrase(), fraudInformations));
	}
}
