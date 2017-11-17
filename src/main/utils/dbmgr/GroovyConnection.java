package utils.dbmgr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utils.Constants;

public class GroovyConnection {

	private static Connection conn = null;

	public static Connection getConnection() throws SQLException, ClassNotFoundException, IOException {
		if (conn == null || conn.isClosed()) {
			System.out.println("Connecting to the DB");
			conn = getFirstConnection();
			System.out.println("Connected to the DB");
		}
		return conn;
	}

	private static Connection getFirstConnection() throws ClassNotFoundException, IOException, SQLException {
		BufferedReader br = new BufferedReader(new FileReader(Constants.CONNECTION_STORAGE));
		String username = br.readLine().trim();
		String password = br.readLine().trim();
		String url = br.readLine();
		br.close();
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

	public static void closeConnection() throws SQLException {
		if (conn != null)
			conn.close();
	}
}
