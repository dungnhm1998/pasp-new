package asia.leadsgen.pasp.model.exception;


import asia.leadsgen.pasp.error.SystemError;

/**
 * Created by HungDX on 22-Jan-16.
 */
public class SessionExpiredException extends SystemException {

	public SessionExpiredException(SystemError error) {
		super(error);
	}
}
