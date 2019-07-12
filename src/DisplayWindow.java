import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayWindow {
    /* WIP. The main screen of the program. This window only ever contains one box, filled with an image created in
     * DrawData. However, this class does a large portion of the heavy lifting regarding information calculation --
     * DrawData will only handle the graphics themselves.
     */
    DrawData art_handler;
    DataHandler info;
    JFrame main_frame;
    JPanel main_panel;
    String timer = "00";
    JLabel[] label_array;
    BufferedImage[] image_array;
    Graphics[] graphics_array;
    TimerThread t_thread;

    private static final int BLUE = 0;
    private static final int RED = 1;
    public DisplayWindow(TopLevelRewrite orig_window, DataHandler data){
        //Init. Some classes are forwarded from TLR for ease of use.
        info = data;
        main_frame = new JFrame();
        main_frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        main_frame.setUndecorated(true);
        main_frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        WindowListener InternalListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                main_frame.dispose();
            }
        };
        main_frame.addWindowListener(InternalListener);
        main_panel = (JPanel) main_frame.getContentPane();
        art_handler = new DrawData(orig_window.data);
        LoadTestImage();
    }

    public void SetVis(){
        //Public-facing visibility toggle.
        if(!main_frame.isVisible()) {
            main_frame.setVisible(true);
        }
        else{
            main_frame.setVisible(false);
            t_thread.Stop();
        }
    }

    public void SetThread(TimerThread t){
        //Sets the thread which controls the timer for champion select, load-in, etc.
        t_thread = t;
    }

    private void LoadTestImage(){
        //Test function. Currently unused.
    }

    public void ForceFront(){
        //Forces the screen to the front of the current monitor.
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                main_frame.toFront();
                main_frame.repaint();
            }
        });
    }

    public void SetTimer(int current){
        //Called by TimerThread. Sets the internal timer string to the value remaining in the thread.
        String temp;
        if(current < 10){
            temp = "0" + current;
        }
        else{
            temp = Integer.toString(current);
        }
        timer = temp;
        label_array[0].setText(timer);
        main_frame.repaint();
    }
}
