package exceptions;

import utils.Reason;

public class PlaylistNotCreatedException extends Exception {

	private static final long serialVersionUID = 9163810804843886848L;
	
	private Reason reason;
	
	public PlaylistNotCreatedException(Reason reason) {
		this.reason = reason;
	}
	
	public Reason getReason() {
		return reason;
	} 
}


