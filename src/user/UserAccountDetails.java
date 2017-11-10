package user;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

import utils.Constants;
import utils.UserOperations;

/**
 * This class stored all details related to the user.
 * It includes username, password, firstname, lastname, date of birth (dateOfBirth), date on which user
 * joined (dateJoined).
 * It also tracks if the user is PREMIUM user or REGULAR user
 * It has 1:1 relationship with User class
 */
public class UserAccountDetails {
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private Date dateJoined;
	private boolean membershipStatus;

	/**
	 * Creates an object of UserAccountDetails and assigns the given parameters
	 * @param username: username
	 * @param password: password
	 * @param firstName: first name of user
	 * @param lastName: last name of user
	 * @param dateOfBirth: Date of birth
	 * @param dateJoined: Date on which user joined GroovyTunes
	 */
	public UserAccountDetails(String username, String password, String firstName, String lastName,
		Date dob, Date dateJoined, boolean membershipStatus) {
 		this.username = username;
 		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dob;
		this.dateJoined = dateJoined;
		this.membershipStatus = false;
	}

	/**
	 * Returns details of the user in the form of JSON.
	 * Details does not include username and password
	 * @return Details of the user in JSON format
	 */
	@SuppressWarnings("unchecked")
	public JSONObject getUserDetails() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		JSONObject userDetails = new JSONObject();
		userDetails.put("firstName", this.firstName);
		userDetails.put("lastName", this.lastName);
		userDetails.put("dateOfBirth", sdf.format(this.dateOfBirth));
		userDetails.put("dateJoined", sdf.format(this.dateJoined));
		userDetails.put("membershipStatus", this.membershipStatus);
		return userDetails;
	}

	/**
	 * This method updates the details stored in the database.
	 * @param username: username. Also used as key
	 * @param firstName: new value of first name. If unchanged, send the previous value
	 * @param lastName: new value of last name. If unchanged, send the previous value
	 * @param dateOfBirth: new value of date of birth. If unchanged, send the previous value
	 * @param dateJoined. send the previous value. It makes no sense to change this
	 * @return true if record is found in database, otherwise false.
	 * If record is found, it is changed for sure
	 */
	public boolean updateDetails(String username, String firstName, String lastName,
		Date dob, Date dateJoined) {
		return false;
	}

	/**
	 * This method marks the user as PREMIUM user
	 */
	public void activatePremium() {
		this.membershipStatus = true;
		UserOperations.activatePremium(username);
	}

	/**
	 * This method checks if the user is a PREMIUM user
	 * @return true if PREMIUM user, false otherwise
	 */
	public boolean isPremiumMember() {
		return this.membershipStatus;
	}

	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getDateOfBirth() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		return sdf.format(dateOfBirth);
	}
	public String getDateJoined() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		return sdf.format(dateJoined);
	}
	public String getMembershipStatus() {
		return membershipStatus ? "PREMIUM": "REGULAR";
	}
}
