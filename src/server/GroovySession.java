package server;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;

public class GroovySession {

    private static GroovySession gSession;
    private org.eclipse.jetty.websocket.api.Session session;
    private Map<String, Object> map;

    public GroovySession(Session session) {
        this.session = session;
        map = new HashMap<>();
        gSession = this;
    }

    public static GroovySession getSession() {
        return gSession;
    }

    public void add(String key, Object object) {
        map.put(key, object);
    }

    public void remove(String key) {
        if(map.containsKey(key)) {
            map.remove(key);
        }
    }

    public boolean contains(String key) {
        return map.containsKey(key);
    }

    public Object get(String key) {
        if(map.containsKey(key))
            return map.get(key);
        else
            return null;
    }

    public RemoteEndpoint getRemote() {
        return session.getRemote();
    }
}