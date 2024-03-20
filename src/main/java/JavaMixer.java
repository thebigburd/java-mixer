import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;

/**
 *  JavaMixer
 *
 * @version 1.00
 * @author Richard Lam
 */
public class JavaMixer {
    private JFrame mainWindow = new JFrame();

    public JavaMixer() {
        init();
    }


    private void init() {

        // Set mainframe
        mainWindow.setTitle("Java Audio Editor");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(1280, 720);
        mainWindow.setResizable(true);
        mainWindow.setLocationRelativeTo(null);

        // Create top menu bar.
        JMenuBar menuBar = new JMenuBar();
        //JMenu menu1 = new JMenu("Java Mixer");
        //JMenuItem exit = new JMenuItem("Exit");
        //menu1.add(exit);
        //menuBar.add(menu1);
        mainWindow.setJMenuBar(menuBar);

        // Add Main Capture and Playback Panel
        JPanel capturePlayback = new CapturePlayback();
        mainWindow.add(capturePlayback);

        mainWindow.setVisible(true);
    }


    /**
     * A dialog that appears if the user has not enabled the required file permissions.
     */
    public static void showSecurityDialog() {
        final String msg =
                "Permissions may be necessary in order to load/save files and record audio :  \n\n" +
                        "grant { \n" +
                        "  Permission java.io.FilePermission \"<<ALL FILES>>\", \"read, write\";\n" +
                        "  permission javax.sound.sampled.AudioPermission \"record\"; \n" +
                        "  permission java.util.PropertyPermission \"user.dir\", \"read\";\n" +
                        "}; \n\n" +
                        "The permissions need to be added to the .java.policy file.";
        new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, msg, "File permission Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
    }

    /**
     * Dialog that appears if the User selects Audio Settings
     * That are not supported by Java Sound
     */
    public static void showLineNotSupportedDialog() {
        final String msg =
                "The selected Audio Settings are not supported. \n" +
                        "Note that uLaw and aLaw do not support 16 bit Sample sizes. \n" +
                        "If in doubt, use Linear encoding \n" +
                        "Please select different settings";
        new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, msg, "File permission Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
    }

    public static void settingsAppliedDialog(){
        final String msg =
                "Settings have been applied!";
        new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, msg, "File permission Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
    }

    public static void fileSavedDialog(){
        final String msg =
                "File has been saved successfully.";
        new Thread(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null, msg, "File permission Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
    }
}
