package asia.leadsgen.pasp.model.exception;

import asia.leadsgen.pasp.error.SystemCode;

public class EtsyException extends SystemException {
	public EtsyException(SystemCode error) {
		super(error);
	}
}
