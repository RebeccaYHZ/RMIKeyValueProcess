package server;

import java.util.concurrent.ConcurrentHashMap;
import java.net.*;

// Class to handle key-value storage operations such as PUT, GET, and DELETE
public class KeyValueProcess {
    // A HashMap to store the key-value pairs
    private final ConcurrentHashMap<String, String> map;

    // Constructor that initializes the HashMap
    public KeyValueProcess() {
        this.map = new ConcurrentHashMap<>();
    }

    public synchronized String processMap(String s, InetAddress clientAddress, int clientPort) {
        long timestamp = System.currentTimeMillis();
        String[] items = s.split(" ");
        String action = items[0].toUpperCase();

        return switch (action) {
            case "PUT" -> {
                if (items.length != 3) {
                    System.out.printf("%d - Received malformed PUT request from %s:%d%n", timestamp, clientAddress, clientPort);
                    yield "ERROR: PUT command must have 2 arguments: PUT <key> <value>";
                }
                yield handlePut(items[1], items[2]);
            }
            case "GET" -> {
                if (items.length != 2) {
                    System.out.printf("%d - Received malformed GET request from %s:%d%n", timestamp, clientAddress, clientPort);
                    yield "ERROR: GET command must have 1 argument: GET <key>";
                }
                yield handleGet(items[1]);
            }
            case "DELETE" -> {
                if (items.length != 2) {
                    System.out.printf("%d - Received malformed DELETE request from %s:%d%n", timestamp, clientAddress, clientPort);
                    yield "ERROR: DELETE command must have 1 argument: DELETE <key>";
                }
                yield handleDelete(items[1]);
            }
            default -> "ERROR: Unknown command: " + action;
        };
    }

    // Handles the PUT command to add or update a key-value pair
    private String handlePut(String k, String v) {
        map.put(k, v);
        return "Successfully put " + k + " = " + v;
    }

    // Handles the GET command to retrieve the value associated with a key
    private String handleGet(String k) {
        if (!map.containsKey(k)) {
            return "ERROR: Key not found: " + k;
        }
        return "Successfully get " + k + " = " + map.get(k);
    }

    // Handles the DELETE command to remove a key-value pair
    private String handleDelete(String k) {
        if (!map.containsKey(k)) {
            return "ERROR: Key not found: " + k;
        }
        map.remove(k);
        return "Successfully delete " + k;
    }
}
