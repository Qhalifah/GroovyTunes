package auth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import user.User;
import utils.Constants;

public class Authenticate {

    /**
     * This method authenticates user. It takes in user name and password as string, and checks it against
     * values in database. If there exists a user, it returns the user.
     * @param username: user name provided at the login
     * @param password: password provided at the login
     * @return User u if there exists a user. NULL otherwise
     * @throws IOException if unable to read the database
     * @throws ParseException if unable to parse date from string
     */
    public static User authUser(String username, String password) throws IOException, ParseException {
        User u = null;
        BufferedReader br = new BufferedReader(new FileReader(Constants.USER_CSV));
        String line = "";
        while((line = br.readLine()) != null) {
            if(!line.startsWith("#")) {
                String[] split = line.split(",");
                if(split[0].trim().equals(username) && split[1].trim().equals(password)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                    Date dob = sdf.parse(split[4].trim());
                    Date doj = sdf.parse(split[5].trim());
                    u = new User(split[0].trim(), split[1].trim(), split[2].trim(), 
                        split[3].trim(), dob, doj);
                }
            }
        }
        br.close();
        return u;
    }
}