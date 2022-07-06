package asia.leadsgen.pasp.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONUtil {
	public static Map<String, Object> convertJSONToMap(String jsonString) {
		JSONObject jsonObject = new JSONObject(jsonString);
		return convertJSONToMap(jsonObject);
	}

	public static Map<String, Object> convertJSONToMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = convertJSONToList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = convertJSONToMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> convertJSONToList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = convertJSONToList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = convertJSONToMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public static String json(Object object) {
		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			jsonString = "Can't build json from object";
		}
//		JSONObject jsonObject = new JSONObject(jsonString);
//		return jsonObject.toString();
		return jsonString;
	}
}
