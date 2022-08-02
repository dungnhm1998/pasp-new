package asia.leadsgen.pasp.model.exception;


import asia.leadsgen.pasp.error.SystemCode;
import lombok.Data;

@Data
public class SystemException extends RuntimeException {

	private SystemCode systemCode;

	public SystemCode getSystemCode() {
		return systemCode;
	}

	public SystemException(SystemCode error) {
		super(error.getMessage());
		this.systemCode = error;
	}
}
