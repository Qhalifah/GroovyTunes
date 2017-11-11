package exceptions;

import utils.Reason;

public class PlaylistNotCreatedException extends Exception {

	private static final long serialVersionUID = 9163810804843886848L;
	
	private Reason reason;
	
	private Exception baseException;
	
	public PlaylistNotCreatedException(Reason reason) {
		this.reason = reason;
	}
	
	public PlaylistNotCreatedException(Reason reason, Exception ex) {
		this.reason = reason;
		this.baseException = ex;
	}
	
	public Reason getReason() {
		return reason;
	}
	
	public Exception getBaseException() {
		return baseException;
	}
}


