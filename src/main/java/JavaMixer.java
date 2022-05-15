import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Vector;


public class JavaMixer
{
    private JFrame mainWindow;
    private AudioSettings audioSettings;

    public JavaMixer(){
        init();
    }

    private void init(){

        // Create main working window.
        mainWindow = new JFrame();
        mainWindow.setTitle("Java Audio Editor");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setSize(1280, 720);
        mainWindow.setResizable(true);
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);

        // Create top menu bar.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("File");
        JButton menu2 = new JButton("Settings");
        menu2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog settingsMenu = new AudioSettingUI(mainWindow);
            }
        });

        menuBar.add(menu1);
        menuBar.add(menu2);
        mainWindow.setJMenuBar(menuBar);

    }


    public void saveSettings(){
        try {
            FileOutputStream file = new FileOutputStream("settings");
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.close();
            file.close();

            System.out.println("File has been saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadSettings(){
        try{
            FileInputStream file = new FileInputStream("settings");
            ObjectInputStream in = new ObjectInputStream(file);
            JavaMixer mixer = (JavaMixer) in.readObject();
            AudioSettings audioSettings = mixer.audioSettings;

            in.close();
            file.close();

            System.out.println("Settings loaded");
            System.out.println(audioSettings);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void setAudioSettings(Vector settings){
        audioSettings.savedSettings = (Vector) settings.clone();
    }

}
