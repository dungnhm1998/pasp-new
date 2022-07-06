package asia.leadsgen.pasp.model.dto.payment;

import asia.leadsgen.pasp.model.dto.Common.CommonResponse;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PaymentRequest {


	@JsonProperty(value = AppParams.METHOD)
	private String method;
	@JsonProperty(value = AppParams.MESSAGE)
	private String message;
	@JsonProperty(value = AppParams.DATA)
	private List<CommonResponse.Data> data;
}
