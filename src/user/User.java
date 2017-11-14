package user;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

import player.Player;
import player.PlayerFactory;
import utils.Constants;

/**
 * This class contains a list of playlist available to a user. It also maps to UserAccountDetails.
 */
public class User {
	private Player player;
	private UserAccountDetails details;

	/**
	 * Created one instance of User using given values.
	 * This instance is created from Authenticate class after letting the user log in
	 */
	public User(String username, String password, String firstName, String lastName,
		Date dob, Date dateJoined, boolean membershipStatus) {
		this.details = new UserAccountDetails(username, password, firstName, lastName,
			dob, dateJoined, membershipStatus);
		player = null;
	}
	
	public Player getPlayer() {
		if(player == null) {
			player = PlayerFactory.getPlayer(this);
		}
		return player;
	}
	/**
	 * Returns user account details
	 * @return account details in JSON form
	 */
	public JSONObject getUserInfo() {
		return this.details.getUserDetails();
	}

	/**
	 * Updates the user details.
	 * @return true if the details are updated, false otherwise
	 * @throws ParseException 
	 */
	public boolean updateUserInfo(String username, String firstName, String lastName, 
		String dateOfBirth) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date dob = sdf.parse(dateOfBirth);
		return details.updateDetails(username, firstName, lastName, dob);
	}

	public boolean isPremiumUser() {
		return details.isPremiumMember();
	}
	
	public void activatePremium() {
		try {
			details.activatePremium();
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String getUsername() {
		return details.getUsername();
	}
	public String getFirstName() {
		return details.getFirstName();
	}
	public String getLastName() {
		return details.getLastName();
	}
	public String getDateOfBirth() {
		return details.getDateOfBirth();
	}
	public String getDateJoined() {
		return details.getDateJoined();
	}
	public String getMembershipStatus() {
		return details.getMembershipStatus();
	}

	public boolean verifyPassword(String password) {
		return details.getPassword().equals(password);
	}

}
