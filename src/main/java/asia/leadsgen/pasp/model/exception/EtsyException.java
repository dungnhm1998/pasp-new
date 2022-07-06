package asia.leadsgen.pasp.model.exception;

import asia.leadsgen.pasp.error.SystemError;

public class EtsyException extends SystemException {
	public EtsyException(SystemError error) {
		super(error);
	}
}
