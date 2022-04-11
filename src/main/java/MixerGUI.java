import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class MixerGUI
{
    private JFrame mainWindow;

    MigLayout layout = new MigLayout();

    public MixerGUI(){
        init();
    }

    private void init(){
        mainWindow = new JFrame();
        mainWindow.setTitle("Java Audio Editor");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(1280, 720);
        mainWindow.setResizable(true);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

}
