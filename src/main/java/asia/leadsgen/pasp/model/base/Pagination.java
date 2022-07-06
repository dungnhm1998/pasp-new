package asia.leadsgen.pasp.model.base;

import asia.leadsgen.pasp.util.AppParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pagination<T> {
	
	@JsonProperty(value = AppParams.PAGE)
	@JsonInclude(value = Include.NON_NULL)
	private Integer page;
	
	@JsonProperty(value = AppParams.PAGE_SIZE)
	@JsonInclude(value = Include.NON_NULL)
	private Integer pageSize;
	
	@JsonProperty(value = AppParams.TOTAL)
	@JsonInclude(value = Include.NON_NULL)
	private Long total;

	@JsonProperty(value = AppParams.TOTAL_PAGE)
	@JsonInclude(value = Include.NON_NULL)
	private Integer totalPage;
	
	@JsonProperty(value = AppParams.TOTAL_AMOUNT)
	@JsonInclude(value = Include.NON_NULL)
	private Double totalAmount;
	
	@JsonProperty(value = AppParams.ERRORS)
	@JsonInclude(value = Include.NON_NULL)
	private List<String> errors;
	
	@JsonProperty(value = AppParams.DATA)
	private T data;

	public Pagination(int page, int pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}

	public Pagination(List<String> errors, T data) {
		super();
		this.errors = errors;
		this.data = data;
	}

	public Pagination(Integer page, Integer pageSize, Long total, Integer totalPage, T data) {
		super();
		this.page = page;
		this.pageSize = pageSize;
		this.total = total;
		this.totalPage = totalPage;
		this.data = data;
	}
	
	
}
