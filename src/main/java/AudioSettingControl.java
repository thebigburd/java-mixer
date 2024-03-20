import net.miginfocom.swing.MigLayout;

import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 *  Class that controls the user's audio setting selections.
 *
 * @version 1.00
 *
 * @author Richard Lam
 */
class AudioSettingControl extends JPanel implements ActionListener {


    final String[] audioEncodingGroup = {"linear", "ulaw", "alaw"};
    final String[] sampleRateGroup = {"8000", "11025", "16000", "22050",
            "44100", "48000"};
    final String[] sampleSizeGroup = {"8", "16"};
    final String[] endianGroup = {"little endian", "big endian"};
    final String[] signGroup = {"signed", "unsigned"};
    final String[] channelGroup = {"mono", "stereo"};

    // Global ComboBoxes for Action Listeners
    JComboBox audioEncodings = new JComboBox(audioEncodingGroup);
    JComboBox sampleRates = new JComboBox(sampleRateGroup);
    JComboBox sampleSizes = new JComboBox(sampleSizeGroup);
    JComboBox endians = new JComboBox((endianGroup));
    JComboBox signs = new JComboBox(signGroup);
    JComboBox channels = new JComboBox(channelGroup);

    // Saved Settings
    AudioSettings audioSettings;

    // Unsaved Setting Selections
    Vector audioSelections = new Vector<String>(6);

    public AudioSettingControl() {
        // Load Saved Settings
        loadSettings();

        // Initialise UI
        init();
    }

    public void init() {
        MigLayout layout = new MigLayout("wrap 2");
        setLayout(layout);


        // Add Listeners to Combo Boxes
        audioEncodings.addActionListener(this);
        sampleRates.addActionListener(this);
        sampleSizes.addActionListener(this);
        endians.addActionListener(this);
        signs.addActionListener(this);
        channels.addActionListener(this);

        // Add Dropdown Boxes to Panel
        add(new JLabel("Encoding:"));
        add(audioEncodings);
        add(new JLabel("Sample Rate:"));
        add(sampleRates);
        add(new JLabel("Sample Size:"));
        add(sampleSizes);
        add(new JLabel("Endian:"));
        add(endians);
        add(new JLabel("Signed?:"));
        add(signs);
        add(new JLabel("Channels:"));
        add(channels);

        // Add Buttons
        JButton okB = new JButton("Apply");
        okB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySelections();
                saveSettings();
                JavaMixer.settingsAppliedDialog();
            }
        });

        JButton resetToDefaultB = new JButton("Reset to Default");
        resetToDefaultB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetToDefault();
            }
        });
        add(okB, "align left");
        add(resetToDefaultB, "tag right");

    }

    private void applySelections() {
        try {
            audioSettings.savedSettings = (Vector) audioSelections.clone();
            System.out.println("Saved: "+ audioSettings.savedSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *  Resets combobox selections
     */
    private void resetToDefault() {
        // Set audioSelections values
        resetAudioSelections();
        // Set Combobox's visible selection
        resetComboBoxes();
    }

    /**
     * Method to get an AudioFormat object to use when operating a Capture input stream.
     * Note: Bit Rate = Sample Rate * Sample Size * No.Channels
     *      Frame Size = Sample Size / No.Channels
     * @return AudioFormat object containing all the user's chosen audio settings.
     */
    public AudioFormat getAudioFormat() {

        AudioFormat.Encoding encoding = AudioFormat.Encoding.ULAW;  // ULAW is 8 Bit and Unsigned
        String encStr = (String) audioSettings.getSavedSettings().get(0);
        float rate = Float.valueOf((String) audioSettings.getSavedSettings().get(1));
        int sampleSize = Integer.valueOf((String) audioSettings.getSavedSettings().get(2));
        boolean bigEndian = ((String) audioSettings.getSavedSettings().get(3)).startsWith("big");
        String signedStr = (String) audioSettings.getSavedSettings().get(4);
        int channels = (audioSettings.getSavedSettings().get(5)).equals("mono") ? 1 : 2;

        if (encStr.equals("linear")) {
            if (signedStr.equals("signed")) {
                encoding = AudioFormat.Encoding.PCM_SIGNED;
            } else {
                encoding = AudioFormat.Encoding.PCM_UNSIGNED;
            }
        } else if (encStr.equals("alaw")) {
            encoding = AudioFormat.Encoding.ALAW; // ALaw is 8 Bit and Unsigned.
        }

        return new AudioFormat(encoding, rate, sampleSize,
                channels, (sampleSize/8)*channels, rate, bigEndian);
    }



    /**
     *  Resets current AudioSelection vector entries to default. Used in ResetToDefault
     */
    private void resetAudioSelections(){
        setAudioEncodingGroup(audioEncodingGroup[0]);
        setSampleRateGroup(sampleRateGroup[4]);
        setSampleSizeGroup(sampleSizeGroup[1]);
        setEndianGroup(endianGroup[1]);
        setSignGroup(signGroup[0]);
        setChannelGroup(channelGroup[1]);
    }

    /**
     *  Resets ComboBox visible selections to default.
     */
    private void resetComboBoxes(){
        audioEncodings.setSelectedItem(audioEncodingGroup[0]);
        sampleRates.setSelectedItem(sampleRateGroup[4]);
        sampleSizes.setSelectedItem(sampleSizeGroup[1]);
        endians.setSelectedItem(endianGroup[1]);
        signs.setSelectedItem(signGroup[0]);
        channels.setSelectedItem(channelGroup[1]);
    }

    /**
     * Method to prevent audioSelections and audioSettings vectors from being null on first launch
     *
     */
    private void loadSelections() {
        // First time launch
        if(audioSettings == null){
            // Add vector entries for all setting categories
            audioSelections.add(audioEncodingGroup[0]);
            audioSelections.add(sampleRateGroup[4]);
            audioSelections.add(sampleSizeGroup[1]);
            audioSelections.add(endianGroup[1]);
            audioSelections.add(signGroup[0]);
            audioSelections.add(channelGroup[1]);
            resetComboBoxes();
            audioSettings = new AudioSettings();
            audioSettings.setSavedSettings(audioSelections);
            System.out.println("AudioSettings and AudioSelections filled.");
        }
        else{
            // Fill vector entries for audioSelections
            audioSelections.add(audioEncodingGroup[0]);
            audioSelections.add(sampleRateGroup[4]);
            audioSelections.add(sampleSizeGroup[1]);
            audioSelections.add(endianGroup[1]);
            audioSelections.add(signGroup[0]);
            audioSelections.add(channelGroup[1]);
            audioSelections = (Vector) audioSettings.getSavedSettings().clone();
            // Change ComboBox displayed selections to saved settings.
            audioEncodings.setSelectedItem(getAudioEncodingSelection());
            sampleRates.setSelectedItem(getSampleRateSelection());
            sampleSizes.setSelectedItem(getSampleSizeSelection());
            endians.setSelectedItem(getEndianSelection());
            signs.setSelectedItem(getSignSelection());
            channels.setSelectedItem(getChannelSelection());
            System.out.println("AudioSelections filled.");
        }
    }

    /**
     *  Saves the user's chosen Audio settings.
     */
    public void saveSettings() {
        try {
            FileOutputStream file = new FileOutputStream("settings");
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(audioSettings);

            // Prevent false EoF exceptions
            out.writeObject(new EofIndicator());

            out.close();
            file.close();

            System.out.println("Settings have been saved");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Loads Settings file that the User has saved in their directory.
     *  If no Settings file is found then default audio format settings are set.
     */
    private void loadSettings(){
        try{
            FileInputStream file = new FileInputStream("settings");
            ObjectInputStream in = new ObjectInputStream(file);
            Object obj;

            // Check for End of file
            while (!((obj = in.readObject()) instanceof EofIndicator)){
                audioSettings = new AudioSettings();
                audioSettings = (AudioSettings) obj;
            }

            in.close();
            file.close();

            System.out.println("Settings: " + audioSettings.getSavedSettings());
            loadSelections();
            System.out.println("Settings have been loaded");
        } catch (FileNotFoundException e) {
            System.out.println("No saved settings found. Loading default settings.");
            loadSelections();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes the selection of an option from any of the dropdown boxes in the Settings Panel.
     * Adds to the audioSelection Vector.
     *
     * @param e , The event that occurs from selecting an item from the dropdown boxes.
     */
    public void actionPerformed(ActionEvent e) {
        final JComboBox<String> source = (JComboBox<String>) e.getSource();
        if (audioEncodings == source) {
            setAudioEncodingGroup((String) source.getSelectedItem());
//            System.out.println("Selections: "+ audioSelections);
        } else if (sampleRates == source) {
            setSampleRateGroup((String) source.getSelectedItem());
//            System.out.println("Selections: "+ audioSelections);
        } else if (sampleSizes == source) {
            setSampleSizeGroup((String) source.getSelectedItem());
//            System.out.println("Selections: "+ audioSelections);
        } else if (endians == source) {
            setEndianGroup((String) source.getSelectedItem());
//            System.out.println("Selections: "+ audioSelections);
        } else if (signs == source) { // Signs
            setSignGroup((String) source.getSelectedItem());
//            System.out.println("Selections: "+ audioSelections);
        } else {
            setChannelGroup((String) source.getSelectedItem());
//            System.out.println("Selections: "+ audioSelections);
        }

    }

    private void setAudioEncodingGroup(String selection) {
        audioSelections.set(0, selection);
    }

    private void setSampleRateGroup(String selection) {
        audioSelections.set(1, selection);
    }

    private void setSampleSizeGroup(String selection) {
        audioSelections.set(2, selection);
    }

    private void setEndianGroup(String selection) {
        audioSelections.set(3, selection);
    }

    private void setSignGroup(String selection) {
        audioSelections.set(4, selection);
    }

    private void setChannelGroup(String selection) {
        audioSelections.set(5, selection);
    }

    private String getAudioEncodingSelection(){
        return (String) audioSelections.elementAt(0);
    }

    private String getSampleRateSelection(){
        return (String) audioSelections.elementAt(1);
    }

    private String getSampleSizeSelection(){
        return (String) audioSelections.elementAt(2);
    }

    private String getEndianSelection(){
        return (String) audioSelections.elementAt(3);
    }

    private String getSignSelection(){
        return (String) audioSelections.elementAt(4);
    }

    private String getChannelSelection(){
        return (String) audioSelections.elementAt(5);
    }
}
