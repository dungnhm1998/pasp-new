package asia.leadsgen.pasp.util;

public enum StringPool {
	BLANK(""), COLON(":"), QUESTION("?"), DOUBLE_SPACE("  "), NEW_LINE("\n"), RETURN_NEW_LINE("\r\n"), NULL("null"),
	SPACE(" "), FORWARD_SLASH("/"), BACK_SLASH("\\"), DASH("-"), VERTICAL_BAR("|"), UNDERSCORE("_"), COMMA(",");

	private String value;

	StringPool(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
