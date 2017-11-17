package utils.dbmgr;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;

import user.User;
import utils.Constants;

import java.sql.Date;

public class UserOperations {

	public static boolean isUsernameAvailable(String name) throws ClassNotFoundException, SQLException, IOException {
		boolean available = true;
		String query = "SELECT username from " + Constants.USER_TABLE + " where username=?";
		PreparedStatement stmt = GroovyConnection.getConnection().prepareStatement(query);
		stmt.setString(1, name);
		ResultSet set = stmt.executeQuery();
		if (set.next()) {
			available = false;
		}
		return available;
	}

	public static boolean createUser(String name, String password, String firstName, String lastName, String dob)
			throws ClassNotFoundException, SQLException, IOException {
		boolean userCreated = true;
		String query = "INSERT INTO " + Constants.USER_TABLE
				+ "(username, password, firstname, lastname, date_of_birth, date_joined, status) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		PreparedStatement stmt = GroovyConnection.getConnection().prepareStatement(query);
		stmt.setString(1, name);
		stmt.setString(2, password);
		if (firstName != null)
			stmt.setString(3, firstName);
		else
			stmt.setNull(3, Types.VARCHAR);
		if (lastName != null)
			stmt.setString(4, lastName);
		else
			stmt.setNull(4, Types.VARCHAR);
		if (dob != null)
			stmt.setDate(5, Date.valueOf(dob));
		else
			stmt.setNull(6, Types.DATE);
		stmt.setDate(6, Date.valueOf(sdf.format(new java.util.Date())));
		stmt.setBoolean(7, false);
		try {
			stmt.execute();
		} catch (SQLException ex) {
			ex.printStackTrace();
			userCreated = false;
		}
		return userCreated;
	}

	public static User findUser(String username) throws ClassNotFoundException, SQLException, IOException {
		User u = null;
		String query = "SELECT * from " + Constants.USER_TABLE + " WHERE username = ?";
		PreparedStatement stmt = GroovyConnection.getConnection().prepareStatement(query);
		stmt.setString(1, username);
		ResultSet set = stmt.executeQuery();
		if (set.next()) {
			u = new User(set.getString("username"), 
					set.getString("password"), 
					set.getString("firstname"),
					set.getString("lastname"), 
					set.getDate("date_of_birth"),
					set.getDate("date_joined"),
					set.getBoolean("status"));
		}
		return u;
	}

	public static void activatePremium(String username) throws ClassNotFoundException, SQLException, IOException {
		String query = "UDPATE " + Constants.USER_TABLE + " SET status = ? WHERE username = ?";
		PreparedStatement stmt = GroovyConnection.getConnection().prepareStatement(query);
		stmt.setBoolean(1, true);
		stmt.setString(1, username);
		stmt.executeUpdate();
	}

}
