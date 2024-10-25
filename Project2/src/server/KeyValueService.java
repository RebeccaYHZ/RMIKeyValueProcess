package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.net.InetAddress;

public interface KeyValueService extends Remote {
    String processMap(String request, InetAddress clientAddress, int clientPort) throws RemoteException;
}
