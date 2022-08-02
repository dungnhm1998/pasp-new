package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.external.PaypalApiConnector;
import asia.leadsgen.pasp.data.access.repository.PaymentAccountRepository;
import asia.leadsgen.pasp.data.access.repository.PaymentRepository;
import asia.leadsgen.pasp.error.SystemCode;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalAccessTokenResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreateInvoiceRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreateInvoiceResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalSendInvoiceRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalSendInvoiceResponse;
import asia.leadsgen.pasp.model.dto.payment_execute.PaymentExecuteResponse;
import asia.leadsgen.pasp.model.dto.payppal.InvoicePaypalRequest;
import asia.leadsgen.pasp.model.dto.payppal.InvoicePaypalResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
@Transactional
@Log4j2
public class PaypalService {

	@Autowired
	PaypalApiConnector paypalApiConnector;
	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	PaymentAccountRepository paymentAccountRepository;

	public ResponseData<InvoicePaypalResponse> createInvoice(InvoicePaypalRequest invoicePaypalRequest) {
		ResponseData<InvoicePaypalResponse> responseData = new ResponseData<>();

		InvoicePaypalResponse invoicePaypalResponse = responseData.getCommonData().getResult();

		log.info("psp call to pasp and payname:" + paypalApiConnector.getPaypalAccountName());

		try {

			PaypalAccessTokenResponse accessTokenResponse = paypalApiConnector.createAccessToken();

			String accessToken = accessTokenResponse.getAccessToken();
			if (StringUtils.isEmpty(accessToken)) {
				errorResponse(responseData, invoicePaypalRequest, SystemCode.INVALID_ACCESS_TOKEN);
			}
			log.info("accessToken = " + accessToken);

			PaypalCreateInvoiceRequest paypalCreateInvoiceRequest = paypalApiConnector.createInvoiceRequest(invoicePaypalRequest);
			PaypalCreateInvoiceResponse paypalCreateInvoiceResponse = paypalApiConnector.createInvoice(accessToken, paypalCreateInvoiceRequest);

			String invoiceId = paypalCreateInvoiceResponse.getId();
			if (StringUtils.isNotEmpty(invoiceId) && HttpResponseStatus.CREATED.code() != paypalCreateInvoiceResponse.getResponseCode()) {

				PaypalSendInvoiceRequest paypalSendInvoiceRequest = paypalApiConnector.createSendInvoiceRequest(invoicePaypalRequest);
				PaypalSendInvoiceResponse paypalSendInvoiceResponse = paypalApiConnector.sendInvoice(accessToken, invoiceId, paypalSendInvoiceRequest);

				if ((HttpResponseStatus.ACCEPTED.code() == paypalCreateInvoiceResponse.getResponseCode()) || (HttpResponseStatus.OK.code() == paypalCreateInvoiceResponse.getResponseCode()) || (HttpResponseStatus.CREATED.code() == paypalCreateInvoiceResponse.getResponseCode())) {

					String href = "";
					if (CollectionUtils.isNotEmpty(paypalSendInvoiceResponse.getLinks())) {
						href = paypalSendInvoiceResponse.getLinks().get(0).getHref();
					}

					invoicePaypalResponse.setId(invoiceId);
					invoicePaypalResponse.setAccountName(paypalApiConnector.getPaypalAccountName());
					invoicePaypalResponse.setHref(href);
					log.info("pasp invoiceSentHandler:" + invoicePaypalResponse);

					responseData.getCommonData().setResult(invoicePaypalResponse);

				} else {
					errorResponse(responseData, invoicePaypalRequest, SystemCode.PAYMENT_CREATE_INVOICE_FAILED);
				}
			} else {
				errorResponse(responseData, invoicePaypalRequest, SystemCode.PAYMENT_CREATE_INVOICE_FAILED);
			}

		} catch (IOException e) {
			e.printStackTrace();
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.PAYMENT_ERROR.getCode(), SystemCode.PAYMENT_ERROR.getMessage());
			responseData.getError().add(error);
		}
		return responseData;
	}

	private void errorResponse(ResponseData<InvoicePaypalResponse> responseData, InvoicePaypalRequest paymentRequest, SystemCode systemCode) {

		responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
		ResponseData.Error error = new ResponseData.Error(systemCode.getCode(), systemCode.getMessage());
		responseData.getError().add(error);
	}
}
