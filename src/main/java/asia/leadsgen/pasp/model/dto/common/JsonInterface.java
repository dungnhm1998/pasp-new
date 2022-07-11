package asia.leadsgen.pasp.model.dto.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public interface JsonInterface {
	public default String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			jsonString = "Can't build json from object";
		}
		return jsonString;
	}
}