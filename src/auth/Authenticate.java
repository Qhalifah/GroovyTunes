package auth;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import user.User;
import utils.UserOperations;

public class Authenticate {

    /**
     * This method authenticates user. It takes in user name and password as string, and checks it against
     * values in database. If there exists a user, it returns the user.
     * @param username: user name provided at the login
     * @param password: password provided at the login
     * @return User u if there exists a user. NULL otherwise
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IOException if unable to read the database
     * @throws ParseException if unable to parse date from string
     */
    public static AuthResult authUser(String username, String password) throws ClassNotFoundException, SQLException, IOException {
        User u = null;
        u = UserOperations.findUser(username);
        if(u == null)
        	return new AuthResult(AUTH_RESULT.NO_SUCH_USER, null);
        if(!u.verifyPassword(password))
        	return new AuthResult(AUTH_RESULT.INCORRECT_PASSWORD, null);
        return new AuthResult(AUTH_RESULT.SUCCESS, u);
    }
    
    public static class AuthResult {
    	private AUTH_RESULT result;
    	User user;
    	
    	public AuthResult(AUTH_RESULT result, User user) {
    		this.result = result;
    		this.user = user;
    	}
    	
    	public AUTH_RESULT getResult() {
    		return result;
    	}
    	
    	public User getUser() {
    		return user;
    	}
    }
    
    public static enum AUTH_RESULT {
    	SUCCESS,
    	NO_SUCH_USER,
    	INCORRECT_PASSWORD
    }
}