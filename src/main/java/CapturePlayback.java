import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Vector;


/**
 * Class to capture audio data through an input channel,
 * and write data to an Output Channel that can be played back.
 *
 * @author Richard Lam
 * @version 1.00
 */

public class CapturePlayback extends JPanel implements ActionListener {


    final int bufferSize = 16384; // 16KB buffer
    AudioSettingControl settingControl = new AudioSettingControl();
    Capture capture = new Capture();
    Playback playback = new Playback();
    fourierTransformer samplingGraph = new fourierTransformer();

    AudioInputStream audioInputStream;


    // Capture and Playback Buttons
    JButton playB, pauseB, captureB, importB;
    // Export Name Field
    JTextField exportField;
    // Audio Export format Buttons
    JButton auB, aiffB, waveB;

    String fileName = "testfile";
    String errorString;
    Vector lines = new Vector();
    double duration, seconds;
    File file;

    /**
     * Constructor sets up user interface.
     */
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
        importB = addButton("Import", buttonPanel, true);

        secondaryPanel.add(buttonPanel);

        // Sample Graph Panel
        JPanel samplingPanel = new JPanel(new BorderLayout());
        padding = new EmptyBorder(10, 20, 20, 20);
        samplingPanel.setBorder(new CompoundBorder(padding, padding));
        samplingPanel.add(samplingGraph = new fourierTransformer());
        secondaryPanel.add(samplingPanel);

        // Export Name Field
        JPanel saveTFpanel = new JPanel();
        saveTFpanel.add(new JLabel("File name:  "));
        saveTFpanel.add(exportField = new JTextField(fileName));
        exportField.setPreferredSize(new Dimension(200, 25));
        secondaryPanel.add(saveTFpanel);

        // Export Panel
        JPanel saveBpanel = new JPanel();
        auB = addButton("Save AU", saveBpanel, false);
        aiffB = addButton("Save AIFF", saveBpanel, false);
        waveB = addButton("Save WAVE", saveBpanel, false);
        secondaryPanel.add(saveBpanel);

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

    /**
     * Activates on the click of the capture or playback related buttons.
     *
     * @param e , The event triggered by the click of a control button.
     */
    public void actionPerformed(ActionEvent e) {
        // Get Action event source
        Object obj = e.getSource();

        // Capture and Playback actions
        // User clicks Play Button
        if (obj.equals(playB)) {
            if (playB.getText().startsWith("Play")) {
                playback.start();
                samplingGraph.start();
                captureB.setEnabled(false);
                pauseB.setEnabled(true);
                playB.setText("Stop");
            } else {
                playback.stop();
                samplingGraph.stop();
                captureB.setEnabled(true);
                pauseB.setEnabled(false);
                playB.setText("Play");
            }
        }
        // User clicks Record Button
        else if (obj.equals(captureB)) {
            if (captureB.getText().startsWith("Record")) {
                file = null;
                capture.start();
                fileName = "untitled";
                samplingGraph.start();
                importB.setEnabled(false);
                playB.setEnabled(false);
                pauseB.setEnabled(true);
                auB.setEnabled(false);
                aiffB.setEnabled(false);
                waveB.setEnabled(false);
                captureB.setText("Stop Recording");
            } else {
                lines.removeAllElements();
                capture.stop();
                samplingGraph.stop();
                importB.setEnabled(true);
                playB.setEnabled(true);
                pauseB.setEnabled(false);
                auB.setEnabled(true);
                aiffB.setEnabled(true);
                waveB.setEnabled(true);
                captureB.setText("Record");
            }
        }
        // Pause Button
        else if (obj.equals(pauseB)) {
            if (pauseB.getText().startsWith("Pause")) {
                if (capture.thread != null) {
                    capture.line.stop();
                } else {
                    if (playback.thread != null) {
                        playback.line.stop();
                    }
                }
                pauseB.setText("Resume");
            } else {
                if (capture.thread != null) {
                    capture.line.start();
                } else {
                    if (playback.thread != null) {
                        playback.line.start();
                    }
                }
                pauseB.setText("Pause");
            }
        }
        // Import Button
        else if (obj.equals(importB)) {
            try {
                File file = new File(System.getProperty("user.dir"));
                JFileChooser fc = new JFileChooser(file);
                fc.setFileFilter(new FileFilter() {
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        }
                        String name = f.getName();
                        // JavaSound supported Audio formats.
                        if (name.endsWith(".au") || name.endsWith(".wav") || name.endsWith(".aiff") || name.endsWith(".aif")) {
                            return true;
                        }
                        return false;
                    }

                    public String getDescription() {
                        return ".au, .wav, .aif";
                    }
                });

                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    createAudioInputStream(fc.getSelectedFile(), true);
                }
            } catch (SecurityException ex) {
                JavaMixer.showSecurityDialog();
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // Export Save Buttons
        else if (obj.equals(auB)) {
            exportFile(exportField.getText().trim(), AudioFileFormat.Type.AU);
        } else if (obj.equals(aiffB)) {
            exportFile(exportField.getText().trim(), AudioFileFormat.Type.AIFF);
        } else if (obj.equals(waveB)) {
            exportFile(exportField.getText().trim(), AudioFileFormat.Type.WAVE);
        }
    }

    public void createAudioInputStream(File file, boolean updateComponents) {
        if (file != null && file.isFile()) {
            try {
                this.file = file;
                errorString = null;
                audioInputStream = AudioSystem.getAudioInputStream(file);
                playB.setEnabled(true);
                fileName = file.getName();
                long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
                duration = milliseconds / 1000.0;
                auB.setEnabled(true);
                aiffB.setEnabled(true);
                waveB.setEnabled(true);
                if (updateComponents) { // Update Graph
                    samplingGraph.createWaveForm(null);
                }
            } catch (Exception ex) {
                reportStatus(ex.toString());
            }
        } else {
            reportStatus("Audio file required.");
        }
    }

    public void exportFile(String name, AudioFileFormat.Type fileType) {

        if (audioInputStream == null) {
            reportStatus("No loaded audio to save");
            return;
        } else if (file != null) {
            createAudioInputStream(file, false);
        }

        // Reset to beginning of captured stream.
        try {
            audioInputStream.reset();
        } catch (Exception e) {
            reportStatus("Unable to reset stream " + e);
            return;
        }

        // Assign file extensions when saving
        String type = String.valueOf(fileType).toLowerCase();
        // WAVE to .WAV
        if (type.equals("wave")) {
            type = type.replaceFirst(".$", "");
        }
        File file = new File(fileName = (name + "." + type));
        try {
            if (AudioSystem.isFileTypeSupported(fileType,
                    audioInputStream)) {
                AudioSystem.write(audioInputStream, fileType, file);
                JavaMixer.fileSavedDialog();
            } else {
                throw new IOException("Error writing to file");
            }
        } catch (Exception ex) {
            reportStatus(ex.toString());
        }

        samplingGraph.repaint();
    }

    private void reportStatus(String msg) {
        if ((errorString = msg) != null) {
            System.out.println(errorString);
            samplingGraph.repaint();
        }
    }
    // End of CapturePlayBack base class


    class Capture implements Runnable {

        TargetDataLine line;
        Thread thread;

        public void start() {
            errorString = null;
            thread = new Thread(this);
            thread.setName("Capture");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errorString = message) != null && thread != null) {
                thread = null;
                samplingGraph.stop();
                importB.setEnabled(true);
                playB.setEnabled(true);
                pauseB.setEnabled(false);
                auB.setEnabled(true);
                aiffB.setEnabled(true);
                waveB.setEnabled(true);
                captureB.setText("Record");
                System.err.println(errorString);
                samplingGraph.repaint();
            }
        }

        public void run() {

            duration = 0;
            audioInputStream = null;

            // Get required attributes for our line,
            AudioFormat format = settingControl.getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class,
                    format);

            // Verify a compatible line is supported.
            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                JavaMixer.showLineNotSupportedDialog();
                return;
            }

            // Get and Open Target data line for Capture.
            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format, line.getBufferSize());
            } catch (LineUnavailableException ex) {
                shutDown("Unable to open the line: " + ex);
                return;
            } catch (SecurityException ex) {
                shutDown(ex.toString());
                JavaMixer.showSecurityDialog();
                return;
            } catch (Exception ex) {
                shutDown(ex.toString());
                return;
            }

            // Read captured audio data
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead;

            // Start TargetDataLine
            line.start();

            // Write captured audio data.
            while (thread != null) {
                if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                    break;
                }
                out.write(data, 0, numBytesRead);
            }

            // End of the stream.  Stop and close line.
            line.stop();
            line.close();
            line = null;

            // Stop and close the output stream
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Load bytes into the audio input stream for playback
            byte audioBytes[] = out.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);

            long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
            duration = milliseconds / 1000.0;

            try {
                audioInputStream.reset();
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            samplingGraph.createWaveForm(audioBytes);
        }
    }

    // End of Capture Class

    /**
     * Class for writing data to the output Audio Channel.
     */
    private class Playback implements Runnable {

        SourceDataLine line;
        Thread thread;


        public void start() {
            errorString = null;
            thread = new Thread(this);
            thread.setName("Playback");
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errorString = message) != null) {
                System.err.println(errorString);
            }
            if (thread != null) {
                thread = null;
                samplingGraph.stop();
                captureB.setEnabled(true);
                pauseB.setEnabled(false);
                playB.setText("Play");
            }
        }

        public void run() {

            // Restart stream if an audio file is loaded.
            if (file != null) {
                createAudioInputStream(file, false);
            }

            // Verify a file has been loaded into stream.
            if (audioInputStream == null) {
                shutDown("No loaded audio to play back");
                return;
            }

            // Reset to the beginning of the stream
            try {
                audioInputStream.reset();
            } catch (Exception e) {
                shutDown("Unable to reset the stream\n" + e);
                return;
            }

            // Get Audio Format for Input stream.
            AudioFormat format = settingControl.getAudioFormat();
            AudioInputStream playbackInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);

            if (playbackInputStream == null) {
                shutDown("Unable to convert stream of format " + audioInputStream + " to format " + format);
                return;
            }

            // Define the required attributes for source line.
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            // Verify a compatible line is supported.
            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // Get and open the source data line for playback.

            try {
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format, bufferSize);
            } catch (LineUnavailableException ex) {
                shutDown("Unable to open the line: " + ex);
                return;
            }

            // Get audio data attributes
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead = 0;

            // Start source data line on thread
            line.start();

            // Playback the captured audio data.
            while (thread != null) {
                try {
                    if ((numBytesRead = playbackInputStream.read(data)) == -1) {
                        break;
                    }
                    int numBytesRemaining = numBytesRead;
                    while (numBytesRemaining > 0) {
                        numBytesRemaining -= line.write(data, 0, numBytesRemaining);
                    }
                } catch (Exception e) {
                    shutDown("Error during playback: " + e);
                    break;
                }
            }
            // we reached the end of the stream.  let the data play out, then
            // stop and close the line.
            if (thread != null) {
                line.drain();
            }
            line.stop();
            line.close();
            line = null;
            shutDown(null);
        }

    }

    // End of Playback Class

    /**
     * Render a waveform
     *
     * @author Brian Lichtenwalter
     * @version 1.11
     */
    public class fourierTransformer extends JPanel implements Runnable {


        private Thread thread;
        private Font font10 = new Font("serif", Font.PLAIN, 10);
        private Font font12 = new Font("serif", Font.PLAIN, 12);
        Color jfcBlue = new Color(204, 204, 255);
        Color pink = new Color(255, 175, 175);


        public fourierTransformer() {
            setBackground(new Color(20, 20, 20));
        }


        public void createWaveForm(byte[] audioBytes) {

            lines.removeAllElements();  // clear the old vector

            AudioFormat format = audioInputStream.getFormat();
            if (audioBytes == null) {
                try {
                    audioBytes = new byte[
                            (int) (audioInputStream.getFrameLength()
                                    * format.getFrameSize())];
                    audioInputStream.read(audioBytes);
                } catch (Exception ex) {
                    reportStatus(ex.toString());
                    return;
                }
            }

            Dimension d = getSize();
            int w = d.width;
            int h = d.height - 15;
            int[] audioData = null;
            if (format.getSampleSizeInBits() == 16) {
                int nlengthInSamples = audioBytes.length / 2;
                audioData = new int[nlengthInSamples];
                if (format.isBigEndian()) {
                    for (int i = 0; i < nlengthInSamples; i++) {
                        /* First byte is MSB (high order) */
                        int MSB = (int) audioBytes[2 * i];
                        /* Second byte is LSB (low order) */
                        int LSB = (int) audioBytes[2 * i + 1];
                        audioData[i] = MSB << 8 | (255 & LSB);
                    }
                } else {
                    for (int i = 0; i < nlengthInSamples; i++) {
                        /* First byte is LSB (low order) */
                        int LSB = (int) audioBytes[2 * i];
                        /* Second byte is MSB (high order) */
                        int MSB = (int) audioBytes[2 * i + 1];
                        audioData[i] = MSB << 8 | (255 & LSB);
                    }
                }
            } else if (format.getSampleSizeInBits() == 8) {
                int nlengthInSamples = audioBytes.length;
                audioData = new int[nlengthInSamples];
                if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                    for (int i = 0; i < audioBytes.length; i++) {
                        audioData[i] = audioBytes[i];
                    }
                } else {
                    for (int i = 0; i < audioBytes.length; i++) {
                        audioData[i] = audioBytes[i] - 128;
                    }
                }
            }

            int frames_per_pixel = audioBytes.length / format.getFrameSize() / w;
            byte my_byte = 0;
            double y_last = 0;
            int numChannels = format.getChannels();
            for (double x = 0; x < w && audioData != null; x++) {
                int idx = (int) (frames_per_pixel * numChannels * x);
                if (format.getSampleSizeInBits() == 8) {
                    my_byte = (byte) audioData[idx];
                } else {
                    my_byte = (byte) (128 * audioData[idx] / 32768);
                }
                double y_new = (double) (h * (128 - my_byte) / 256);
                lines.add(new Line2D.Double(x, y_last, x, y_new));
                y_last = y_new;
            }

            repaint();
        }


        public void paint(Graphics g) {

            Dimension d = getSize();
            int w = d.width;
            int h = d.height;
            int INFOPAD = 15;

            Graphics2D g2 = (Graphics2D) g;
            g2.setBackground(getBackground());
            g2.clearRect(0, 0, w, h);
            g2.setColor(Color.white);
            g2.fillRect(0, h - INFOPAD, w, INFOPAD);

            if (errorString != null) {
                g2.setColor(jfcBlue);
                g2.setFont(new Font("serif", Font.BOLD, 18));
                g2.drawString("ERROR", 5, 20);
                AttributedString as = new AttributedString(errorString);
                as.addAttribute(TextAttribute.FONT, font12, 0, errorString.length());
                AttributedCharacterIterator aci = as.getIterator();
                FontRenderContext frc = g2.getFontRenderContext();
                LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
                float x = 5, y = 25;
                lbm.setPosition(0);
                while (lbm.getPosition() < errorString.length()) {
                    TextLayout tl = lbm.nextLayout(w - x - 5);
                    if (!tl.isLeftToRight()) {
                        x = w - tl.getAdvance();
                    }
                    tl.draw(g2, x, y += tl.getAscent());
                    y += tl.getDescent() + tl.getLeading();
                }
            } else if (capture.thread != null) {
                g2.setColor(Color.black);
                g2.setFont(font12);
                g2.drawString("Length: " + String.valueOf(seconds), 3, h - 4);
            } else {
                g2.setColor(Color.black);
                g2.setFont(font12);
                g2.drawString("File: " + fileName + "  Length: " + String.valueOf(duration) + "  Position: " + String.valueOf(seconds), 3, h - 4);

                if (audioInputStream != null) {
                    // .. render sampling graph ..
                    g2.setColor(jfcBlue);
                    for (int i = 1; i < lines.size(); i++) {
                        g2.draw((Line2D) lines.get(i));
                    }

                    // .. draw current position ..
                    if (seconds != 0) {
                        double loc = seconds / duration * w;
                        g2.setColor(pink);
                        g2.setStroke(new BasicStroke(3));
                        g2.draw(new Line2D.Double(loc, 0, loc, h - INFOPAD - 2));
                    }
                }
            }
        }

        public void start() {
            thread = new Thread(this);
            thread.setName("SamplingGraph");
            thread.start();
            seconds = 0;
        }

        public void stop() {
            if (thread != null) {
                thread.interrupt();
            }
            thread = null;
        }

        public void run() {
            seconds = 0;
            while (thread != null) {
                if ((playback.line != null) && (playback.line.isOpen())) {

                    long milliseconds = (long) (playback.line.getMicrosecondPosition() / 1000);
                    seconds = milliseconds / 1000.0;
                } else if ((capture.line != null) && (capture.line.isActive())) {

                    long milliseconds = (long) (capture.line.getMicrosecondPosition() / 1000);
                    seconds = milliseconds / 1000.0;
                }

                try {
                    thread.sleep(100);
                } catch (Exception e) {
                    break;
                }

                repaint();

                while ((capture.line != null && !capture.line.isActive()) ||
                        (playback.line != null && !playback.line.isOpen())) {
                    try {
                        thread.sleep(10);
                    } catch (Exception e) {
                        break;
                    }
                }
            }
            seconds = 0;
            repaint();
        }

    }


}
