package server;

import java.io.IOException;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import auth.Authenticate;
import auth.Authenticate.AUTH_RESULT;
import auth.Authenticate.AuthResult;
import user.User;
import utils.Constants;
import utils.dbmgr.UserOperations;

public class UserRequestHelper {

	private GroovySession session;

	public UserRequestHelper(GroovySession session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public void onMessage(JSONObject message) throws ParseException, ClassNotFoundException, SQLException, IOException {
		String type = (String) message.get("type");
		JSONObject response;
		switch (type) {
		case "check-username":
			response = new JSONObject();
			String name = (String) message.get("username");
			if (UserOperations.isUsernameAvailable(name)) {
				response.put("status", "success");
			} else {
				response.put("status", "error");
				response.put("message", "username not available");
			}
			session.getRemote().sendString(response.toJSONString());
			break;

		case "create-account":
			response = new JSONObject();
			name = (String) message.get("username");
			String password = (String) message.get("password");
			String firstName = null, lastName = null, dob = null;
			if (message.containsKey("firstname"))
				firstName = (String) message.get("firstname");
			if (message.containsKey("lastname"))
				lastName = (String) message.get("lastname");
			if (message.containsKey("dob"))
				dob = (String) message.get("dob");
			if (UserOperations.createUser(name, password, firstName, lastName, dob)) {
				response.put("status", "success");
			} else {
				response.put("status", "error");
				response.put("message", "unable to create a user");
			}
			session.getRemote().sendString(response.toJSONString());
			break;

		case "login":
			response = new JSONObject();
			name = (String) message.get("username");
			password = (String) message.get("password");
			AuthResult result = Authenticate.authUser(name, password);
			if (result.getResult() == AUTH_RESULT.SUCCESS) {
				response.put("type", "retLogin");
				response.put("status", "success");
				session.add(Constants.USER_SESSION_KEY, result.getUser());
			} else if (result.getResult() == AUTH_RESULT.INCORRECT_PASSWORD) {
				response.put("type", "retLogin");
				response.put("status", "error");
				response.put("message", "wrong password");
			} else {
				response.put("type", "retLogin");
				response.put("status", "error");
				response.put("message", "no such user");
			}
			session.getRemote().sendString(response.toJSONString());
			break;

		case "logout":
			response = new JSONObject();
			session.remove(Constants.USER_SESSION_KEY);
			response.put("status", "success");
			session.getRemote().sendString(response.toJSONString());
			session.invalidate();
			break;

		case "get-info":
			response = new JSONObject();
			User u = (User) session.get(Constants.USER_SESSION_KEY);
			response.put("status", "success");
			response.put("username", u.getUsername());
			response.put("firstname", u.getFirstName());
			response.put("lastname", u.getLastName());
			response.put("dob", u.getDateOfBirth());
			response.put("date-joined", u.getDateJoined());
			response.put("status", u.getMembershipStatus());
			session.getRemote().sendString(response.toJSONString());
			break;

		case "update-info":
			response = new JSONObject();
			name = (String) message.get("username");
			String firstname = (String) message.get("firstname");
			String lastname = (String) message.get("lastname");
			dob = (String) message.get("dob");
			u = (User) session.get(Constants.USER_SESSION_KEY);
			try {
				if (u.updateUserInfo(name, firstname, lastname, dob))
					response.put("status", "success");
				else {
					response.put("status", "error");
					response.put("message", "unknown error");
				}
			} catch (java.text.ParseException e) {
				response.put("status", "error");
				response.put("message", "unable to parse the date format");
			}
			session.getRemote().sendString(response.toJSONString());
			break;
		}
	}
}
