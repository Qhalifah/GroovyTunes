import java.util.Date;
public class UserAccountDetails{
	public String firstName;
	public String lastName;
	public Date dob;
	public Date dateJoined;
	public boolean membershipStatus;
	
	public UserAccountDetails(String firstName, String lastName, Date dob, Date dateJoined){
			this.firstName = firstName;
			this.lastName = lastName;
			this.dob = dob;
			this.dateJoined = dateJoined;
			this.membershipStatus = false;
	}
	
	public String getUserDetails(){
		return "";
	}
}