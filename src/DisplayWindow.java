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
    DrawData art_handler;
    DataHandler info;
    JFrame main_frame;
    JPanel main_panel;
    String timer = "00";
    BufferedImage[] image_array;
    Graphics[] graphics_array;

    private static final int BLUE = 0;
    private static final int RED = 1;
    public DisplayWindow(TopLevelRewrite orig_window, DataHandler data){
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
        if(!main_frame.isVisible()) {
            main_frame.setVisible(true);
        }
        else{
            main_frame.setVisible(false);
        }
    }

    private void LoadTestImage(){

    }

    public void ForceFront(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                main_frame.toFront();
                main_frame.repaint();
            }
        });
    }

    public void SetTimer(int current){
        String temp;
        if(current < 10){
            temp = "0" + current;
        }
        else{
            temp = Integer.toString(current);
        }
        timer = temp;
        main_frame.repaint();
    }
}
