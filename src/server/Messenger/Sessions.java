package server.Messenger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sessions {
    private Map<String, Session> sessions = new HashMap<>();

    public List<Session> getSessions() {
        List<Session> sessionList = new ArrayList<>( sessions.values() );
        return sessionList;
    }

    public Session createSession() {
        Session newSession = new Session();
        sessions.put( newSession.getSessionID(), newSession );
        return newSession;
    }

    public Session getSession( String sessionID ) {
        return ( sessions.containsKey( sessionID ) ) ? sessions.get( sessionID ) : null;
    }

    public void removeSession( String sessionID ) {
        if ( sessions.containsKey( sessionID ) ) {
            sessions.remove( sessionID );
        }
    }
}
