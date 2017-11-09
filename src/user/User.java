package user;

import java.io.IOException;
import java.util.Date;

import org.json.simple.JSONObject;

import player.Player;
import player.PlayerFactory;

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
		Date dob, Date dateJoined) {
		this.details = new UserAccountDetails(username, password, firstName, lastName,
			dob, dateJoined);
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
	 */
	public boolean updateUserInfo(String username, String firstName, String lastName, 
		Date dob, Date dateJoined) throws IOException {
		return details.updateUserDetails(username, firstName, lastName, dob, dateJoined);
	}

	public boolean isPremiumUser() {
		return details.isPremiumMember();
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
