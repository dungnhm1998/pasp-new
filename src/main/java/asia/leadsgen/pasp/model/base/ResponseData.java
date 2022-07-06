package asia.leadsgen.pasp.model.base;

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
public class ResponseData<T>  implements Serializable {

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
        return restResult(HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), null);
    }

    public static <T> ResponseData<T> ok(String message) {
        return restResult(HttpStatus.OK, message, null);
    }

    public static <T> ResponseData<T> ok(T responseData) {
        return restResult(HttpStatus.OK, HttpStatus.OK.getReasonPhrase(), responseData);
    }

    public static <T> ResponseData<T> ok(String message, T responseData) {
        return restResult(HttpStatus.OK, message, responseData);
    }

    public static <T> ResponseData<T> failed(HttpStatus status, String message) {
        return restResult(status, message, null);
    }


    public static <T> ResponseData<T> failed(HttpStatus status, String message, T responseData) {
        return restResult(status, message, responseData);
    }

    public static <T> ResponseData<T> build(HttpStatus status, String message, T responseData) {
        return restResult(status, message, responseData);
    }

    private static <T> ResponseData<T> restResult(HttpStatus status, String message, T responseData) {
        ResponseData<T> apiResult = new ResponseData<>();
        apiResult.setStatus(status.value());
        apiResult.setMessage(message);
        apiResult.setResponseData(responseData);
        return apiResult;
    }

}
