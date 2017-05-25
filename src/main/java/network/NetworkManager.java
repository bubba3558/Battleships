package network;

import model.Game;
import model.LoggingInterface;
import java.io.*;
import java.net.ServerSocket;
import java.net.*;


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
    LoggingInterface loggingInterface;

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
                    sendErrorMessage("Nikt do Ciebie nie dolaczyl w przewidzianym czasie");
                    closeConnections();
                }
            }
            try {
                messageOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                messageListener = new MessageListener(clientSocket.getInputStream());

            } catch (IOException e) {
                sendErrorMessage("Nie udało sie utorzyć streamow");
                closeConnections();
            }
            messageListener.start();

            loggingInterface.startGame();

        }

    }

    /**
     * after getting connection logging interface is no more need
     */
    public void setGame(Game game) {
        this.game = game;
        loggingInterface = null;
    }

    public void closeConnections() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            if (messageOutput != null) {
                messageOutput.close();
            }
            if (messageListener != null) {
                messageListener.MessageInput.close();
            }
        } catch (IOException e) {
            System.err.println("Error when closing connection");
        }
    }

    public boolean sendMessage(Message message) {
        if (!connected) {
            return false;
        }
        try {
            messageOutput.writeObject(message);
        } catch (IOException e) {
            System.err.println("Could not get output to sent message");
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
                sendErrorMessage("Nie udalo sie utowrzyc streamu dla wiadomosci");
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
                System.err.println("Can not recognise recived message");
                e.printStackTrace();
            } catch (IOException e) {
                game.haveLostConnection();
            } catch (Exception e) {
                System.err.println("Incorrect message");
                e.printStackTrace();
            }

        }
    }

    public void sendErrorMessage(String text) {
        loggingInterface.setError(text);
    }
}
