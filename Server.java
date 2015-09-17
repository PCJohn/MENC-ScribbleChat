
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

    
    public class Server extends JFrame {
        

        
        private JTextArea  messageArea=new JTextArea(); 
        private JScrollPane jsp=new JScrollPane(messageArea); 
        private ClientHandler ch;
        private Calendar c;
       
        public Server(int port) 
        {
        ch=new ClientHandler(this, port); 
        messageArea=new JTextArea();	
        messageArea.setEditable(false);
        jsp=new JScrollPane(messageArea); 
        jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()  //I ADDED HERE- TAMSI!!!!
        {                                                                                           
            public void adjustmentValueChanged(AdjustmentEvent e){
                messageArea.select(messageArea.getHeight()+1000,0);
        }});
            
        // Container to put everything in
        Container con=getContentPane();
        con.setLayout(new BorderLayout());
        con.add(jsp,"Center");
        addWindowListener(closeWindow);
        setSize(600,300);
        show();
        }
        
        
        
        WindowAdapter closeWindow=new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                try{
                c=Calendar.getInstance();
                PrintWriter pout=new PrintWriter(new BufferedWriter(new FileWriter(("C:\\"+(Integer.toString(c.get(Calendar.YEAR))+Integer.toString(c.get(Calendar.MONTH))+Integer.toString(c.get(Calendar.DATE)))+".txt"), true)));
                pout.print("\n"+messageArea.getText());
                pout.close();
            } catch(IOException io)
            {}
                System.exit(0);
                
            }
        };
    
        
        
        public void newTitle(String host, int port, int clientCount) {
            
            setTitle("PriSais on " + host + " listening on port " + port + " Clients: " + clientCount);
            show();
            
        }
        
        
     
        public void showMessage(String msg) {
            
            messageArea.append(msg+"\n");
            
        }
        
        
       
        public static void main(String [] args){ 
            
            int port;
            try{
                port=Integer.parseInt(args[0]);
            }
            catch(Exception e){
                port=8563;
            }
            new Server(port);
        }
        
    } 