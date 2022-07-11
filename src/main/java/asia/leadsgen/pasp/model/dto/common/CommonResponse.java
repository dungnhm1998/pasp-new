package asia.leadsgen.pasp.model.dto.common;


import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

public class CommonResponse {

	@JsonProperty(value = AppParams.CODE)
	private String code;
	@JsonProperty(value = AppParams.MESSAGE)
	private String message;
	@JsonProperty(value = AppParams.DATA)
	private List<Data> data;
	@JsonProperty(value = AppParams.ERROR)
	private List<Error> error;

	@lombok.Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Data {
		@JsonProperty(value = AppParams.TOTAL)
		@JsonInclude(value = JsonInclude.Include.NON_NULL)
		private int total;
		@JsonProperty(value = AppParams.PAGE)
		@JsonInclude(value = JsonInclude.Include.NON_NULL)
		private int page;
		@JsonProperty(value = AppParams.PAGE_SIZE)
		@JsonInclude(value = JsonInclude.Include.NON_NULL)
		private int pageSize;
		@JsonProperty(value = AppParams.RESULT)
		private Object result;
	}

	@lombok.Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Error {
		@JsonProperty(value = AppParams.SERVER_CODE)
		private int serverCode;
		@JsonProperty(value = AppParams.MESSAGE)
		private String reason;
	}
}