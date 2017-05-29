package network;

import gui.LoggingInterface;
import model.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class NetworkManager {
    private Game game;
    private boolean isHost;
    private boolean connected = false;
    private int port;
    private String serverIP;
    private ObjectOutputStream messageOutput = null;
    private MessageListener messageListener = null;
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;
    private LoggingInterface loggingInterface;

    public NetworkManager(boolean isHost, int port, String serverIP, LoggingInterface loggingInterface) throws IOException {
        this.isHost = isHost;
        this.serverIP = serverIP;
        this.port = port;
        this.loggingInterface = loggingInterface;

        if (isHost) {
            serverSocket = new ServerSocket(port, 1);
            serverSocket.setSoTimeout(40000);
        } else {
            clientSocket = new Socket(serverIP, port);
            connected = true;
        }
    }

    public void run() {
        (new ServerThread()).start();
    }

    private class ServerThread extends Thread {

        public void run() {
            if (isHost) {
                try {
                    clientSocket = serverSocket.accept();
                    connected = true;
                } catch (IOException e) {
                    loggingInterface.setError(ErrorType.connectionTimeOut);
                    return;
                }
            }
            try {
                messageOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                messageListener = new MessageListener(clientSocket.getInputStream());

            } catch (IOException e) {
                loggingInterface.setError(ErrorType.couldNotCreateStream);
                closeConnections();
                e.printStackTrace();
                return;
            }
            messageListener.start();
            loggingInterface.startGame();

        }

    }

    public void setGame(Game game) {//message handler
        this.game = game;
    }

    public void closeConnections() {
        try {
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
            if (clientSocket != null && !clientSocket.isClosed())
                clientSocket.close();
            if (messageOutput != null)
                messageOutput.close();
            if (messageListener != null)
                messageListener.MessageInput.close();
        } catch (IOException e) {
            System.err.println("Error when closing connection");
            e.printStackTrace();
        }
    }

    public boolean sendMessage(Message message) {
        if (!connected)
            return false;
        try {
            messageOutput.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class MessageListener extends Thread {
        ObjectInputStream MessageInput;

        public MessageListener(InputStream stream) {
            try {
                MessageInput = new ObjectInputStream(stream);
            } catch (IOException e) {
                loggingInterface.setError(ErrorType.getCouldNotCreateInputStream);
                e.printStackTrace();
                closeConnections();
            }
        }

        /**
         * receive messages
         */
        public void run() {
            Message message;

            try {
                while (true) {
                    while ((message = (Message) MessageInput.readObject()) != null) {
                        game.handleMessage(message);
                    }
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Can not recognise received message");
                e.printStackTrace();
            } catch (IOException e) {
                game.haveLostConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}