package player;

import org.json.simple.JSONObject;

public interface Playable {
    public String[] play();
    public JSONObject getAsJSON();
}