/*
Paint application to handle user's scribbles. Simple javax.swing to track mous coordinates when clicked.
*/
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.lang.Math.*;
import java.text.*;
import java.io.*;
import java.awt.Scrollbar.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.SwingUtilities;
//import javax.jnlp.*;

public class Scribble extends Applet implements ActionListener, AdjustmentListener, MouseListener, MouseMotionListener
{
 /* Maximum X and Maximum Y coordinate values. */
 private final int MAX_X           = 800;
 private final int MAX_Y           = 600;

 /* Operation Constants */
 private final int  NO_OP          = 0;
 private final int  PEN_OP         = 1;
 private final int  LINE_OP        = 2;
 private final int  ERASER_OP      = 3;
 private final int  CLEAR_OP       = 4;

 /* Current mouse coordinates */
 private int mousex                = 0;
 private int mousey                = 0;

 /* Previous mouse coordinates */
 private int prevx                 = 0;
 private int prevy                 = 0;

 /* Initial state falgs for operation */
 private boolean initialPen        = true;
 private boolean initialLine       = true;
 private boolean initialEraser     = true;

 /* Main Mouse X and Y coordiante variables */
 private int  Orx                  = 0;
 private int  Ory                  = 0;
 private int  OrWidth              = 0;
 private int  OrHeight             = 0;
 private int  drawX                = 0;
 private int  drawY                = 0;
 private int  eraserLength         = 5;
 private int  udefRedValue         = 255;
 private int  udefGreenValue       = 255;
 private int  udefBlueValue        = 255;

 /* Primitive status & color variables */
 private int    opStatus           = PEN_OP;
 private int    colorStatus        = 1;
 private Color  mainColor          = new Color(0,0,0);
 private Color  xorColor           = new Color(255,255,255);
 private Color  statusBarColor     = new Color(166,166,255);
 private Color  userDefinedColor   = new Color(udefRedValue,udefGreenValue,udefBlueValue);

 /* Operation Button definitions */
 private JButton penButton          = new JButton("Pen");
 private JButton lineButton         = new JButton("Line");
 private JButton eraserButton       = new JButton("Eraser");
 private JButton clearButton        = new JButton("Clear");
 private JButton sendButton         = new JButton("Send");
 
 /* Labels for operation and color fields */
 private Label operationLabel      = new Label("   Tool mode:");
 private Label colorLabel          = new Label("   Color mode:");
 private Label cursorLabel         = new Label("   Cursor:");

 /* Sub panels of the main applet */
 private Panel controlPanel        = new Panel(new GridLayout(11,2,0,0));
 //static Panel drawPanel           = new Panel();
 private Panel statusPanel         = new Panel();
 private Panel udefcolPanel        = new Panel(new GridLayout(3,2,0,0));
 private Panel udefdemcolPanel     = new Panel();
 private JPanel drawPanel           = new JPanel();

 public void init()
 {
    setLayout(new BorderLayout());

    /* setup operation buttons */
    controlPanel.add(penButton);
    controlPanel.add(lineButton);
    controlPanel.add(eraserButton);
    controlPanel.add(clearButton);
    controlPanel.add(sendButton);
   
    controlPanel.setBounds(0,0,100,300);
    controlPanel.add(udefcolPanel);
    controlPanel.add(udefdemcolPanel);

    /* Add user-defined RGB buttons to panel */
    


 
  
    drawPanel.setSize(50,50);
    statusPanel.setBackground(statusBarColor);
    controlPanel.setBackground(Color.white);
    drawPanel.setBackground(Color.white);
    add(statusPanel, "North");
    add(controlPanel, "West");
    add(drawPanel, "Center");

    /* Setup action listener */
    penButton.addActionListener(this);
    lineButton.addActionListener(this);
    eraserButton.addActionListener(this);
    clearButton.addActionListener(this);
    sendButton.addActionListener(this);
   
    /* Adding component listeners to main panel (applet) */
    drawPanel.addMouseMotionListener(this);
    drawPanel.addMouseListener(this);
    this.addMouseListener(this);
    this.addMouseMotionListener(this);

   // updateRGBValues();

 }


 /*
    Method is called when an action event has been preformed.
    All button operations and some labels, text field operations
    are handled in this method.
 */
 public void actionPerformed(ActionEvent e)
 {
    /* Determine what action has occured */
    /* Set the relative values           */

    if (e.getActionCommand() == "Pen")
       opStatus = PEN_OP;

    if (e.getActionCommand() == "Line")
       opStatus = LINE_OP;

    if (e.getActionCommand() == "Eraser")
       opStatus = ERASER_OP;

    if (e.getActionCommand() == "Clear")
       opStatus = CLEAR_OP;

     if (e.getActionCommand() == "Send")
     {
       // Chat.drawPad.setIcon(new ImageIcon(createImage(drawPanel)));
        // Chat.drawPad.setIcon((Icon)(createImage(drawPanel)));
        createImage(drawPanel);
     }
     
    setMainColor();
   
 }


 public void adjustmentValueChanged(AdjustmentEvent e)
 {
    //updateRGBValues();
 }


 /*
   Method will clear the whole drawPanel with
   the current background color
 */
 public void clearPanel(Panel p)
 {
   // opStatusBar.setText("Clear");
    Graphics g = p.getGraphics();
    g.setColor(p.getBackground());
    g.fillRect(0,0,p.getBounds().width,p.getBounds().height);
  }


 /*
   Method will emulate a pen style graphic.
   by drawing a line from the previous mouse corrdinates
   to the current mouse coordinates.

   Note: In initial attempt the previous mouse coordinates
         are set to the current mouse coordinates so as
         not to begin the pen graphic from an unwanted
         arbitrary point.
 */
 public void penOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialPen)
    {
       setGraphicalDefaults(e);
       initialPen = false;
       g.drawLine(prevx,prevy,mousex,mousey);
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       /*
          set mouse coordinates to
          current mouse position
       */
       mousex = e.getX();
       mousey = e.getY();

       /*
          draw a line from the previous mouse coordinates
          to the current mouse coordinates
       */
       g.drawLine(prevx,prevy,mousex,mousey);

       /*
          set the current mouse coordinates to
          previous mouse coordinates for next time
       */
       prevx = mousex;
       prevy = mousey;
    }
 }


 /*
   Method will emulate a line drawing graphic.
   By drawing a shadow line for an origin mouse
   coordinate pair to a moving mouse coordinate
   pair, until the mouse has been release from
   dragmode.
 */
 public void lineOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor);

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialLine)
    {
       setGraphicalDefaults(e);
       g.setXORMode(xorColor);
       g.drawLine(Orx,Ory,mousex,mousey);
       initialLine=false;
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       /*
         Delete previous line shadow
         by xor-ing the graphical object
       */
       g.setXORMode(xorColor);
       g.drawLine(Orx,Ory,mousex,mousey);

       /* Update new mouse coordinates */
       mousex = e.getX();
       mousey = e.getY();

       /* Draw line shadow */
       g.drawLine(Orx,Ory,mousex,mousey);
    }
 }


 


 /*
   Method will emulate a eraser graphic.
   By drawing a filled rectangle of background color,
   with the current mouse coordinates being the center
   of the rectangle. This is done until the mouse has
   been release from dragmode
 */
 public void eraserOperation(MouseEvent e)
 {
    Graphics g  = drawPanel.getGraphics();

    /*
      In initial state setup default values
      for mouse coordinates
    */
    if (initialEraser)
    {
       setGraphicalDefaults(e);
       initialEraser = false;
       g.setColor(mainColor.white);
       g.fillRect(mousex-eraserLength, mousey-eraserLength,eraserLength*2,eraserLength*2);
       g.setColor(Color.black);
       g.drawRect(mousex-eraserLength,mousey-eraserLength,eraserLength*2,eraserLength*2);
       prevx = mousex;
       prevy = mousey;
    }

    /*
      Make sure that the mouse has actually
      moved from its previous position.
    */
    if (mouseHasMoved(e))
    {
       g.setColor(mainColor.white);
       g.drawRect(prevx-eraserLength, prevy-eraserLength,eraserLength*2,eraserLength*2);

       /* Get current mouse coordinates */
       mousex  = e.getX();
       mousey  = e.getY();

       /* Draw eraser block to panel */
       g.setColor(mainColor.white);
       g.fillRect(mousex-eraserLength, mousey-eraserLength,eraserLength*2,eraserLength*2);
       g.setColor(Color.black);
       g.drawRect(mousex-eraserLength,mousey-eraserLength,eraserLength*2,eraserLength*2);
       prevx = mousex;
       prevy = mousey;
    }
 }



 /*
   Method determines weather the mouse has moved
   from its last recorded position.
   If mouse has deviated from previous position,
   the value returned will be true, otherwise
   the value that is returned will be false.
 */
 public boolean mouseHasMoved(MouseEvent e)
 {
    return (mousex != e.getX() || mousey != e.getY());
 }


 /*
   Method is a key segment in the operations where
   there are more than 2 variables deviating to
   makeup a graphical operation.
   This method calculates the new values for the
   global varibles drawX and drawY according to
   the new positions of the mouse cursor.
   This method eleviates the possibility that
   a negative width or height can occur.
 */
 public void setActualBoundry()
 {
       /*
         If the any of the current mouse coordinates
         are smaller than the origin coordinates, meaning
         if drag occured in a negative manner, where either
         the x-shift occured from right and/or y-shift occured
         from bottom to top.
       */
       if (mousex < Orx || mousey < Ory)
       {
          /*
            if the current mouse x coordinate is smaller
            than the origin x coordinate,
            equate the drawX to be the difference between
            the current width and the origin x coordinate.
          */
          if (mousex < Orx)
          {
             OrWidth = Orx - mousex;
             drawX   = Orx - OrWidth;
          }
          else
          {
             drawX    = Orx;
             OrWidth  = mousex - Orx;

          }
          /*
            if the current mouse y coordinate is smaller
            than the origin y coordinate,
            equate the drawY to be the difference between
            the current height and the origin y coordinate.
          */
          if (mousey < Ory)
          {
             OrHeight = Ory - mousey;
             drawY    = Ory - OrHeight;
          }
          else
          {
             drawY    = Ory;
             OrHeight = mousey - Ory;
          }
       }
       /*
         Else if drag was done in a positive manner meaning
         x-shift occured from left to right and or y-shift occured
         from top to bottom
       */
       else
       {
          drawX    = Orx;
          drawY    = Ory;
          OrWidth  = mousex - Orx;
          OrHeight = mousey - Ory;
       }
 }


 /*
   Method sets all the drawing varibles to the default
   state which is the current position of the mouse cursor
   Also the height and width varibles are zeroed off.
 */
 public void setGraphicalDefaults(MouseEvent e)
 {
    mousex   = e.getX();
    mousey   = e.getY();
    prevx    = e.getX();
    prevy    = e.getY();
    Orx      = e.getX();
    Ory      = e.getY();
    drawX    = e.getX();
    drawY    = e.getY();
    OrWidth  = 0;
    OrHeight = 0;
 }


 /*
   Method will be activated when mouse is being dragged.
   depending on what operation is the opstatus, the switch
   statement will call the relevent operation
 */
 public void mouseDragged(MouseEvent e)
 {
    updateMouseCoordinates(e);

    switch (opStatus)
    {
       /* If opStatus is PEN_OP  then call penOperation method */
       case PEN_OP   : penOperation(e);
                       break;

       /* If opStatus is LINE_OP then call lineOperation method */
       case LINE_OP  : lineOperation(e);
                       break;

      

       /* If opStatus is ERASER_OP then call eraserOperation method */
       case ERASER_OP: eraserOperation(e);
                       break;
    }
 }


 /*
    Method will be activated when mouse has been release from pressed \
    mode. At this stage the method will call the finalization routines
    for the current operation.
 */
 public void mouseReleased(MouseEvent e)
 {
    /* Update current mouse coordinates to screen */
    updateMouseCoordinates(e);

    switch (opStatus)
    {
       /* If opStatus is PEN_OP  then call releasedPen method */
       case PEN_OP    : releasedPen();
                        break;

       /* If opStatus is LINE_OP then call releasedLine method */
       case LINE_OP   : releasedLine();
                        break;

       /* If opStatus is RECT_OP  then call releasedRect method */


       /* If opStatus is ERASER_OP then call releasedEraser method */
       case ERASER_OP : releasedEraser();
                        break;
    }
 }


 /*
    Method will be activated when mouse enters the applet area.
    This method will then update the current mouse x and coordinates
    on the screen.
 */
 public void mouseEntered(MouseEvent e)
 {
    updateMouseCoordinates(e);
 }


 /*
   Method will set the main system color according to the
   current color status.
 */
 public void setMainColor()
 {
    switch (colorStatus)
    {
       case 1 : mainColor = Color.black;
                break;

       case 2:  mainColor = Color.blue;
                break;

       case 3:  mainColor = Color.green;
                break;

       case 4:  mainColor = Color.red;
                break;

       case 5:  mainColor = Color.magenta;
                break;

       case 6:  mainColor = Color.orange;
                break;

       case 7:  mainColor = Color.pink;
                break;

       case 8:  mainColor = Color.gray;
                break;

       case 9:  mainColor = Color.yellow;
                break;

       case 10: mainColor = userDefinedColor;
                break;
    }
 }


 /*
   Method is invoked when mouse has been released
   and current operation is Pen
 */
 public void releasedPen()
 {
    initialPen = true;
 }


 /*
   Method is invoked when mouse has been released
   and current operation is Line
 */
 public void releasedLine()
 {
    if ((Math.abs(Orx - mousex) + Math.abs(Ory - mousey)) != 0)
    {
       System.out.println("Line has been released....");
       initialLine = true;
       Graphics g  = drawPanel.getGraphics();
       g.setColor(mainColor);
       g.drawLine(Orx,Ory,mousex,mousey);
    }
 }


 /*
   Method is invoked when mouse has been released
   and current operation is Eraser
 */
 public void releasedEraser()
 {
    initialEraser = true;
    Graphics g  = drawPanel.getGraphics();
    g.setColor(mainColor.white);
    g.drawRect(mousex-eraserLength,mousey-eraserLength,eraserLength*2,eraserLength*2);
 }


 /*
   Method is invoked when mouse has been released
   and current operation is Rectangle
 */
 
 /*
   Method displays the mouse coordinates x and y values
   and updates the mouse status bar with the new values.
 */
 public void updateMouseCoordinates(MouseEvent e)
 {
    String xCoor ="";
    String yCoor ="";

    if (e.getX() < 0) xCoor = "0";
    else
    {
       xCoor = String.valueOf(e.getX());
    }

    if (e.getY() < 0) xCoor = "0";
    else
    {
       yCoor = String.valueOf(e.getY());
    }
    //mouseStatusBar.setText("x:" + xCoor + "   y:" + yCoor);
 }


 /*
   Method updates user-defined values for udefRGB
 */
 

 /*
   Method updates mouse coordinates if mouse has been clicked
 */
 public void mouseClicked(MouseEvent e)
 {
    updateMouseCoordinates(e);
  
}


 /*
   Method updates mouse coordinates if mouse has exited applet
 */
 public void mouseExited(MouseEvent e)
 {
    updateMouseCoordinates(e);
 }


 /*
   Method updates mouse coordinates if mouse has moved
 */
 public void mouseMoved(MouseEvent e)
 {
    updateMouseCoordinates(e);
 }


 /*
   Method updates mouse coordinates if mouse has been pressed
 */
 public void mousePressed(MouseEvent e)
 {
    updateMouseCoordinates(e);
 }

public void createImage(JPanel panel)
{
        int w = panel.getWidth();
 int h = panel.getHeight();
 //Image draws=panel.createImage(w, h);
 
// BufferedImage bi ;//= new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);     
 //bi=(BufferedImage)drawPanel.createImage(w,h);
 //Graphics2D g2 = bi.createGraphics();     
 //Graphics g1=drawPanel.getGraphics();
  //Graphics g=Chat.drawPad.getGraphics();
  //Graphics2D g2d =(Graphics2D)g;
  //g2d.drawLine(0,0 , 100, 100);
  //g2d.drawImage(new ImageIcon("C:\\JDeveloper\\mywork\\MENC\\Chat\\chat-25.png"));
  //g2d.drawImage(drawPanel.createImage(w,h),0,0,w,h,0,0,w,h,null); 
  // Chat.drawPad.setIcon(new ImageIcon("C:\\JDeveloper\\mywork\\MENC\\Chat\\chat-25.png"));
    Chat.drawPad.add(drawPanel);
    Chat.con.validate();
     } 

 
 public static void beginScribble()
 {
     final Applet applet = new Scribble();
    System.runFinalizersOnExit(true);
    Frame frame = new Frame (
                 "Scribble");
    frame.addWindowListener (
                  new WindowAdapter()
    {
      public void windowClosing (
                   WindowEvent event)
      {
        applet.stop();
        applet.destroy();
        System.exit(0);
      }
    });
    frame.add ("Center", applet);
    frame.setSize(25,25);
    frame.show();
    applet.init();
    applet.start();
    frame.pack();
    }
 

} // End of Scribble

