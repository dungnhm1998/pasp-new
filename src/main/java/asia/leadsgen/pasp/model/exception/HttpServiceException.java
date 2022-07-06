package asia.leadsgen.pasp.model.exception;

import asia.leadsgen.pasp.error.SystemError;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.ParamUtil;

import java.util.Map;

/**
 * Created by HungDX on 22-Jan-16.
 */
public class HttpServiceException extends SystemException {

	public HttpServiceException(int code, String reason, Map informationMap) {

		super(new SystemError(code, reason, ParamUtil.getString(informationMap, AppParams.DETAILS)));
	}
}
