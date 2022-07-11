package asia.leadsgen.pasp.model.base;

import asia.leadsgen.pasp.error.SystemError;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> implements Serializable {

	private int status;
	private String message;

	@JsonProperty(value = "errors")
	@JsonInclude(value = Include.NON_NULL)
	private List<String> errors;

	@JsonProperty(value = "response_data")
	@JsonInclude(value = Include.NON_NULL)
	private T responseData;

	@JsonProperty(value = "infomation_link")
	@JsonInclude(value = Include.NON_EMPTY)
	private String infomationLink;

	public static <T> ResponseData<T> ok() {
		return restResult(SystemError.RESPONSE_OK, null);
	}

	public static <T> ResponseData<T> ok(String message) {
		return restResult(SystemError.RESPONSE_OK, null);
	}

	public static <T> ResponseData<T> ok(T responseData) {
		return restResult(SystemError.RESPONSE_OK, responseData);
	}

	public static <T> ResponseData<T> ok(SystemError systemError, T responseData) {
		return restResult(systemError, responseData);
	}

	public static <T> ResponseData<T> failed(SystemError systemError) {
		return restResult(systemError, null);
	}


	public static <T> ResponseData<T> failed(SystemError systemError, T responseData) {
		return restResult(systemError, responseData);
	}

	public static <T> ResponseData<T> build(SystemError systemError, T responseData) {
		return restResult(systemError, responseData);
	}

	private static <T> ResponseData<T> restResult(SystemError systemError, T responseData) {
		ResponseData<T> apiResult = new ResponseData<>();
		apiResult.setStatus(systemError.getCode());
		apiResult.setMessage(systemError.getMessage());
		apiResult.setResponseData(responseData);
		return apiResult;
	}

}
