
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class Receiver implements Runnable {
    
    private BufferedReader in;
    private String host;
    private Chat chat;
    private Calendar c= Calendar.getInstance();

    public Receiver(Chat chat, Socket theSocket)  {
        
        this.chat = chat;
        try {
            in = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));
            chat.setTitle("Chat Client on: "+theSocket.getInetAddress().getHostName()+
            " :port " + theSocket.getPort());
        }
        catch (IOException ioe) {
        }
    }
    
    /*Reads messages from the server and makes callbacks to the GUI class*/
    public void run() {
        
        try{
            String inStream;
            while (( inStream =in.readLine()) != null)
            {
                c= Calendar.getInstance();
                chat.setIncomingMessage(inStream);
            } 
        }
        catch (IOException ioe) {
        } 
         catch (NullPointerException npe) {
        } 
    } 
    
    /*Closes the stream*/
    public void closeInStream() {
        
        try {
            if(in !=null)
                in.close();
        }
        catch(IOException iE) {
        } 
    } 
} // End class Receiver	
