/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.awt.event.*;

public class imax extends Frame{
    Toolkit tk = Toolkit.getDefaultToolkit();
    Dimension tam = tk.getScreenSize();
    Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/img/imax.jpg")); 
  
    int w=(int)tam.getWidth();
    int h=(int)tam.getHeight();
    public imax()
    {
        System.out.println(img);
        //super(filename);
        //image=getToolkit().getImage(filename);
        setTitle("IMAX");
        setSize(w,h/5);
	setUndecorated(true);
	setVisible(true);
	setAlwaysOnTop(true);
        
    }
    public void paint(Graphics g){
        g.setColor(Color.black);
        g.fillRect(0,0,w,h/5);
        g.drawImage(img,w/10,0,this);
    }
    public static void main(String[] args) {
        Frame f=new imax();
        f.addWindowListener(new WindowAdapter(){
        public void windowClosing(WindowEvent we){
            System.exit(0);}
        });

    }
}
