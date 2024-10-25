package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class KeyValueServiceImpl extends UnicastRemoteObject implements KeyValueService {
    private final KeyValueProcess keyValueProcess;
    private final ExecutorService executorService;

    public KeyValueServiceImpl() throws RemoteException {
        super();
        this.keyValueProcess = new KeyValueProcess();
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public String processMap(String s, InetAddress clientAddress, int clientPort) throws RemoteException {
        // Submit the processMap task to the executor service and get the result using Callable and Future
        Callable<String> task = () -> keyValueProcess.processMap(s, clientAddress, clientPort);
        Future<String> futureResult = executorService.submit(task);

        try {
            // Wait for the task to complete and retrieve the result
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RemoteException("Error processing map request", e);
        }
    }

    public void shutdown() {
        executorService.shutdown();
        // Optionally wait for termination
        try {
            if (!executorService.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Force shutdown if not terminated
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt(); // Preserve interrupt status
        }
    }
}
