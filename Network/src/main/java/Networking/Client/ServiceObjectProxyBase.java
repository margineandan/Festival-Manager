package Networking.Client;

import Networking.ObjectProtocol.IRequest;
import Networking.ObjectProtocol.IResponse;
import Networking.ObjectProtocol.UpdateResponse;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ServiceObjectProxyBase {
    private final String host;
    private final int port;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket connection;
    private final BlockingQueue<IResponse> responseQueue = new LinkedBlockingQueue<>();
    private volatile boolean finished;

    public ServiceObjectProxyBase(String host, int port) {
        this.host = host;
        this.port = port;
    }

    protected void initializeConnection() {
        try {
            connection = new Socket(host, port);
            outputStream = new ObjectOutputStream(connection.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (Exception exception) {
            throw new RuntimeException("Error initializing connection", exception);
        }
    }

    private void startReader() {
        var readerThread = new Thread(new ServiceObjectProxyBase.ReaderThread());
        readerThread.start();
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
               try {
                   Object response = inputStream.readObject();
                    System.out.println("Response received: " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    }
                    else {
                        try {
                            responseQueue.put((IResponse) response);
                        } catch (InterruptedException exception) {
                            System.out.println("ReaderThread: " + exception.getMessage());
                            throw new RuntimeException(exception);
                        }
                    }
               } catch (EOFException exception) {
                   continue;
               } catch (IOException | ClassNotFoundException exception) {
                   System.out.println("ReaderThread: " + exception.getMessage());
                   throw new RuntimeException(exception);
               }
            }
        }
    }

    protected void sendRequest(IRequest request){
        try {
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException exception) {
            throw new RuntimeException("Error sending request: " + exception);
        }
    }

    protected IResponse readResponse(){
        IResponse response = null;
        try {
            response = responseQueue.take();
        } catch (InterruptedException exception) {
            throw new RuntimeException("Error receiving response: " + exception);
        }

        return response;
    }

    protected void testConnectionOpen() {
        if (connection == null || connection.isClosed()) {
            throw new RuntimeException("Connection is closed");
        }
    }

    public void closeConnection() {
        System.out.println("Closing connection...");
        finished = true;
        try {
            Thread.sleep(1000);
        } catch (Exception exception) {
            throw new RuntimeException("Error closing connection: " + exception);
        }
        try {
            inputStream.close();
            outputStream.close();
            connection.close();
            connection = null;
            System.out.println("Connection closed");
        } catch (IOException exception) {
            System.out.println("Error closing connection: " + exception.getMessage());
        }
    }

    protected abstract void handleUpdate(UpdateResponse response);
}
