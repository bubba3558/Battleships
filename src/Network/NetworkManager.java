package Network;

import Logic.Game;

import java.io.*;
import java.net.ServerSocket;
import java.net.*;

/**
 * Created by Martyna on 14.05.2017.
 */
public class NetworkManager {
    private Game game;
    private boolean isHost;
    private boolean connected=false ;
    private int port;
    private String serverIP;
    private ObjectOutputStream messageOutput = null;
    private MessageListener messageListener = null;
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;

    public NetworkManager( boolean isHost, int port, String serverIP) {
        this.isHost = isHost;
        this.serverIP = serverIP;
        this.port = port;
    }
    public boolean initConnection()  {
        try{
             if (isHost) {
                serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(40000);
                clientSocket = serverSocket.accept();
                connected = true;
            } else {
            clientSocket = new Socket(serverIP, port);
            }
        }catch (IOException e) {
            System.err.println("Could not connect");
            e.printStackTrace();
            return false;
            }
        try {
           messageOutput = new ObjectOutputStream(clientSocket.getOutputStream());
           messageListener = new MessageListener(clientSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("could not get input or output");
            closeConnections();
            return false;
        }
        run();
    return true;
    }
    public void setGame(Game game) {//message handler
        this.game=game;
    }
    public void run() {
        (new ServerThread()).start();
    }

    public void closeConnections() {
        try {
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
            if (clientSocket != null && !clientSocket.isClosed())
                clientSocket.close();
        } catch (IOException e) {
            System.err.println("Server could not made connection");
            e.printStackTrace();
        }
    }
    public boolean sendMessage(Message message) {
        if (!connected)
            return false;
        try {
            messageOutput.writeObject(message);
        } catch (IOException e) {
            System.out.println("Could not get output to sent message");
            e.printStackTrace();
            return false;
        }
        return true;
    }
    private class ServerThread extends Thread {

        public void run() {
//            try {
//                if (isHost) {
//                    serverSocket = new ServerSocket(port);
//                    serverSocket.setSoTimeout(40000);
//                    clientSocket = serverSocket.accept();
//                    connected = true;
//                } else {
//                    clientSocket = new Socket(serverIP, port);
//                }
//            }catch (IOException e) {
//                    System.err.println("Could not connect");
//                    e.printStackTrace();
//                }
//
//            try {
//                messageOutput = new ObjectOutputStream(clientSocket.getOutputStream());
//                messageListener = new MessageListener(clientSocket.getInputStream());
//            } catch (IOException e) {
//                System.err.println("could not get input or output");
//                closeConnections();
//            }
            messageListener.start();
            //debug
            connected = true;
            System.out.println("powstal serwer");
            //nie zamknie sie za szybko?

        }
    }

    private class MessageListener extends Thread {
        ObjectInputStream MessageInput;

        public MessageListener(InputStream stream) {
            try {
                MessageInput = new ObjectInputStream(stream); //!!aa moze bebznwej instan cji
            } catch (IOException e) {
                System.err.println("Could not get input");
            }
        }

        public void run() {//receive messages
            Message message;

            try {
                while (true) {
                    while ((message = (Message) MessageInput.readObject()) != null) {
                        game.handleMessage(message);
                    }
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Can not recognise recived message");
            } catch (IOException e) {
                System.err.println("Could not get input");
            } catch (Exception e) {
                System.err.println("Incorrect message");
                e.printStackTrace();
            }

        }
    }
    public boolean isConnected(){
        return connected;
    }
}
