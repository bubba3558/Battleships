package Network;

import GUI.Controller;
import GUI.LoginController;
import Logic.Game;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.*;

/**
 * Created by Martyna on 14.05.2017.
 */
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
    private LoginController controller;

    public NetworkManager(boolean isHost, int port, String serverIP, LoginController controller) throws IOException{
        this.isHost = isHost;
        this.serverIP = serverIP;
        this.port = port;
        this.controller=controller;

        if(isHost) {
            serverSocket = new ServerSocket(port,1);
            serverSocket.setSoTimeout(40000);
        }
        else {
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
                    e.printStackTrace();
                }
            }
            try {
                messageOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                messageListener = new MessageListener(clientSocket.getInputStream());

            }catch(IOException e){
                    sendErrorMessage("Nie udało sie utorzyc streamow");
                    closeConnections();
                    e.printStackTrace();
                }
            messageListener.start();
                Platform.runLater(new Runnable() {//connected so change scene and allow to play!
                    @Override
                    public void run() {

                        try {
                            controller.startGame();
                        } catch (IOException e) {
                            sendErrorMessage("oops cos poszlo nie tak przy ladowaniu widoku gry");
                            e.printStackTrace();
                        }
                    }
                });

        }

    }

    public void setGame(Game game) {//message handler
        this.game=game;
    }
    public void closeConnections() {
        try {
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
            if (clientSocket != null && !clientSocket.isClosed())
                clientSocket.close();
            if (messageOutput != null)
                messageOutput.close();
            if( messageListener != null)
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
            System.out.println("Could not get output to sent message");
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
                e.printStackTrace();
            } catch (IOException e) {
                game.haveLostConnection();
                System.err.println("Could not get input");
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Incorrect message");
                e.printStackTrace();
            }

        }
    }
    public void sendErrorMessage(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.getError(text);
            }
        });
    }
}
