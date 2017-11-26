package player;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.dbmgr.UserOperations;

public class RegularPlayerTest {

	@BeforeClass
	public static void addUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.createUser("test-user", "test-password", "test-firstname", "test-lastname", "1993-01-01");
		UserOperations.activatePremium("test-user");
	}
	
	@Test
	public void test() {
		
	}
	
	@AfterClass
	public static void removeUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.removeUser("test-user");
	}
}
