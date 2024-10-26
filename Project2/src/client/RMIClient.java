package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.KeyValueService;
import java.util.logging.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RMIClient {

    private static final Logger LOGGER = Logger.getLogger(RMIClient.class.getName());
    private static final int THREAD_POOL_SIZE = 5;
    private static KeyValueService keyValueService;

    public static void main(String[] args) throws RemoteException {
        if (args.length != 2) {
            System.out.println("Usage: java RMIClient <server IP> <port number>");
            return;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            keyValueService = (KeyValueService) registry.lookup("KeyValueService");

            String[] commands = { "PUT", "GET", "DELETE" };
            String[][] keyValues = {
                    { "6650", "fall" },
                    { "3450", "spring" },
                    { "5520", "summer" },
                    { "5100", "winter" },
                    { "5200", "fall" }
            };

            for (String[] kv : keyValues) {
                final String command = "PUT " + kv[0] + " " + kv[1];
                executorService.submit(() -> sendRequest(command));
            }

            // Submit GET requests to the server
            for (String[] kv : keyValues) {
                final String command = "GET " + kv[0];
                executorService.submit(() -> sendRequest(command));
            }

            // Submit DELETE requests to the server
            for (String[] kv : keyValues) {
                final String command = "DELETE " + kv[0];
                executorService.submit(() -> sendRequest(command));
            }

            // Shutdown the executor service gracefully and wait for tasks to finish
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }

            // Handle user input in the main thread after all tasks are completed
            handleUserInput();

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            LOGGER.severe("Remote Exception: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.severe("Interrupted Exception: " + e.getMessage());
        }
    }

    private static void sendRequest(String command) {
        try {
            String response = keyValueService.processMap(command, null, 0);
            LOGGER.info("Response: " + response);
        } catch (RemoteException e) {
            LOGGER.severe("Failed to process command: " + command + ". Error: " + e.getMessage());
        }
    }

    private static void handleUserInput() {
        try (BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("Enter command (PUT/GET/DELETE <key> <value>) or 'exit' to quit: ");
                String userInput = userInputReader.readLine();

                // Log user input
                LOGGER.info("User entered: " + userInput);

                if (userInput.equalsIgnoreCase("exit")) {
                    LOGGER.info("User exited the program.");
                    System.out.println("Exiting...");
                    break;
                }

                sendRequest(userInput);
            }
        } catch (Exception e) {
            LOGGER.severe("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
