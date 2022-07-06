package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.payment.PaymentRequest;
import asia.leadsgen.pasp.model.dto.payment.PaymentResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Log4j2
public class PaymentService {
	public ResponseData<PaymentResponse> paymentCharge(String userId, PaymentRequest requestBody) {
		return null;
	}
}
