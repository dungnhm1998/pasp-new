package asia.leadsgen.pasp.util;

import asia.leadsgen.pasp.error.SystemError;
import asia.leadsgen.pasp.model.base.BurgerContext;
import asia.leadsgen.pasp.model.exception.AuthorizationException;
import asia.leadsgen.pasp.model.exception.BadRequestException;
import asia.leadsgen.pasp.model.exception.SystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

@Log4j2
public class AppUtil {

	private static ModelMapper mapper = new ModelMapper();

	public static String getFullURL(HttpServletRequest request) {
//	    log.info("request.getRequestURL = {}", request.getRequestURL());
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	public static void checkAuthorize(BurgerContext burgerContext) throws SystemException {
		if (Objects.isNull(burgerContext) || Objects.isNull(burgerContext.getUserId())) {
			throw new AuthorizationException(SystemError.UNAUTHORIZED);
		}
	}


	/**
	 * Check User is a subaccount and is not authorized to store data
	 *
	 * @param burgerContext
	 * @param storeId
	 */
	public static void secureSubaccountAccessStore(BurgerContext burgerContext, String storeId) {
		Boolean isOwner = burgerContext.getIsOwner();
		if (!isOwner) {
			List<String> listStore = burgerContext.getDropshipStoreIds();
			long count = listStore.stream().filter(e -> storeId.equalsIgnoreCase(e)).count();
			if (count <= 0) {
				throw new BadRequestException(SystemError.OPERATION_NOT_PERMITTED);
			}
		}
	}

	public static Date getDateByTimeZone(String timezone) {

		DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");

		LocalDateTime currentDateTime = LocalDateTime.now();

		ZonedDateTime current = currentDateTime.atZone(ZoneId.of(TimeZone.getDefault().getID()));
		ZonedDateTime converted = current.withZoneSameInstant(ZoneId.of(timezone));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String str = globalFormat.format(converted);
		Date t = null;
		try {
			t = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}

	public static String reformat(String date, String formatin, String formatout) throws ParseException {
		SimpleDateFormat dfIn = new SimpleDateFormat(formatin);
		Date d = dfIn.parse(date);
		SimpleDateFormat dfOut = new SimpleDateFormat(formatout);
		String rs = dfOut.format(d);
		return rs;
	}

	public static Date format(Date in, String format) {
		SimpleDateFormat dfIn = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = dfIn.parse(dfIn.format(in));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static String getDefaultFormat(Date in) {
		SimpleDateFormat dfIn = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);
		String date = null;
		date = dfIn.format(in);
		return date;
	}

	@SuppressWarnings("unchecked")
	public static ModelMapper getModelMapper() {
		mapper.getConfiguration().setAmbiguityIgnored(true);
		mapper.addConverter(dateToStringConverter);
		mapper.addConverter(stringToDouble);
		return mapper;
	}

	private static Converter dateToStringConverter = new Converter<Date, String>() {

		@Override
		public String convert(MappingContext<Date, String> context) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);
			String create = context.getSource() != null ? dateFormat.format(context.getSource()):null;
			return create;
		}

	};

	private static Converter stringToDouble = new Converter<String, Double>() {

		@Override
		public Double convert(MappingContext<String, Double> context) {
			Double d = GetterUtil.getDouble(StringUtils.isNotEmpty(context.getSource()) ? context.getSource():"0.00");
			return d;
		}

	};

	public static String generateOrderTrackingNumber() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);

		return cal.getTime();
	}

	public static String formatTimezone(String timezone) {

		timezone = timezone.trim().replace(' ', '+');

		if (timezone.endsWith(".25")) {
			timezone = timezone.replace(".25", ".5");
		}

		if (timezone.endsWith(".5")) {
			if (timezone.contains("+")) {
				if (timezone.substring((timezone.lastIndexOf("+") + 1), timezone.lastIndexOf(".")).length() < 2) {
					timezone = (timezone.replace("+", "+0")).replace(".5", ":30");
				} else {
					timezone = timezone.replace(".5", ":30");
				}

			} else if (timezone.contains("-")) {
				if (timezone.substring((timezone.lastIndexOf("-") + 1), timezone.lastIndexOf(".")).length() < 2) {
					timezone = (timezone.replace("-", "-0")).replace(".5", ":30");
				} else {
					timezone = timezone.replace(".5", ":30");
				}
			}

		} else if (timezone.endsWith(".75")) {
			if (timezone.contains("+")) {
				if (timezone.substring((timezone.lastIndexOf("+") + 1), timezone.lastIndexOf(".")).length() < 2) {
					timezone = (timezone.replace("+", "+0")).replace(".75", ":45");
				} else {
					timezone = timezone.replace(".75", ":45");
				}

			} else if (timezone.contains("-")) {
				if (timezone.substring((timezone.lastIndexOf("-") + 1), timezone.lastIndexOf(".")).length() < 2) {
					timezone = (timezone.replace("-", "-0")).replace(".75", ":45");
				} else {
					timezone = timezone.replace(".75", ":45");
				}
			}
		} else {
			if (timezone.contains("+")) {
				if (timezone.substring((timezone.lastIndexOf("+") + 1)).length() < 2) {
					timezone = (timezone.replace("+", "+0")).concat(":00");
				} else {
					timezone = timezone.concat(":00");
				}

			} else if (timezone.contains("-")) {
				if (timezone.substring((timezone.lastIndexOf("-") + 1)).length() < 2) {
					timezone = (timezone.replace("-", "-0")).concat(":00");
				} else {
					timezone = timezone.concat(":00");
				}
			}
		}

		return timezone.replace("UTC", "GMT");
	}

	public static String genRandomId() {
		String random = RandomStringUtils.randomAlphanumeric(16);
		return random;
	}

	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			throw new NullPointerException("Entity passed for initialization is null");
		}

		Hibernate.initialize(entity);
		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}
		return entity;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString().replace("-", "");
		return uuidAsString;
	}

	public static String getStackTrace(Exception ex) {
		StringBuffer sb = new StringBuffer(500);
		StackTraceElement[] st = ex.getStackTrace();
		sb.append(ex.getClass().getName() + ": " + ex.getMessage() + "\n");
		for (int i = 0; i < st.length; i++) {
			sb.append("\t at " + st[i].toString() + "\n");
		}
		return sb.toString();
	}

	public static String replaceEmptyString(String str, String replaceStr) {
		return StringUtils.isEmpty(str) ? replaceStr:str;
	}

	public static Object jsonToObject(String input, Class<?> clazz)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		Object obj = null;
		obj = mapper.readValue(input, clazz);

		return obj;
	}

	public static long getCampaignTimeRemaining() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime twentythree;
		if (now.getHour() == 23) {
			twentythree = now.plusDays(1).withHour(23).withMinute(0).withSecond(0).withNano(0);
		} else {
			twentythree = now.withHour(23).withMinute(0).withSecond(0).withNano(0);
		}

		return Duration.between(now, twentythree).toMillis();
	}

	public static Date parse(String in, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = df.parse(in);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static Date parseToDefaultFormat(String in) {
		return parse(in, AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);
	}

	public static String getMD5(String fileName) throws Exception {
		String original = fileName;
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original.getBytes());
		byte[] digest = md.digest();
		StringBuilder sb = new StringBuilder();
		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}

		return sb.toString();
	}

	public static Date parseDateAndTimeZone(String inLong, String timezone) {
		Date out = null;
		Calendar affCal = Calendar.getInstance();
		if (StringUtils.isNotEmpty(inLong)) {
			DateFormat dateFormat = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);
			dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
			affCal.setTimeInMillis(Long.parseLong(inLong));
//			inLong = dateFormat.format(affCal.getTime());
//			out = parseToDefaultFormat(inLong);

			DateFormat dateFormatAmerica = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT_PATTERN);
			dateFormatAmerica.setTimeZone(TimeZone.getTimeZone(AppConstants.DEFAULT_TIME_ZONE_ID));
			inLong = dateFormatAmerica.format(affCal.getTime());
			out = parseToDefaultFormat(inLong);
		}
		return out;
	}

}
