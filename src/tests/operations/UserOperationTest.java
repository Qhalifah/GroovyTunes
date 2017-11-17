package operations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import user.User;
import utils.dbmgr.UserOperations;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserOperationTest {

	String username = "test-user";

	@Test
	public void testA_CreateUser() throws ClassNotFoundException, SQLException, IOException {
		String dateOfBirth = "1992-01-01";
		boolean created = UserOperations.createUser(username, "test-password", "test-firstname", "test-lastname",
				dateOfBirth);
		assertTrue(created);
	}

	@Test
	public void testB_IsUsernameAvailable() throws ClassNotFoundException, SQLException, IOException {
		boolean available = UserOperations.isUsernameAvailable(username);
		assertFalse(available);
	}

	@Test
	public void testC_FindUser() throws ClassNotFoundException, SQLException, IOException, ParseException {
		User user = UserOperations.findUser(username);
		boolean found = user.getUsername().equals(username)
				&& user.getFirstName().equals("test-firstname")
				&& user.getLastName().equals("test-lastname")
				&& user.getMembershipStatus().equals("REGULAR");
		assertTrue(found);
	}
	
	public void testD_ActivatePremium() throws ClassNotFoundException, SQLException, IOException {
		boolean premium = UserOperations.activatePremium(username);
		assertTrue(premium);
	}

	@Test
	public void testE_RemoveUser() throws ClassNotFoundException, SQLException, IOException {
		boolean removed = UserOperations.removeUser(username);
		assertTrue(removed);
	}

}
