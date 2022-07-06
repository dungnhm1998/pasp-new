package asia.leadsgen.pasp.model.exception;


import asia.leadsgen.pasp.error.SystemError;

/**
 * Created by DungNHM on 05-07-2022.
 *
 * Who are you
 */
public class AuthenticationException extends SystemException {

	public AuthenticationException(SystemError error) {
		super(error);
	}
}
