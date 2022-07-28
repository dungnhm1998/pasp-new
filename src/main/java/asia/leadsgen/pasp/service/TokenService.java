package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.external.BraintreeApiConnector;
import asia.leadsgen.pasp.data.access.external.PaypalProApiConnector;
import asia.leadsgen.pasp.error.SystemError;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalAccessTokenResponse;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PaypalProAuth2TokenResponse;
import asia.leadsgen.pasp.model.dto.payment.token.BraintreeTokenResponse;
import asia.leadsgen.pasp.model.dto.payment.token.PaypalProTokenResponse;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import com.braintreegateway.BraintreeGateway;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Service
@Transactional
@Log4j2
public class TokenService {

	@Autowired
	BraintreeApiConnector braintreeApiConnector;
	@Autowired
	PaypalProApiConnector paypalProApiConnector;

	SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);

	public ResponseData<BraintreeTokenResponse> getTokenBraintree(String method) {
		BraintreeTokenResponse braintreeTokenResponse = new BraintreeTokenResponse();
		if (AppParams.BRAINTREE.matches(method)) {
			BraintreeGateway braintreeGateway = braintreeApiConnector.getToken();
			if (braintreeGateway!= null){
				braintreeTokenResponse.setToken(braintreeGateway.clientToken().generate());
			}
		}

		return ResponseData.ok(braintreeTokenResponse);
	}

	public ResponseData<PaypalProTokenResponse> getTokenClientPaypal() {
		PaypalProTokenResponse paypalProTokenResponse= new PaypalProTokenResponse();

		try {
			PaypalAccessTokenResponse accessTokenResponse = paypalProApiConnector.createAccessToken();

			String accessToken = accessTokenResponse.getAccessToken();
			if (StringUtils.isEmpty(accessToken)) {
				paypalProTokenResponse.setError("error while generate server access token");
			}else{
				PaypalProAuth2TokenResponse auth2Token = paypalProApiConnector.generateClientToken(accessToken);
				if (auth2Token.getResponseCode() == 200){
					paypalProTokenResponse.setScope(auth2Token.getScope());
					paypalProTokenResponse.setAccessToken(auth2Token.getAccessToken());
					paypalProTokenResponse.setTokenType(auth2Token.getTokenType());
					paypalProTokenResponse.setAppId(auth2Token.getAppId());
					paypalProTokenResponse.setNonce(auth2Token.getNonce());
					paypalProTokenResponse.setExpiresIn(auth2Token.getExpiresIn());
				}else{
					paypalProTokenResponse.setError(auth2Token.getMessage());
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			ResponseData.failed(SystemError.PAYMENT_ERROR);
		}

		return ResponseData.ok(paypalProTokenResponse);
	}

}
