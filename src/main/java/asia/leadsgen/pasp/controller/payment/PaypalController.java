package asia.leadsgen.pasp.controller.payment;

import asia.leadsgen.pasp.model.base.BurgerContext;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.payppal.InvoicePaypalExample;
import asia.leadsgen.pasp.model.dto.payppal.InvoicePaypalRequest;
import asia.leadsgen.pasp.model.dto.payppal.InvoicePaypalResponse;
import asia.leadsgen.pasp.service.PaypalService;
import asia.leadsgen.pasp.util.AppConstants;
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
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@Api(value = "Handler Paypal")
public class PaypalController {

	@Autowired
	PaypalService paypalService;

	@RequestMapping(method = RequestMethod.POST, path = "/paypal/invoice")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Request success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "charge succees", summary = "charge success", value = InvoicePaypalExample.SUCCESS)})),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "Invalid payment method", summary = "Invalid payment method", value = InvoicePaypalExample.E400_INVALID_PAYMENT_METHOD),
					@ExampleObject(name = "Invalid payment currency", summary = "Invalid payment currency", value = InvoicePaypalExample.E400_INVALID_PAYMENT_CURRENCY),
					@ExampleObject(name = "Payment processing error", summary = "Payment processing error", value = InvoicePaypalExample.E400_PAYMENT_PROCESSING_ERROR),
					@ExampleObject(name = "Invalid payment info", summary = "Invalid payment info", value = InvoicePaypalExample.E400_INVALID_PAYMENT_INFO)})),
			@ApiResponse(responseCode = "401", description = "Burger user id not exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "User id not exists", summary = "User id not exists", value = InvoicePaypalExample.E401_UNAUTHORIZED)})),
			@ApiResponse(responseCode = "500", description = "Internal Sever Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "Something error", summary = "Something error", value = InvoicePaypalExample.INTERNAL_SEVER_ERROR)})),})
	public ResponseData<InvoicePaypalResponse> chargePaypal(
			@RequestHeader(name = AppConstants.AUTHORIZE_HEADER, required = true) @ApiParam(value = "Access Token") String accessToken,
			@RequestAttribute(name = AppConstants.BG_CONTEXT, required = true) BurgerContext burgerContext,
			@RequestBody InvoicePaypalRequest invoicePaypalRequest) {

		AppUtil.checkAuthorize(burgerContext);

		ResponseData<InvoicePaypalResponse> responseBody = paypalService.createInvoice(invoicePaypalRequest);

		return responseBody;
	}

}
