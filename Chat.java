/*GUI for the chat client for MENC chat.

Author: Prithvitji Chakrabarty (prithvichakra@gmail.com)
*/
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Calendar;


public class Chat extends JFrame implements ActionListener
{
    
    private static String host="localhost"; // Server/host to connect to
    private int port; // Port to be used for the connection
    private Socket theSocket;
    private Thread receiveactivity; 
    Chat c1;
        
    private JTextField writeField = new JTextField(60); // Text field for writing messages in
    private JTextArea messageArea = new JTextArea(); // Text area for messages received from the server
    private JScrollPane jsp = new JScrollPane(messageArea); // Scroll pane with the message area

    private JPanel mainPad=new JPanel();
    static  JPanel drawPad=new JPanel();
    private JScrollPane jsp2 = new JScrollPane(drawPad);
    private JPanel up = new JPanel(),down=new JPanel(), bottom=new JPanel(), right=new JPanel(), img=new JPanel(); // North and south panels
    private JButton sendButton = new JButton(), quit = new JButton(), clear= new JButton(),scribble=new JButton();         // Send and Quit buttons
    private ButtonGroup buttonGroup = new ButtonGroup();   
    private JRadioButton roman, arial, comic;
    private Receiver receiver; 
    private Sender sender; 
    private Calendar c;
    static Container con;

    
    /*Constructor*/
    public Chat(String host, int port)throws IOException {
        
        this.host=host;
        this.port=port;
        
        //Setting the grid layout.
        GridLayout lay=new GridLayout(0,2);
        mainPad.setLayout(lay);
        
        /*Container to put everything in*/
         con = getContentPane();
       // con.setLayout(lay);
        // Layout of the north panel
        up.setLayout(new FlowLayout(FlowLayout.RIGHT));
        up.setBackground(Color.lightGray);
        up.add(quit);
        up.add(clear);
        up.add(scribble);
        
       // Layout of the south panel
        down.setLayout(new FlowLayout(FlowLayout.LEFT));
        down.add(writeField);
        down.add(sendButton);
        
        //Layout of East Panel
        right.setLayout(new FlowLayout(FlowLayout.LEFT));
        Label label=new Label("Font:");
        right.add(label);
        
         drawPad.setBackground(Color.white);
         drawPad.setVisible(true);
         roman = new JRadioButton("Times New Roman");
         buttonGroup.add(roman);
         right.add(roman);
         arial = new JRadioButton("Arial");
         buttonGroup.add(arial);
         right.add(arial);
         roman.setSelected(true);
         comic = new JRadioButton("Comic Sans MS");
         buttonGroup.add(comic);
         right.add(comic);
         ImageIcon icon = new ImageIcon("C:\\JDeveloper\\mywork\\MENC\\Chat\\chat-25.png"); 
         JLabel label1 = new JLabel(); 
         label1.setIcon(icon); 
         img.add(label1);
                  
        // Prevent changes in the message area
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        
        
        // Layout of the text field
        writeField.setText(" ");
        writeField.requestFocus();
        
        
        // Text for the buttons
        sendButton.setText("SEND");
        quit.setText("QUIT");
        //quit.setBackground(Color.white);
        clear.setText("CLEAR");
       // clear.setBackground(Color.white);
        scribble.setText("SCRIBBLE");
        //scribble.setBackground(Color.white);
        
        
        
        /*Adds listener*/
        sendButton.addActionListener(this);
        quit.addActionListener(this);
        clear.addActionListener(this);
        writeField.addActionListener(this);
        arial.addActionListener(this);
        roman.addActionListener(this);
        comic.addActionListener(this);
        scribble.addActionListener(this);
        
        // Adds panels
        bottom.add(down,BorderLayout.SOUTH);
        con.add(up,BorderLayout.NORTH);
        bottom.add(right,BorderLayout.CENTER);
        con.add(bottom, BorderLayout.SOUTH);
        con.add(img, BorderLayout.EAST);
        
        //Adds scroll area with text area
        jsp.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()    //I added here -Vamsi
        {                                                                                           //The autoscroll after every update
            public void adjustmentValueChanged(AdjustmentEvent e){
                messageArea.select(messageArea.getHeight()+1000,0);
        }});
        con.add(mainPad,BorderLayout.CENTER);
        
        jsp2.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()    //I added here -Vamsi
        {                                                                                           //The autoscroll after every update
            public void adjustmentValueChanged(AdjustmentEvent e){
                //drawPad.select(drawPad.getHeight()+1000,0);
        }});
        con.add(mainPad,BorderLayout.CENTER);
        mainPad.add(jsp);
        mainPad.add(jsp2);
        
        
        //Sets the window size and makes it all visible
        setSize(1200,380);
        
       show();
        
        // Tries to create a socket
        try {
            theSocket = new Socket(host, port);
        }
        catch(UnknownHostException uHe) {
            messageArea.setText("Unable to connect to "+ host + " on " + port +".....closing");
            System.err.println("Unable to connect to "+ host + " on " + port +".....closing");
            setTitle("Chat Here: PriSais");
            try {
                Thread.sleep(30000); // Sleeps awhile to show messages
                System.exit(0); // Then closes
            }
            catch(InterruptedException e) {
            }
        }
        catch (IOException iE) {
            messageArea.setText("Unable to connect to " + host + " on " + port + ".....closing");
            System.err.println("Unable to connect to " + host + " on " + port + ".....closing");
                try{
                Thread.sleep(30000); // Sleeps awhile to show messages
                System.exit(0); // Then closes
            }
            catch(InterruptedException e) {
            }
        }
        sender=new Sender(theSocket); // Creates an instance of class Sender
        receiver=new Receiver(this, theSocket); // Creates an instance of class Receiver
        receiveactivity= new Thread(receiver); 
        receiveactivity.start(); // Starts the Receiver thread
    }
    
    
    // Used to clear the message field
    public void clearWriteField() {
        
        writeField.setText("");
    }
    
    // Message from server
    public void setIncomingMessage(String s) {
        if(s!=null){
            c= Calendar.getInstance();
        
        messageArea.append("\n"+ s);}
    }
        
    
    
    /*Decides what will happend when an user action is performed*/  
    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource()==writeField || ae.getSource()==sendButton) {
            
            String text=writeField.getText();
            int length=text.length();
            if(text.startsWith("hostconfig"))
            {
                String host2=text.substring(12,(length));
                try {
                new Chat(host2, 8563);
                c1.dispose();
                
            }
                
          
                catch(IOException io)
                {}
            }
            else {
            try {
                sender.sendAway(writeField.getText().trim());
                clearWriteField();
            }
            catch(Exception e) {
                messageArea.append("\n Not able to send message at this time");
                }
        } }
        else if(ae.getSource()==quit) {
            try {
            sender.closeOutStream();
            receiver.closeInStream();
            
                if(theSocket!=null) 
                    theSocket.close();
            }
            catch(IOException iE) {
                messageArea.append("\n Please wait...having some trouble closing..");
            }
            catch(NullPointerException nE) {
                messageArea.append("\n Please wait...having some trouble closing..");
            }
        System.exit(0); 
        }
         else if(ae.getSource()==clear) {
             messageArea.setText("");
            }
        else if(ae.getSource()==scribble){
            Scribble.beginScribble();
        }
         else if(ae.getSource()==arial){
                messageArea.setFont(new Font("Arial", Font.PLAIN, 14));}
                
         else if(ae.getSource()==roman)
         {
                messageArea.setFont(new Font("Times New Roman", Font.PLAIN, 14));
            }
            else if(ae.getSource()==comic)
         {
                messageArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
            }
        
                
            
    } 



    // Main
    public static void main (String[] args) {
        
        int port=8563; // default port
        String host="117.192.9.119"; // default host
        String portText = "port must be a number between"+
                    "0 and 65535";
        System.out.println("Welcome to Chat\n");  
        
        try {
            //If there are 2 or more arguments
            if(args.length>=2){
                host=args[0];
                port =Integer.parseInt(args[1]);
            }
            //If there is 1 argument, i.e. host
            else if(args.length==1){
                host=args[0];
            }
            // To make sure the port is within the valid range
            if(port > 65535 || port < 0)
                System.err.println(portText);
            else
                new Chat(host, port); // starts
        }
        catch(NumberFormatException nFe) {
            System.err.println(portText);
        }
        catch(Exception e){
            System.err.println();  
        }   
        
    }
}//End class Chat
