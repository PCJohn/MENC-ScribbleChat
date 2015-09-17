
import java.net.*;
import java.io.*;
import java.util.*;

public class Sender {
    
    private PrintWriter out;
    private Calendar c;

    public Sender(Socket theSocket) {
        
        try {
            out = new PrintWriter(theSocket.getOutputStream(), true);
        }
        catch(IOException iE) {
        }
    }
    
    /*Closes the outgoing stream*/
    public void closeOutStream() {
        
        if(out != null) 
            out.close();
    }
    
    /*Sends a message to the server*/
    public void sendAway(String s)throws Exception  {
        
        if(out !=null)
            c=Calendar.getInstance();
            out.println(Login.getcname()+"\t"+c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR)+" "+(c.get(Calendar.HOUR)+12)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+"\t"+s);
    }
} // End class Sender