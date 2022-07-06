package asia.leadsgen.pasp.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamUtil {

	public static boolean getBoolean(Map paramMap, String paramName) {
		return getBoolean(paramMap, paramName, false);
	}

	public static boolean getBoolean(Map paramMap, String paramName, boolean defaultValue) {
		return paramMap.get(paramName) != null ? GetterUtil.getBoolean(paramMap.get(paramName).toString(), defaultValue)
				:defaultValue;
	}

	public static int getInt(Map paramMap, String paramName) {
		return getInt(paramMap, paramName, 0);
	}

	public static int getInt(Map paramMap, String paramName, int defaultValue) {
		return paramMap.get(paramName) != null ? GetterUtil.getInteger(paramMap.get(paramName), defaultValue)
				:defaultValue;
	}

	public static long getLong(Map paramMap, String paramName) {
		return getLong(paramMap, paramName, 0L);
	}

	public static long getLong(Map paramMap, String paramName, long defaultValue) {
		return paramMap.get(paramName) != null ? GetterUtil.getLong(paramMap.get(paramName).toString(), defaultValue)
				:defaultValue;
	}

	public static double getFormatedDouble(Map paramMap, String paramName, int decimalPlaces) {
		double value = getDouble(paramMap, paramName, 0d);
		return GetterUtil.formatDouble(value, decimalPlaces);
	}


	public static double getFormatedDouble(String paramName, int decimalPlaces) {
		double value = getDouble(paramName, 0d);
		return GetterUtil.formatDouble(value, decimalPlaces);
	}

	public static double getDouble(String paramName, double defaultValue) {
		return paramName != null ? GetterUtil.get(paramName, defaultValue):defaultValue;
	}

	public static double getDouble(Map paramMap, String paramName) {
		return getDouble(paramMap, paramName, 0d);
	}

	public static double getDouble(Map paramMap, String paramName, double defaultValue) {
		return paramMap.get(paramName) != null ? GetterUtil.get(paramMap.get(paramName), defaultValue):defaultValue;
	}

	public static String getString(Map paramMap, String paramName) {
		return getString(paramMap, paramName, StringPool.BLANK.getValue());
	}

	public static String getString(Map paramMap, String paramName, String defaultValue) {

		if (paramMap == null || paramMap.isEmpty() || paramName.isEmpty()) {
			return defaultValue;
		}

		if (paramMap.get(paramName) == null || paramMap.get(paramName).toString().isEmpty()) {
			return defaultValue;
		}

		if (paramMap.get(paramName) instanceof List) {
			return ((List) paramMap.get(paramName)).get(0).toString().trim();
		}

		if (paramMap.get(paramName) instanceof java.sql.Timestamp) {

			SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);

//            dateFormat.setTimeZone(TimeZone.getTimeZone(AppConstants.DEFAULT_TIME_ZONE));

			return dateFormat.format(paramMap.get(paramName));
		}

		return paramMap.get(paramName).toString().trim();
	}

	public static Map getMapData(Map paramMap, String paramName) {
		return getMapData(paramMap, paramName, new HashMap());
	}

	public static Map getMapData(Map paramMap, String paramName, Map defaultValue) {

		Object paramValue = paramMap.get(paramName);

		if (paramValue != null) {

			if (paramValue instanceof String) {

				return JSONUtil.convertJSONToMap(paramValue.toString());

			} else if (paramValue instanceof Map) {

				return (Map) paramValue;
			} else {
				return defaultValue;
			}

		} else {
			return defaultValue;
		}
	}

	public static List getListData(Map paramMap, String paramName) {
		return getListData(paramMap, paramName, new ArrayList<>());
	}

	public static List getListData(Map paramMap, String paramName, List defaultValue) {

		Object paramValue = paramMap.get(paramName);

		if (paramValue != null) {

			if (paramValue instanceof List) {

				return (List) paramValue;

			} else {
				return defaultValue;
			}

		} else {
			return defaultValue;
		}
	}

	public static String getString(String[] paramStringArray, int position, String defaultValue) {

		if (paramStringArray.length > position) {
			return paramStringArray[position];
		} else {
			return defaultValue;
		}
	}

	public static String getString(String[] paramStringArray, int position) {

		if (paramStringArray.length > position) {
			return paramStringArray[position];
		} else {
			return getString(paramStringArray, position, StringPool.BLANK.getValue());
		}
	}
}
