import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


/**
 *  Class to read data to an Input Channel
 *  and write data to an Output Channel
 */

public class CapturePlayback extends JPanel implements Runnable, ActionListener {


    Playback playback = new Playback();

    AudioInputStream audioInputStream;
    AudioSettingControl settingControl = new AudioSettingControl();

    // Capture and Playback Buttons
    JButton playB, pauseB, captureB, loadB;
    // Audio Output format Buttons
    JButton auB, aiffB, waveB;
    String fileName = "testfile";
    String errorString;
    double duration, seconds;
    File file;

    public CapturePlayback() {
        setLayout(new BorderLayout());
        EmptyBorder padding = new EmptyBorder(5, 5, 5, 5);

        // Core Container Panel
        JPanel corePanel = new JPanel();
        corePanel.setLayout(new BoxLayout(corePanel, BoxLayout.X_AXIS));
        corePanel.add(settingControl);

        // Secondary Container Panel
        JPanel secondaryPanel = new JPanel();
//        secondaryPanel.setBorder(sbb);
        secondaryPanel.setLayout(new BoxLayout(secondaryPanel, BoxLayout.Y_AXIS));

        // Buttons Panel for Capture and Playback
        JPanel buttonPanel = new JPanel();
        playB = addButton("Play", buttonPanel, false);
        captureB = addButton("Record", buttonPanel, true);
        pauseB = addButton("Pause", buttonPanel, false);
        loadB = addButton("Load...", buttonPanel, true);
        secondaryPanel.add(buttonPanel);

        // Sample Graph Panel
        JPanel samplingPanel = new JPanel(new BorderLayout());
        padding = new EmptyBorder(10, 20, 20, 20);
        samplingPanel.setBorder(new CompoundBorder(padding, padding));
//        samplingPanel.add(samplingGraph = new SamplingGraph());
        secondaryPanel.add(samplingPanel);

        // Settings Panel
        corePanel.add(secondaryPanel);
        add(corePanel);
        corePanel.setVisible(true);
    }

    /**
     * Method for adding the capture playback related buttons to a JPanel
     *
     * @param name  , The name of the button.
     * @param p     , The chosen JPanel to add the button to.
     * @param state , Enabled or Disabled default state
     * @return A Button added to the input JPanel p, with the given name in an accessible or inaccessible default state.
     */
    private JButton addButton(String name, JPanel p, boolean state) {
        JButton b = new JButton(name);
        b.addActionListener(this);
        b.setEnabled(state);
        p.add(b);
        return b;
    }

//    public void open() { }
//
//
//    public void close() {
//        if (playback.thread != null) {
//            playB.doClick(0);
//        }
//        if (capture.thread != null) {
//            captB.doClick(0);
//        }
//    }


    public void run() {
    }


    public void actionPerformed(ActionEvent e) {
        System.out.println("Button test");
    }

    public void createAudioInputStream(File file, boolean updateComponents) {
        if (file != null && file.isFile()) {
            try {
                this.file = file;
                errorString = null;
                audioInputStream = AudioSystem.getAudioInputStream(file);
                playB.setEnabled(true);
                fileName = file.getName();
                long milliseconds = (long)((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
                duration = milliseconds / 1000.0;
                auB.setEnabled(true);
                aiffB.setEnabled(true);
                waveB.setEnabled(true);
                if (updateComponents) {
                    settingControl.setFormat(audioInputStream.getFormat());
//                    samplingGraph.createWaveForm(null);
                }
            } catch (Exception ex) {
                reportStatus(ex.toString());
            }
        } else {
            reportStatus("Audio file required.");
        }
    }

    public void saveToFile(String name, AudioFileFormat.Type fileType) {

        if (audioInputStream == null) {
            reportStatus("No loaded audio to save");
            return;
        } else if (file != null) {
            createAudioInputStream(file, false);
        }

        // Rest to beginning of captured stream.
        try {
            audioInputStream.reset();
        } catch (Exception e) {
            reportStatus("Unable to reset stream " + e);
            return;
        }

        File file = new File(fileName = name);
        try {
            if (AudioSystem.write(audioInputStream, fileType, file) == -1) {
                throw new IOException("Problems writing to file");
            }
        } catch (Exception ex) { reportStatus(ex.toString()); }
//        samplingGraph.repaint();
    }

    private void reportStatus(String msg) {
        if ((errorString = msg) != null) {
            System.out.println(errorString);
//                samplingGraph.repaint();
        }
    }


    public void saveSettings() {
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



    private class Playback implements Runnable {

        SourceDataLine line;
        Thread thread;


        public void start() {
            errorString = null;
            thread = new Thread(this);
            thread.setName("Playback");
            thread.start();
        }

        public void stop(){
            thread = null;
        }

        private void shutDown(String message) {
            if ((errorString = message) != null) {
                System.err.println(errorString);
            }
            if (thread != null) {
                thread = null;
            }
        }

        public void run(){

            // reload the file if loaded by file
            if (file != null) {
                createAudioInputStream(file, false);
            }

        }

    }

}
