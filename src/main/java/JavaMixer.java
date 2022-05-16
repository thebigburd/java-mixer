import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;


public class JavaMixer
{
    private JFrame mainWindow = new JFrame();

    public JavaMixer(){
        init();
    }

    private void init(){

        // Set mainframe
        mainWindow.setTitle("Java Audio Editor");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(1280, 720);
        mainWindow.setResizable(true);
        mainWindow.setLocationRelativeTo(null);

        // Create top menu bar.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JButton menu2 = new JButton("Settings");
        menuBar.add(menu1);
        menuBar.add(menu2);
        mainWindow.setJMenuBar(menuBar);

        // Add Main Capture and Playback Panel
        JPanel capturePlayback = new CapturePlayback();
        mainWindow.add(capturePlayback);

        mainWindow.setVisible(true);
    }

}
