package asia.leadsgen.pasp.model.exception;

import asia.leadsgen.pasp.error.SystemCode;

/**
 * Created by HungDX on 22-Jan-16.
 */
public class ResourceNotFoundException extends SystemException {

    public ResourceNotFoundException(SystemCode error) {
        super(error);
    }

}
