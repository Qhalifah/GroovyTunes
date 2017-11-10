package user;

import java.io.IOException;
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
		String dateOfBirth, String dateJoined) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		Date dob = sdf.parse(dateOfBirth);
		Date doj = sdf.parse(dateJoined);
		return details.updateDetails(username, firstName, lastName, dob, doj);
	}

	public boolean isPremiumUser() {
		return details.isPremiumMember();
	}
	
	public String getUsername() {
		return details.getUsername();
	}
	public String getPassword() {
		return details.getPassword();
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
	
	/**
	 * commenting out this method. Do we need to provide this functionality as well?
	 */
	/*
	public static void removeUser(String username){
		try{

			CSVReader reader = new CSVReader(new FileReader(USERS_DATABASE), ',', '"', 0);
			List<String[]> allRows = reader.readAll();

				// Iterator created for List of records
				// For each String[] record, if 0th element (username) is the one to be deleted, it is removed from List
			for (Iterator<String[]> iterator = allRows.listIterator(); iterator.hasNext(); ){
				String[] record = iterator.next();
				if (record[1].equals(username)) {
					iterator.remove();
				}
			}

			CSVWriter writer = new CSVWriter(new FileWriter(USERS_DATABASE));
			writer.writeAll(allRows);
			writer.flush();
            writer.close();
		}

		catch(Exception e){
			e.printStackTrace();
		}
	}
	*/

}
