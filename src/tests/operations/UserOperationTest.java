package operations;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;

import utils.dbmgr.UserOperations;

public class UserOperationTest {

	@Test
	public void testIsUsernameAvailable() throws ClassNotFoundException, SQLException, IOException {
		File f = new File("somefile");
		f.createNewFile();
		String username = "test-user";
		boolean available = UserOperations.isUsernameAvailable(username);
		assertTrue(available);
	}
}
