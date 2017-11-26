package user;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import auth.Authenticate;
import auth.Authenticate.AUTH_RESULT;
import auth.Authenticate.AuthResult;
import utils.dbmgr.UserOperations;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTest {

	@BeforeClass
	public static void addUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.createUser("test-user", "test-password", "test-firstname", "test-lastname", "1993-01-01");
	}

	@Test
	public void testA_GetPlayer() throws ClassNotFoundException, SQLException, IOException {
		AuthResult authUser = Authenticate.authUser("test-user", "test-password");
		boolean success = false;
		if (authUser.getResult() == AUTH_RESULT.INCORRECT_PASSWORD || authUser.getResult() == AUTH_RESULT.NO_SUCH_USER)
			assertTrue(success);
		success = authUser.getUser().getPlayer() != null;
		assertTrue(success);
	}

	@Test
	public void testB_Premium() throws ClassNotFoundException, SQLException, IOException {
		AuthResult authUser = Authenticate.authUser("test-user", "test-password");
		boolean success = false;
		if (authUser.getResult() == AUTH_RESULT.INCORRECT_PASSWORD || authUser.getResult() == AUTH_RESULT.NO_SUCH_USER)
			assertTrue(success);
		User user = authUser.getUser();
		if (user.isPremiumUser() == true)
			assertTrue(success);
		user.activatePremium();
		assertTrue(user.isPremiumUser());
	}

	@Test
	public void testC_GetUserDetails() throws ClassNotFoundException, SQLException, IOException {
		AuthResult authUser = Authenticate.authUser("test-user", "test-password");
		boolean success = false;
		if (authUser.getResult() == AUTH_RESULT.INCORRECT_PASSWORD || authUser.getResult() == AUTH_RESULT.NO_SUCH_USER)
			assertTrue(success);
		User user = authUser.getUser();
		JSONObject userInfo = user.getUserInfo();
		success = ((String) userInfo.get("firstName")).equals("test-firstname")
				& ((String) userInfo.get("lastName")).equals("test-lastname")
				& ((boolean) userInfo.get("membershipStatus") == false);
		assertTrue(success);
	}

	@AfterClass
	public static void removeUser() throws ClassNotFoundException, SQLException, IOException {
		UserOperations.removeUser("test-user");
	}
}
