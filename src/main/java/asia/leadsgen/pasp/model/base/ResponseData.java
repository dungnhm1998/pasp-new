package asia.leadsgen.pasp.model.base;

import asia.leadsgen.pasp.error.SystemCode;
import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseData<T> implements Serializable {

	@JsonProperty(value = AppParams.CODE)
	int code = 200;
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

		@SneakyThrows
		public T getResult(Class<T> clazz) {
			if (result!=null){
				return result;
			}
			return clazz.newInstance();
		}
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

	public static <T> ResponseData<T> build(SystemCode systemCode, T responseData) {
		return restResult(systemCode, responseData, null);
	}

	public static <T> ResponseData<T> build(SystemCode systemCode, T responseData, List<Error> error) {
		return restResult(systemCode, responseData, error);
	}

	private static <T> ResponseData<T> restResult(SystemCode systemCode, T responseData, List<Error> errors) {
		ResponseData<T> apiResult = new ResponseData<>();
		apiResult.setCode(systemCode.getCode());

		if (StringUtils.isNotEmpty(systemCode.getMessage())){
			apiResult.setMessage(systemCode.getMessage());
		}

		if (responseData != null) {
			CommonData<T> data = new CommonData<T>();
			data.setResult(responseData);
			apiResult.setCommonData(data);
		}

		if (CollectionUtils.isNotEmpty(errors)){
			apiResult.setError(errors);
		}

		return apiResult;
	}

}
