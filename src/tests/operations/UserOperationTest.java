package operations;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

import utils.dbmgr.UserOperations;

public class UserOperationTest {

	@Test
	public void testIsUsernameAvailable() throws ClassNotFoundException, SQLException, IOException {
		String username = "test-user";
		boolean available = UserOperations.isUsernameAvailable(username);
		assertFalse(available);
	}
}
