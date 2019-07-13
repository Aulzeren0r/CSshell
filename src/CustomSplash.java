import javax.swing.*;
import java.awt.*;

public class CustomSplash extends JWindow {
    JLabel text;
    public void ShowSplash(){
        JPanel content = (JPanel) getContentPane();
        content.setBackground(Color.decode("#9e1b32"));
        int w = 400;
        int h = 100;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - w) / 2;
        int y = (screen.height - h) / 2;
        setBounds(x, y, w, h);
        JLabel header = new JLabel("Now Loading Bama Champ Select Display");
        text = new JLabel("Fetching Current Version");
        text.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
        text.setForeground(Color.decode("#000000"));
        JLabel bar = new JLabel(new ImageIcon(".\\img\\loadbar.gif"));
        header.setFont(new Font("Sans-Serif", Font.BOLD, 16));
        header.setForeground(Color.decode("#000000"));
        content.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(5, 5, 5,5);
        c.ipady = 5;
        c.ipadx = 5;
        content.add(header, c);
        c.gridy ++;
        content.add(text, c);
        c.gridy ++;
        content.add(bar, c);
        setVisible(true);
    }

    public void EndSplash(){
        setVisible(false);
        dispose();
    }

    public void ChangeText(String t){
        text.setText(t);
        repaint();
    }
}
