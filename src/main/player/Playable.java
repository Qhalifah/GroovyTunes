package player;

import org.json.simple.JSONObject;

public interface Playable {
    public JSONObject play();
    public JSONObject getAsJSON();
}