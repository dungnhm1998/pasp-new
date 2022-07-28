package asia.leadsgen.pasp.service;

import asia.leadsgen.pasp.model.base.BurgerContext;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.affiliate.AddCardRequest;
import asia.leadsgen.pasp.model.dto.affiliate.AddCardResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Log4j2
public class AffiliateService {

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

		return ResponseData.ok(addCardResponse);
	}

	private ResponseData<AddCardResponse> chargeAndSaveCard(AddCardResponse addCardResponse, String affuserId, AddCardRequest addCardRequest, String email) {
		//todo
		return ResponseData.ok(addCardResponse);
	}
}
