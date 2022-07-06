package asia.leadsgen.pasp.model.exception;


import asia.leadsgen.pasp.error.SystemError;

/**
 * Created by DungNHM on 05-07-2022.
 *
 * What can you do
 */
public class AuthorizationException extends SystemException {

	public AuthorizationException(SystemError error) {
		super(error);
	}
}
