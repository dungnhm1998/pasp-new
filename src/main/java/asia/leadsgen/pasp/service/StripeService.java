package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.external.StripeApiConnector;
import asia.leadsgen.pasp.data.access.repository.PaymentAccountRepository;
import asia.leadsgen.pasp.data.access.repository.PaymentRepository;
import asia.leadsgen.pasp.entity.Payment;
import asia.leadsgen.pasp.entity.PaymentAccount;
import asia.leadsgen.pasp.error.SystemError;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Payer;
import asia.leadsgen.pasp.model.dto.payment.external.paypal.Reason;
import asia.leadsgen.pasp.model.dto.stripe.BalanceStripeResponse;
import asia.leadsgen.pasp.model.dto.stripe.ChargeStripeRequest;
import asia.leadsgen.pasp.model.dto.stripe.ChargeStripeResponse;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.GetterUtil;
import asia.leadsgen.pasp.util.ResourceStates;
import com.stripe.exception.StripeException;
import com.stripe.model.Balance;
import com.stripe.model.Charge;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@Log4j2
public class StripeService {

	@Autowired
	StripeApiConnector stripeApiConnector;
	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	PaymentAccountRepository paymentAccountRepository;

	SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);

	public ResponseData<ChargeStripeResponse> chargeStripe(ChargeStripeRequest chargeStripeRequest) {
		ChargeStripeResponse chargeResponse = new ChargeStripeResponse();

		try {
			String customerId = chargeStripeRequest.getCustomerId();
			String currency = chargeStripeRequest.getCurrency();
			String reference = chargeStripeRequest.getReference();
			String orderId = chargeStripeRequest.getOrderId();
			String code = chargeStripeRequest.getCode();
			double amountDouble = chargeStripeRequest.getAmount();
			long amount = GetterUtil.getLong(String.format("%.2f", amountDouble).replace(".", ""));
			Charge charge = stripeApiConnector.createCharge(amount, currency, customerId, orderId, code);

			String id = "";
			String state = "";
			if (ResourceStates.SUCCEEDED.equals(charge.getStatus())) {
				state = ResourceStates.APPROVED;
				id = charge.getId();
			} else {
				state = ResourceStates.FAIL;
				Reason reason = new Reason();
				reason.setDetails(charge.toString());
				chargeResponse.setReason(reason);
			}

			Payment insertPayment = Payment.builder()
					.id(id)
					.state(state)
					.accessToken(customerId)
					.reference(reference)
					.amount(String.valueOf(amountDouble))
					.currency(currency)
					.dataClob(charge.toString())
					.method(AppParams.STRIPE)
					.build();
			paymentRepository.save(insertPayment);//insert into db

			chargeResponse.setAccountName(stripeApiConnector.getStripeAccountName());
			chargeResponse.setId(insertPayment.getId());
			chargeResponse.setPayId(id);
			chargeResponse.setCustomerId(customerId);
			chargeResponse.setReference(insertPayment.getReference());
			chargeResponse.setAmount(insertPayment.getAmount());
			chargeResponse.setCurrency(insertPayment.getCurrency());
			chargeResponse.setMethod(insertPayment.getMethod());
			chargeResponse.setPayer(new Payer());
			chargeResponse.setCreateTime(dateFormat.format(insertPayment.getCreateDate()));
			chargeResponse.setState(insertPayment.getState());

		} catch (StripeException e) {
			e.printStackTrace();
			return ResponseData.failed(SystemError.PAYMENT_ERROR);
		}

		return ResponseData.ok(chargeResponse);
	}

	public ResponseData<BalanceStripeResponse> getStripeBalance(String accountName) {
		BalanceStripeResponse balanceStripeResponse = new BalanceStripeResponse();

		try {
			log.info("*** Stripe balance: account_name= " + accountName);
			PaymentAccount account = null;
			double balanceAmount = 0d;

			if (StringUtils.isNotEmpty(accountName)) {
				account = paymentAccountRepository.findByName(accountName).orElse(null);
			}
			if (account != null) {
				Balance balance = stripeApiConnector.getAccountBalance(account);
				List<Balance.Money> moneyList = balance.getAvailable();
				for (Balance.Money m : moneyList) {
					log.info(m.toJson());
				}
				Balance.Money maxBalance = null;
				if (CollectionUtils.isNotEmpty(moneyList)) {
					maxBalance = moneyList.stream().max(Comparator.comparing(Balance.Money::getAmount)).orElse(null);
				}
				log.info("maxBalance=" + maxBalance);
				if (maxBalance != null && StringUtils.isNotEmpty(maxBalance.getCurrency()) && "SGD".equalsIgnoreCase(maxBalance.getCurrency())) {
					balanceAmount = (double) maxBalance.getAmount() / (100d * 1.4);
				} else {
					balanceAmount = (double) maxBalance.getAmount() / 100d;
				}
			}

			balanceStripeResponse.setAccountName(accountName);
			balanceStripeResponse.setBalance(balanceAmount);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseData.failed(SystemError.PAYMENT_ERROR);
		}

		return ResponseData.ok(balanceStripeResponse);
	}
}
