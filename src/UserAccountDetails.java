import java.util.Date;
import org.json.simple.JSONObject;

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
		JSONObject obj = new JSONObject();
		obj.put("firstName", this.firstName);
		obj.put("lastName", this.lastName);
		obj.put("dob", this.dob);
		obj.put("dateJoined", this.dateJoined);
		obj.put("membershipStatus", this.membershipStatus);
		return obj.toString();
	}
}