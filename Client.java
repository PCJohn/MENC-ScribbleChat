/**
*An instance of class Client is created for each connected client*
*Internetprogrammering 1 - Course
*@author Martin Carlsson
*/
import java.io.*;
import java.net.*;

    public class Client extends Thread {
        
        public Socket socket;
        public ClientHandler ch;	
        public BufferedReader in;
        public PrintWriter out;
        private String host;
        
        
        public Client(Socket socket, ClientHandler ch) {
            this.socket = socket;
            this.ch = ch;
            ch.clientCount++;
            try {
                ch.chatServer.newTitle(ch.serverSocket.getInetAddress().getLocalHost().getHostName(), ch.port, ch.clientCount);
            }
            catch(Exception e) {
            }
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                host = socket.getInetAddress().getHostName();
                out = new PrintWriter(socket.getOutputStream(),true);
                out.println("Welcome to " +ch.serverSocket.getInetAddress().getLocalHost().getHostName());
            } 
            catch(Exception e) {
                //Ignored
            }
                this.start();
        }
        
        
        // Sends a message to the client object
        public void sendMessage(String msg) {
            try {
                out.println(msg);
            }
            catch(Exception e) {
                ch.killClient(this);
            }
        }
        
        
        // Receives messages
        public void run() {
            String inStream=" ";
            try {
                while((inStream = in.readLine()) != null) {
                    ch.broadcast(socket.getInetAddress()+": " + inStream);
                    inStream=" ";
                } 
                out.close();
                in.close();
                socket.close();
            }
            catch(Exception e) {
            } 
            ch.killClient(this);
        } 
    } // End class Client
    
    