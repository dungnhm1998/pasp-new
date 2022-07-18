package asia.leadsgen.pasp.controller.payment;


import asia.leadsgen.pasp.model.base.BurgerContext;
import asia.leadsgen.pasp.model.base.ResponseData;
import asia.leadsgen.pasp.model.dto.payment.PaymentExample;
import asia.leadsgen.pasp.model.dto.payment.PaymentRequest;
import asia.leadsgen.pasp.model.dto.payment.PaymentResponse;
import asia.leadsgen.pasp.service.PaymentService;
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
@Api(value = "Handler Payment")
@RequestMapping(path = "/payment")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@RequestMapping(method = RequestMethod.POST, path = "")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Request success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "charge succees", summary = "charge success", value = PaymentExample.SUCCESS)})),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "Invalid payment method", summary = "Invalid payment method", value = PaymentExample.E400_INVALID_PAYMENT_METHOD),
					@ExampleObject(name = "Invalid payment currency", summary = "Invalid payment currency", value = PaymentExample.E400_INVALID_PAYMENT_CURRENCY),
					@ExampleObject(name = "Payment processing error", summary = "Payment processing error", value = PaymentExample.E400_PAYMENT_PROCESSING_ERROR),
					@ExampleObject(name = "Invalid payment info", summary = "Invalid payment info", value = PaymentExample.E400_INVALID_PAYMENT_INFO)})),
			@ApiResponse(responseCode = "401", description = "Burger user id not exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "User id not exists", summary = "User id not exists", value = PaymentExample.E401_UNAUTHORIZED)})),
			@ApiResponse(responseCode = "500", description = "Internal Sever Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "Something error", summary = "Something error", value = PaymentExample.INTERNAL_SEVER_ERROR)})),})
	public ResponseData<PaymentResponse> paymentCharge(
			@RequestHeader(name = AppConstants.AUTHORIZE_HEADER, required = true) @ApiParam(value = "Access Token") String accessToken,
			@RequestAttribute(name = AppConstants.BG_CONTEXT, required = true) BurgerContext burgerContext,
			@RequestBody PaymentRequest requestBody) {

		AppUtil.checkAuthorize(burgerContext);

		ResponseData<PaymentResponse> responseBody = paymentService.paymentCharge(burgerContext.getUserId(), requestBody);

		return responseBody;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/{id}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Request success", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "charge succees", summary = "charge success", value = PaymentExample.SUCCESS)})),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "Invalid payment method", summary = "Invalid payment method", value = PaymentExample.E400_INVALID_PAYMENT_METHOD),
					@ExampleObject(name = "Invalid payment currency", summary = "Invalid payment currency", value = PaymentExample.E400_INVALID_PAYMENT_CURRENCY),
					@ExampleObject(name = "Payment processing error", summary = "Payment processing error", value = PaymentExample.E400_PAYMENT_PROCESSING_ERROR),
					@ExampleObject(name = "Invalid payment info", summary = "Invalid payment info", value = PaymentExample.E400_INVALID_PAYMENT_INFO)})),
			@ApiResponse(responseCode = "401", description = "Burger user id not exists", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "User id not exists", summary = "User id not exists", value = PaymentExample.E401_UNAUTHORIZED)})),
			@ApiResponse(responseCode = "500", description = "Internal Sever Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Object.class), examples = {
					@ExampleObject(name = "Something error", summary = "Something error", value = PaymentExample.INTERNAL_SEVER_ERROR)})),})
	public ResponseData<PaymentResponse> executePayment(
			@RequestHeader(name = AppConstants.AUTHORIZE_HEADER, required = true) @ApiParam(value = "Access Token") String accessToken,
			@RequestAttribute(name = AppConstants.BG_CONTEXT, required = true) BurgerContext burgerContext,
			@RequestBody PaymentRequest requestBody) {

		AppUtil.checkAuthorize(burgerContext);

		ResponseData<PaymentResponse> responseBody = paymentService.paymentExecute(burgerContext.getUserId(), requestBody);

		return responseBody;
	}

}
