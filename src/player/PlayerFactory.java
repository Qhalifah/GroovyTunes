package player;

import user.User;

public abstract class PlayerFactory {

	public static Player getPlayer(User user) {
		Player player = null;
		if(user.isPremiumUser()) {
			player = new PremiumPlayer();
		} else {
			player = new RegularPlayer();
		}
		return player;
	}
}
