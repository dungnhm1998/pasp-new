package asia.leadsgen.pasp.model.exception;


import asia.leadsgen.pasp.error.SystemError;

/**
 * Created by HungDX on 22-Jan-16.
 */
public class LoginException extends SystemException {

	public LoginException(SystemError error) {
		super(error);
	}
}
