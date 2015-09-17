
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Calendar;



public class Login extends JFrame implements ActionListener 
{
    
    private static String host; 
    
    private Socket theSocket;
    private Thread receiveactivity; 
    
        
    private JTextField hostconfig = new JTextField(18), cname = new JTextField(15); 


    private JPanel up = new JPanel(), img=new JPanel(); 
    private JButton sendButton = new JButton();      
    private static String name;

    
    /*Constructor*/
    public Login()throws IOException {
        Container con = getContentPane();
        hostconfig.setText("localhost");
        cname.setText("");
        hostconfig.requestFocus();
        cname.requestFocus();
        sendButton.setText("SEND");
        
        
        Label label=new Label("Host Server: ");
        Label label1=new Label("Nickname: ");
        
        up.add(label);
        up.add(hostconfig);
        up.add(label1);
        up.add(cname);
        up.add(sendButton);
        
        
        
        sendButton.addActionListener(this);
        cname.addActionListener(this);
        
        con.add(up,BorderLayout.CENTER);
        setSize(380,180);
        
       show();
    }
     public void actionPerformed(ActionEvent ae) {
         if(ae.getSource()==cname || ae.getSource()==sendButton) {
            
            String text=hostconfig.getText();
            int length=text.length();
            name=cname.getText();
            try{
            if(length<16)
            {
                host=text;
                new Chat(host,8563);
            }
        }catch(IOException ie)
        {}
        }
    }
    public static String gethostconfig()throws Exception
    {
        return host;
    }
    public static String getcname() throws Exception
    {
        return name;
    }
    public static void main(String[] args)throws Exception
    {
       new Login();
    }
}
       