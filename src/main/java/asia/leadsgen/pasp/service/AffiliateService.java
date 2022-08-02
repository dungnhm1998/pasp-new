package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.data.access.external.StripeApiConnector;
import asia.leadsgen.pasp.error.SystemCode;
import asia.leadsgen.pasp.model.base.BurgerContext;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.affiliate.AddCardRequest;
import asia.leadsgen.pasp.model.dto.affiliate.AddCardResponse;
import asia.leadsgen.pasp.model.dto.affiliate.CreateCustomerRequest;
import asia.leadsgen.pasp.model.dto.affiliate.CreateCustomerResponse;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.ResourceStates;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Log4j2
public class AffiliateService {

	@Autowired
	StripeApiConnector stripeApiConnector;

	public ResponseData<AddCardResponse> addCard(BurgerContext burgerContext, AddCardRequest addCardRequest) {
		AddCardResponse addCardResponse = new AddCardResponse();

		String paymentType = addCardRequest.getType();
		String email = addCardRequest.getEmail();
		log.info("paymentType=" + paymentType);

		String affuserId = SessionLookup.getUserInfoFromContext(burgerContext).getAffId();

		if ("card".equalsIgnoreCase(paymentType)) {
			log.info("add Card");
			return chargeAndSaveCard(addCardResponse, affuserId, addCardRequest, email);
		}

		return ResponseData.build(SystemCode.RESPONSE_CREATED, addCardResponse, null);
	}

	private ResponseData<AddCardResponse> chargeAndSaveCard(AddCardResponse addCardResponse, String affuserId, AddCardRequest addCardRequest, String email) {
		//todo
		return null;
	}

	public ResponseData<CreateCustomerResponse> createCustomer(BurgerContext burgerContext, CreateCustomerRequest addCardRequest) {
		CreateCustomerResponse customerResponse = new CreateCustomerResponse();
		List<ResponseData.Error> errors = new ArrayList<>();
		SystemCode systemCode;

		String token = addCardRequest.getToken();
		String email = addCardRequest.getEmail();

		try {
			Customer customer = stripeApiConnector.saveCustomer(token, email);

			Charge chargeResult = stripeApiConnector.chargeSaveCardInfo(AppConstants.DEFAULT_SAVE_CARD_CHARGE,
					AppConstants.DEFAULT_CURRENCY, customer.getId(), email, token);

			if (ResourceStates.SUCCEEDED.equals(chargeResult.getStatus())) {

				String chargeId = chargeResult.getId();
				stripeApiConnector.createRefund(null, chargeId, AppConstants.DEFAULT_SAVE_CARD_CHARGE);

				Charge.PaymentMethodDetails paymentMethodDetails = chargeResult.getPaymentMethodDetails();
				Charge.PaymentMethodDetails.Card card = paymentMethodDetails.getCard();

				customerResponse.setState(ResourceStates.SUCCEEDED);
				customerResponse.setBrand(card.getBrand());
				customerResponse.setLast4(card.getLast4());
				customerResponse.setExpMonth(card.getExpMonth());
				customerResponse.setExpYear(card.getExpYear());
				customerResponse.setCustomerId(customer.getId());

				systemCode = SystemCode.RESPONSE_CREATED;
			} else {
				customerResponse.setState(ResourceStates.FAIL);
				ResponseData.Error error = new ResponseData.Error(400001, SystemCode.INLVAID_CARD_INFO.getMessage());
				errors.add(error);
				systemCode = SystemCode.RESPONSE_BAD_REQUEST;
			}

		} catch (StripeException e) {
			errorResponse(customerResponse, errors, SystemCode.INLVAID_CLIENT_INFO.getMessage());
			systemCode = SystemCode.RESPONSE_BAD_REQUEST;
		}

		return ResponseData.build(systemCode, customerResponse, errors);
	}

	private void errorResponse(CreateCustomerResponse customerResponse, List<ResponseData.Error> errors, String message) {
		ResponseData.Error error = new ResponseData.Error(400001, message);
		errors.add(error);
		customerResponse.setState(ResourceStates.FAIL);
	}

}


