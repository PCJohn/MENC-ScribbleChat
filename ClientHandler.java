/*
Class to handle multiple clients as a group.

Author: Prithvijit Chakrabarty (prithvichakra@gmail.com)
*/
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    
    public  ServerSocket serverSocket;
    public int port; // Port the server listens to
    public int clientCount; // Number of clients
    public Server chatServer; 
    private ArrayList clientArr; // Array of clients
    private Thread thread;
    
    /*Constructor*/
    public ClientHandler(Server chatServer, int port) {
        this.chatServer = chatServer;
        this.port = port;
        try{
            serverSocket=new ServerSocket(port);
        }
        catch(Exception e) {
            System.err.println("Can't make a socketconnection " + e);
            System.exit(0);
        } 
        clientCount = 0;
        try {
            chatServer.newTitle(serverSocket.getInetAddress().getLocalHost().getHostName(), port, clientCount);
        }
        catch(Exception e) {
        }
        clientArr=new ArrayList();
        thread=new Thread(this);
        thread.start(); // starts this thread
    }
        
    /*Looks for new clients and adds them to an ArrayList*/
    public void run() {
        while(true) {
            try {
                Socket socket=serverSocket.accept();
                chatServer.showMessage(socket.getInetAddress() + " is connected");
                clientArr.add(new Client(socket, this));
            } 
            catch(Exception e){
            }
        } 
    } 
        
    
    /*Sends a message to all connected clients*/
    public synchronized void broadcast(String msg) {
        chatServer.showMessage(msg);
        for(int i=0; i < clientArr.size(); i++) {
            Client client=(Client)clientArr.get(i);
            client.sendMessage(msg);
        }
    }
    
    //Kills and removes a client from the ArrayList
    public synchronized void killClient(Client c) {
        String client=c.socket.getInetAddress().toString();
        try{
            c.in.close();
            c.out.close();
            c.socket.close();
            for(int i=0;i<clientArr.size();i++) {
                if(((Client)clientArr.get(i)).equals(c)){
                    clientArr.remove(i);
                }
            }
            clientArr.trimToSize();
            clientCount--;
            chatServer.newTitle(serverSocket.getInetAddress().getLocalHost().getHostName(), port, clientCount);
        }
        catch(Exception e) {
        }
        broadcast(client +" has disconnected");
    } 
} //End class ClientHandler
