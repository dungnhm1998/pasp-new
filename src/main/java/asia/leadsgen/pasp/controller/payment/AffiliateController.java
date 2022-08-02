package asia.leadsgen.pasp.controller.payment;

import asia.leadsgen.pasp.model.base.BurgerContext;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.affiliate.AddCardRequest;
import asia.leadsgen.pasp.model.dto.affiliate.AddCardResponse;
import asia.leadsgen.pasp.model.dto.affiliate.CreateCustomerRequest;
import asia.leadsgen.pasp.model.dto.affiliate.CreateCustomerResponse;
import asia.leadsgen.pasp.model.dto.stripe.BalanceStripeResponse;
import asia.leadsgen.pasp.model.dto.stripe.ChargeStripeExample;
import asia.leadsgen.pasp.model.dto.stripe.ChargeStripeRequest;
import asia.leadsgen.pasp.model.dto.stripe.ChargeStripeResponse;
import asia.leadsgen.pasp.service.AffiliateService;
import asia.leadsgen.pasp.service.StripeService;
import asia.leadsgen.pasp.util.AppConstants;
import asia.leadsgen.pasp.util.AppParams;
import asia.leadsgen.pasp.util.AppUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@Api(value = "Handler Affiliate")
public class AffiliateController {

	@Autowired
	AffiliateService affiliateService;

	@RequestMapping(method = RequestMethod.POST, path = "/cards")
	public ResponseData<CreateCustomerResponse> createCustomer(
			@RequestHeader(name = AppConstants.AUTHORIZE_HEADER, required = true) @ApiParam(value = "Access Token") String accessToken,
			@RequestAttribute(name = AppConstants.BG_CONTEXT, required = true) BurgerContext burgerContext,
			@RequestBody CreateCustomerRequest addCardRequest) {

		AppUtil.checkAuthorize(burgerContext);

		ResponseData<CreateCustomerResponse> responseBody = affiliateService.createCustomer(burgerContext, addCardRequest);

		return responseBody;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/affiliate/cards")
	public ResponseData<AddCardResponse> addCard(
			@RequestHeader(name = AppConstants.AUTHORIZE_HEADER, required = true) @ApiParam(value = "Access Token") String accessToken,
			@RequestAttribute(name = AppConstants.BG_CONTEXT, required = true) BurgerContext burgerContext,
			@RequestBody AddCardRequest addCardRequest) {

		AppUtil.checkAuthorize(burgerContext);

		ResponseData<AddCardResponse> responseBody = affiliateService.addCard(burgerContext, addCardRequest);

		return responseBody;
	}

}
