package server;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIServer {
    private static final int THREAD_POOL_SIZE = 10;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        KeyValueServiceImpl keyValueService = null;
        try{
            keyValueService = new KeyValueServiceImpl();

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("KeyValueService", keyValueService);
            System.out.println("RMI server is running and KeyValueService is bound.");
            synchronized (lock) {
                lock.wait();  // Block the thread indefinitely without busy-waiting
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        } finally {
            // Shutdown the executor service gracefully
            if (keyValueService != null) {
                keyValueService.shutdown();
            }
            executorService.shutdown();
        }

        final KeyValueServiceImpl finalKeyValueService = keyValueService;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            synchronized (lock) {
                // This will release the lock if you want to exit gracefully
                lock.notify();
            }
            if (finalKeyValueService != null) {
                finalKeyValueService.shutdown();
            }
            executorService.shutdown();
        }));

    }
}
