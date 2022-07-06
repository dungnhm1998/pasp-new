package asia.leadsgen.pasp.model.exception;


import asia.leadsgen.pasp.error.SystemError;
import lombok.Data;

@Data
public class SystemException extends RuntimeException {

	private SystemError systemError;

	public SystemError getSystemError() {
		return systemError;
	}

	public SystemException(SystemError error) {
		super(error.getMessage());
		this.systemError = error;
	}
}
