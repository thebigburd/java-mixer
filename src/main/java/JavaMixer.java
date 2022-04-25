import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class JavaMixer
{
    private JFrame mainWindow;


    public JavaMixer(){
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

        AudioSettingUI settingMenu = new AudioSettingUI();

        mainWindow.setContentPane(settingMenu);
    }

}
