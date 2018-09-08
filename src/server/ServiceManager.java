package server;

import server.Messenger.Session;
import server.Messenger.Sessions;
import server.RestServices.MessengerRestService.MessengerRestService;

import java.util.ArrayList;

public class ServiceManager {
    public static Sessions activeSessions = new Sessions();
}