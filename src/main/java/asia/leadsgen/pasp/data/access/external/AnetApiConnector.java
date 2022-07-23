package asia.leadsgen.pasp.data.access.external;

import asia.leadsgen.pasp.entity.PaymentAccount;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import net.authorize.Environment;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.CustomerAddressType;
import net.authorize.api.contract.v1.CustomerDataType;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.base.ApiOperationBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Log4j2
@Component
@Data
public class AnetApiConnector {
	@Value("${anet.service.api.key}")
	String apiKey;
	@Value("${anet.service.transaction.key}")
	String transactionKey;
	@Value("${anet.service.launchMod}")
	String launchMod;
	@Value("${anet.service.account_name}")
	String anetAccountName;

	public CreateTransactionResponse chargeCreditCard(CreditCardType card, CustomerDataType customer,
													  CustomerAddressType shipTo, CustomerAddressType billTo, double amount, String ip) {

		// Set the request to operate in either the sandbox or production environment
		ApiOperationBase.setEnvironment(
				"PRODUCTION".equalsIgnoreCase(launchMod) ? Environment.PRODUCTION:Environment.SANDBOX);
		// Create object with merchant authentication details
		MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
		merchantAuthenticationType.setName(apiKey);
		merchantAuthenticationType.setTransactionKey(transactionKey);

		// Populate the payment data
		PaymentType paymentType = new PaymentType();
		paymentType.setCreditCard(card);

		// Set email address (optional)

		TransactionRequestType txnRequest = new TransactionRequestType();
		txnRequest.setCustomerIP(ip);
		txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
		txnRequest.setPayment(paymentType);

		if (customer != null) {
			txnRequest.setCustomer(customer);
		}

		if (billTo != null) {
			txnRequest.setBillTo(billTo);
		}
		if (shipTo != null) {
			txnRequest.setShipTo(shipTo);
		}

		txnRequest.setAmount(new BigDecimal(amount).setScale(2, RoundingMode.CEILING));

		// Create the API request and set the parameters for this specific request
		CreateTransactionRequest apiRequest = new CreateTransactionRequest();
		apiRequest.setMerchantAuthentication(merchantAuthenticationType);
		apiRequest.setTransactionRequest(txnRequest);

		// Call the controller
		CreateTransactionController controller = new CreateTransactionController(apiRequest);
		controller.execute();

		// Get the response
		CreateTransactionResponse response = new CreateTransactionResponse();
		response = controller.getApiResponse();

		return response;
	}

	public CreateTransactionResponse refundTransaction(String transactionId, String transactionAmount, String last4, String expireDate, PaymentAccount account) {
		ApiOperationBase.setEnvironment("PRODUCTION".equalsIgnoreCase(launchMod) ? Environment.PRODUCTION : Environment.SANDBOX);

		MerchantAuthenticationType merchantAuthenticationType = new MerchantAuthenticationType();
		String xapiKey = account == null ? apiKey : account.getAnetApiKey();
		String xtransactionKey = account == null ? transactionKey : account.getAnetTransactionKey();
		merchantAuthenticationType.setName(xapiKey);
		merchantAuthenticationType.setTransactionKey(xtransactionKey);
		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

		// Create a payment object, last 4 of the credit card and expiration date are
		// required
		PaymentType paymentType = new PaymentType();
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber(last4);
		creditCard.setExpirationDate(expireDate);
		paymentType.setCreditCard(creditCard);

		// Create the payment transaction request
		TransactionRequestType txnRequest = new TransactionRequestType();
		txnRequest.setTransactionType(TransactionTypeEnum.REFUND_TRANSACTION.value());
		txnRequest.setRefTransId(transactionId);
		txnRequest.setAmount(new BigDecimal(transactionAmount.toString()));
		txnRequest.setPayment(paymentType);

		// Make the API Request
		CreateTransactionRequest apiRequest = new CreateTransactionRequest();
		apiRequest.setTransactionRequest(txnRequest);
		CreateTransactionController controller = new CreateTransactionController(apiRequest);
		controller.execute();

		CreateTransactionResponse response = controller.getApiResponse();
		return response;
	}
}
