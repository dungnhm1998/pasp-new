package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.external.PaypalApiConnector;
import asia.leadsgen.pasp.data.access.external.PaypalProApiConnector;
import asia.leadsgen.pasp.data.access.repository.PaymentRepository;
import asia.leadsgen.pasp.entity.Payment;
import asia.leadsgen.pasp.error.SystemCode;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.external.paypal.PayerInfo;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreatePaymentExecuteUrlRequest;
import asia.leadsgen.pasp.model.dto.external.paypal.PaypalCreatePaymentExecuteUrlResponse;
import asia.leadsgen.pasp.model.dto.external.paypal.Reason;
import asia.leadsgen.pasp.model.dto.external.paypal.RelatedResources;
import asia.leadsgen.pasp.model.dto.external.paypal.Sale;
import asia.leadsgen.pasp.model.dto.external.paypal.ShippingAddress;
import asia.leadsgen.pasp.model.dto.external.paypal.Transaction;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.Capture;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PaypalProCreateOrderResponse;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PurchaseUnit;
import asia.leadsgen.pasp.model.dto.external.paypal_pro.PurchaseUnitPayment;
import asia.leadsgen.pasp.model.dto.payment.refund.PaymentRefundResponse;
import asia.leadsgen.pasp.model.dto.payment_execute.PaymentExecuteRequest;
import asia.leadsgen.pasp.model.dto.payment_execute.PaymentExecuteResponse;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.AppUtil;
import asia.leadsgen.pasp.util.ResourceStates;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Log4j2
public class PaymentExecuteService {

	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	PaypalApiConnector paypalApiConnector;
	@Autowired
	PaypalProApiConnector paypalProApiConnector;

	SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);

	public ResponseData<PaymentExecuteResponse> paymentExecute(PaymentExecuteRequest paymentExeRq) {

		ResponseData<PaymentExecuteResponse> responseData = new ResponseData<>();
		PaymentExecuteResponse paymentExecuteResponse = new PaymentExecuteResponse();

		String id = paymentExeRq.getId();
		String method = paymentExeRq.getMethod();
		String payerId = paymentExeRq.getPayerId();
		String payId = paymentExeRq.getPayId();
		String tokenId = paymentExeRq.getTokenId();
		if (!AppParams.PAYPAL_PRO.equals(method) && (StringUtils.isEmpty(payerId) || StringUtils.isEmpty(tokenId) || StringUtils.isEmpty(payId))) {
			errorResponse(responseData, paymentExeRq, SystemCode.PAYMENT_ERROR);
		} else {
			Payment paymentDB = paymentRepository.getById(id);
			String state = paymentDB.getState();
			if (ResourceStates.CREATED.equals(state)) {
				String accessToken = paymentDB.getAccessToken();
				switch (method) {
					case AppParams.PAYPAL_PRO:
						paypalProExecutePayment(responseData, paymentExeRq, accessToken);
					default:
						paypalExecutePayment(responseData, paymentExeRq, accessToken);
				}
			} else if (ResourceStates.APPROVED.equals(state)) {

				paymentExecuteResponse.setId(paymentDB.getId());
				paymentExecuteResponse.setPayId(paymentDB.getPayId());
				paymentExecuteResponse.setReference(paymentDB.getReference());
				paymentExecuteResponse.setAmount(paymentDB.getAmount());
				paymentExecuteResponse.setCurrency(paymentDB.getCurrency());
				paymentExecuteResponse.setMethod(paymentDB.getMethod());
				Payer payer = new Payer();
				payer.setPayerId(payerId);
				paymentExecuteResponse.setPayer(payer);
				paymentExecuteResponse.setCreateTime(dateFormat.format(paymentDB.getCreateDate()));
				paymentExecuteResponse.setUpdateTime(dateFormat.format(paymentDB.getUpdateDate()));
				paymentExecuteResponse.setState(paymentDB.getState());
				paymentExecuteResponse.setTokenId(tokenId);
				responseData.getCommonData().setResult(paymentExecuteResponse);
			}

		}
		return responseData;
	}

	private void paypalProExecutePayment(ResponseData<PaymentExecuteResponse> responseData, PaymentExecuteRequest paymentExeRq, String accessToken) {
		try {
			PaypalProCreateOrderResponse paypalProOrderCapture = paypalProApiConnector.createOrderCapture(paymentExeRq.getPayId(), accessToken);

			if (HttpResponseStatus.CREATED.code() != paypalProOrderCapture.getResponseCode()) {
				errorResponse(responseData, paymentExeRq, SystemCode.PAYPAL_CREATE_ORDER_FAIL);
			} else {
				String id = paymentExeRq.getId();
				String tokenId = paymentExeRq.getTokenId();
				String payerId = paymentExeRq.getPayerId();
				String state = "", captureId = "";

				List<PurchaseUnit> purchaseUnits = paypalProOrderCapture.getPurchaseUnits();
				if (!CollectionUtils.isEmpty(purchaseUnits)) {
					List<PurchaseUnitPayment> payments = purchaseUnits.get(0).getPayments();
					if (!CollectionUtils.isEmpty(payments)) {
						List<Capture> captures = payments.get(0).getCaptures();
						if (!CollectionUtils.isEmpty(captures)) {
							Capture captures0 = captures.get(0);
							state = captures0.getStatus();
							captureId = captures0.getId();
							log.info("catureId=" + captureId);
						}
					}
				}

				if (ResourceStates.COMPLETED.equalsIgnoreCase(state)) {
					state = ResourceStates.APPROVED;
					payerId = captureId;
				} else {
					state = ResourceStates.FAIL;
				}

				Payment insertPayment = Payment.builder()
						.id(id)
						.payerId(payerId)
						.method(paymentExeRq.getMethod())
						.state(state)
						.data(paypalProOrderCapture.toString())
						.token(tokenId)
						.updateDate(new Date())
						.build();
				paymentRepository.save(insertPayment);//insert into db

				// response
				PaymentExecuteResponse paymentExecuteResponse = responseData.getCommonData().getResult();
				paymentExecuteResponse.setAccountName(paypalProApiConnector.getPaypalProAccountName());
				paymentExecuteResponse.setId(insertPayment.getId());
				paymentExecuteResponse.setPayId(insertPayment.getPayId());
				paymentExecuteResponse.setReference(insertPayment.getReference());
				paymentExecuteResponse.setAmount(insertPayment.getAmount());
				paymentExecuteResponse.setCurrency(insertPayment.getCurrency());
				paymentExecuteResponse.setMethod(insertPayment.getMethod());
				paymentExecuteResponse.setPayer(new Payer());
				paymentExecuteResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
				paymentExecuteResponse.setUpdateTime(dateFormat.format(insertPayment.getUpdateDate()));
				paymentExecuteResponse.setState(insertPayment.getState());
				responseData.getCommonData().setResult(paymentExecuteResponse);
			}

		} catch (Exception e) {
			e.printStackTrace();
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.PAYMENT_ERROR.getCode(), SystemCode.PAYMENT_ERROR.getMessage());
			responseData.getError().add(error);
		}
	}

	private void paypalExecutePayment(ResponseData<PaymentExecuteResponse> responseData, PaymentExecuteRequest paymentExeRq, String accessToken) {
		try {
			PaypalCreatePaymentExecuteUrlRequest executeUrlRequest = paypalApiConnector.createPaymentExecuteUrlRequest(paymentExeRq);
			PaypalCreatePaymentExecuteUrlResponse executeUrl = paypalApiConnector.createPaymentExecuteUrl(accessToken, executeUrlRequest, paymentExeRq);

			if (HttpResponseStatus.CREATED.code() != executeUrl.getResponseCode()) {
				errorResponse(responseData, paymentExeRq, SystemCode.PAYMENT_ERROR);
			}

			String saleId = "";
			String invoiceNumber = "";
			List<Transaction> transactions = executeUrl.getTransactions();
			if (!CollectionUtils.isEmpty(transactions)) {
				List<RelatedResources> relatedResources = transactions.get(0).getRelatedResources();
				if (!CollectionUtils.isEmpty(relatedResources)) {
					Sale sale = relatedResources.get(0).getSale();
					if (!ObjectUtils.isEmpty(sale)) {
						saleId = sale.getId();
					}
				}
				invoiceNumber = transactions.get(0).getInvoiceNumber();
			}

			PaymentExecuteResponse paymentExecuteResponse = responseData.getCommonData().getResult();

			String state = "";
			String tokenId = "";
			Payer payer = new Payer();
			if (ResourceStates.APPROVED.equals(executeUrl.getState())) {
				state = ResourceStates.APPROVED;
				tokenId = paymentExeRq.getTokenId();

				payer.setPayerId(paymentExeRq.getPayerId());
				PaymentExecuteRequest.PaymentInfo paymentRequestInfo = paymentExeRq.getPayment();
				String token = paymentRequestInfo.getToken();
				if (AppParams.PAYPAL_EXPRESS.equals(token)) {
					Payer payerResponse = executeUrl.getPayer();
					PayerInfo payerInfo = payerResponse.getPayerInfo();
					ShippingAddress payerShippingAddress = payerInfo.getShippingAddress();

					ShippingAddress shippingAddress = new ShippingAddress();
					shippingAddress.setName(payerShippingAddress.getRecipientName());
					shippingAddress.setEmail(payerInfo.getEmail());
					shippingAddress.setAddrLine1(payerShippingAddress.getLine1());
					shippingAddress.setCity(payerShippingAddress.getCity());
					shippingAddress.setState(payerShippingAddress.getState());
					shippingAddress.setCountryCode(payerShippingAddress.getCountryCode());
					shippingAddress.setPostalCode(payerShippingAddress.getPostalCode());

					paymentExecuteResponse.setShipping(shippingAddress);
					paymentExecuteResponse.setType(AppParams.PAYPAL_EXPRESS);
				}
			} else {
				state = ResourceStates.FAIL;
				tokenId = "";
			}

			Payment insertPayment = Payment.builder()
					.id(paymentExeRq.getId())
					.state(state)
					.token(paymentExeRq.getTokenId())
					.payerId(paymentExeRq.getPayerId())
					.updateDate(new Date())
					.dataClob(paymentExecuteResponse.toString())
					.paypalSaleId(saleId)
					.paypalInvoiceNumber(invoiceNumber)
					.build();
			paymentRepository.save(insertPayment);//insert into db

			paymentExecuteResponse.setAccountName(paypalApiConnector.getPaypalAccountName());
			paymentExecuteResponse.setId(insertPayment.getId());
			paymentExecuteResponse.setPayId(insertPayment.getPayId());
			paymentExecuteResponse.setTokenId(tokenId);
			paymentExecuteResponse.setReference(insertPayment.getReference());
			paymentExecuteResponse.setAmount(insertPayment.getAmount());
			paymentExecuteResponse.setCurrency(insertPayment.getCurrency());
			paymentExecuteResponse.setMethod(insertPayment.getMethod());
			paymentExecuteResponse.setPayer(payer);
			paymentExecuteResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			paymentExecuteResponse.setUpdateTime(dateFormat.format(insertPayment.getUpdateDate()));
			paymentExecuteResponse.setState(insertPayment.getState());
			paymentExecuteResponse.setSaleId(saleId);
			paymentExecuteResponse.setInvoiceNumber(invoiceNumber);
			responseData.getCommonData().setResult(paymentExecuteResponse);

		} catch (IOException e) {
			e.printStackTrace();
			responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
			ResponseData.Error error = new ResponseData.Error(SystemCode.PAYMENT_ERROR.getCode(), SystemCode.PAYMENT_ERROR.getMessage());
			responseData.getError().add(error);
		}
	}

	private void errorResponse(ResponseData<PaymentExecuteResponse> responseData, PaymentExecuteRequest paymentExeRq, SystemCode systemCode) {

		Payment insertPayment = Payment.builder()
				.id(AppUtil.genRandomId())
				.amount(paymentExeRq.getAmount())
				.currency(paymentExeRq.getCurrency())
				.reference(paymentExeRq.getReference())
				.method(paymentExeRq.getMethod())
				.state(ResourceStates.FAIL)
				.data(paymentExeRq.toString())
				.createDate(new Date())
				.build();
		paymentRepository.save(insertPayment);//insert into db

		Reason reason = new Reason();
		reason.setMessage(systemCode.getMessage());

		// response
		PaymentExecuteResponse paymentExecuteResponse = responseData.getCommonData().getResult();
		paymentExecuteResponse.setId(insertPayment.getId());
		paymentExecuteResponse.setPayId(insertPayment.getPayId());
		paymentExecuteResponse.setReference(insertPayment.getReference());
		paymentExecuteResponse.setAmount(insertPayment.getAmount());
		paymentExecuteResponse.setCurrency(insertPayment.getCurrency());
		paymentExecuteResponse.setMethod(insertPayment.getMethod());
		paymentExecuteResponse.setPayer(new Payer());
		paymentExecuteResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
		paymentExecuteResponse.setUpdateTime(null);
		paymentExecuteResponse.setState(ResourceStates.FAIL);
		paymentExecuteResponse.setReason(reason);
		paymentExecuteResponse.setLinks(new ArrayList<>());
		paymentExecuteResponse.setTokenId("");
		responseData.getCommonData().setResult(paymentExecuteResponse);

		responseData.setCode(SystemCode.RESPONSE_BAD_REQUEST.getCode());
		ResponseData.Error error = new ResponseData.Error(systemCode.getCode(), systemCode.getMessage());
		responseData.getError().add(error);
	}
}
