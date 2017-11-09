package user;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;

import utils.Constants;

/**
 * This class stored all details related to the user.
 * It includes username, password, firstname, lastname, date of birth (dob), date on which user
 * joined (dateJoined).
 * It also tracks if the user is PREMIUM user or REGULAR user
 * It has 1:1 relationship with User class
 */
public class UserAccountDetails {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Date dob;
	private Date dateJoined;
	private boolean membershipStatus;

	/**
	 * Creates an object of UserAccountDetails and assigns the given parameters
	 * @param username: username
	 * @param password: password
	 * @param firstName: first name of user
	 * @param lastName: last name of user
	 * @param dob: Date of birth
	 * @param dateJoined: Date on which user joined GroovyTunes
	 */
	public UserAccountDetails(String username, String password, String firstName, String lastName,
		Date dob, Date dateJoined) {
 		this.username = username;
 		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.dateJoined = dateJoined;
		this.membershipStatus = false;
	}

	/**
	 * Returns details of the user in the form of JSON.
	 * Details does not include username and password
	 * @return Details of the user in JSON format
	 */
	public JSONObject getUserDetails() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		JSONObject userDetails = new JSONObject();
		userDetails.put("firstName", this.firstName);
		userDetails.put("lastName", this.lastName);
		userDetails.put("dob", sdf.format(this.dob));
		userDetails.put("dateJoined", sdf.format(this.dateJoined));
		userDetails.put("membershipStatus", this.membershipStatus);
		return userDetails;
	}

	/**
	 * This method updates the details stored in the database.
	 * @param username: username. Also used as key
	 * @param firstName: new value of first name. If unchanged, send the previous value
	 * @param lastName: new value of last name. If unchanged, send the previous value
	 * @param dob: new value of date of birth. If unchanged, send the previous value
	 * @param dateJoined. send the previous value. It makes no sense to change this
	 * @return true if record is found in database, otherwise false.
	 * If record is found, it is changed for sure
	 */
	public boolean updateUserDetails(String username, String firstName, String lastName,
		Date dob, Date dateJoined) throws IOException {
		boolean changed = false;
		List<String> list = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(Constants.USER_CSV));
		String line = "";
		// Read all lines into a list
		while((line = br.readLine()) != null) {
			list.add(line);
		}
		br.close();
		// Update the list. If a desired value is found, update all values on that index
		// Also, mark if list is changed
		for(int i = 0; i < list.size(); ++i) {
			line = list.get(i);
			if(line.startsWith(username)) {
				list.remove(line);
				changed = true;
				SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
				String[] split = line.split(",");
				split[2] = firstName;
				split[3] = lastName;
				split[4] = sdf.format(dob);
				split[5] = sdf.format(dateJoined);
				line = split[0] + ", " + split[1] + ", " + split[2] + ", "
					+ split[3] + ", " + split[4] + ", " + split[5];
				list.add(line);
			}
		}
		// Write only if list is changed
		if(changed) {
			PrintWriter pw = new PrintWriter(new FileWriter(Constants.USER_CSV, false));
			for(String l: list) {
				pw.write(l);
			}
			pw.close();
		}
		return changed;
	}

	/**
	 * This method marks the user as PREMIUM user
	 */
	public void activatePremium() {
		this.membershipStatus = true;
	}

	/**
	 * This method checks if the user is a PREMIUM user
	 * @return true if PREMIUM user, false otherwise
	 */
	public boolean isPremiumMember() {
		return this.membershipStatus;
	}
}
