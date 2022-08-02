package asia.leadsgen.pasp.model.exception;


import asia.leadsgen.pasp.error.SystemCode;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by HungDX on 22-Jan-16.
 */
public class FraudException extends SystemException {

	public FraudException(SystemCode error) {
		super(error);
	}

	public FraudException(String fraudInformations) {

		super(new SystemCode(HttpResponseStatus.FORBIDDEN.code(), HttpResponseStatus.FORBIDDEN.reasonPhrase(), fraudInformations));
	}
}
