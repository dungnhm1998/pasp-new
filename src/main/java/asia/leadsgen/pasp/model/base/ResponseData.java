package asia.leadsgen.pasp.model.base;

import asia.leadsgen.pasp.error.SystemError;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> implements Serializable {

	@JsonProperty(value = AppParams.CODE)
	int code;
	@JsonProperty(value = AppParams.MESSAGE)
	String message;
	@JsonProperty(value = AppParams.DATA)
	CommonData<T> commonData;
	@JsonProperty(value = AppParams.ERROR)
	List<Error> error;

	@lombok.Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CommonData<T> {
		@JsonProperty(value = AppParams.TOTAL)
		@JsonInclude(value = JsonInclude.Include.NON_NULL)
		int total;
		@JsonProperty(value = AppParams.PAGE)
		@JsonInclude(value = JsonInclude.Include.NON_NULL)
		int page;
		@JsonProperty(value = AppParams.PAGE_SIZE)
		@JsonInclude(value = JsonInclude.Include.NON_NULL)
		int pageSize;
		@JsonProperty(value = AppParams.RESULT)
		T result;
	}

	@lombok.Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Error {
		@JsonProperty(value = AppParams.SERVER_CODE)
		int serverCode;
		@JsonProperty(value = AppParams.MESSAGE)
		String reason;
	}

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
		apiResult.setCode(systemError.getCode());
		apiResult.setMessage(systemError.getMessage());

		CommonData<T> data = new CommonData<T>();
		data.setResult(responseData);

		apiResult.setCommonData(data);
		return apiResult;
	}

}
