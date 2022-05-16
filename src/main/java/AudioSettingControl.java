import net.miginfocom.swing.MigLayout;

import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 */
class AudioSettingControl extends JPanel implements ActionListener {


    final String[] audioEncodingGroup = {"linear", "ulaw", "alaw"};
    final String[] sampleRateGroup = {"8000", "11025", "16000", "22050",
            "44100Hz", "48000Hz"};
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

        // Load Default Dropdown box selections
        loadSelections();
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
        JButton okB = new JButton("OK");
        okB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSelections();
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

    private void saveSelections() {
        try {
            audioSettings.setSavedSettings(audioSelections);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // INCOMPLETE
    public AudioFormat getAudioFormat() {
        Vector v = new Vector(audioSettings.getSavedSettings().size());
        Enumeration e = v.elements();

        while (e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100;
        int sampleSize = 16;
        String signed = "signed";
        int channels = 2;
        boolean bigEndian = true;

        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }

    public void setFormat(AudioFormat format) {

    }


    private void resetToDefault() {
        setAudioEncodingGroup(audioEncodingGroup[1]);
        setSampleRateGroup(sampleRateGroup[4]);
        setSampleSizeGroup(sampleSizeGroup[1]);
        setEndianGroup(endianGroup[1]);
        setSignGroup(signGroup[0]);
        setChannelGroup(channelGroup[1]);
    }

    /**
     * Method to prevent audioSelections array from being null
     */
    private void loadSelections() {
        audioSelections.add(audioEncodingGroup[0]);
        audioSelections.add(sampleRateGroup[0]);
        audioSelections.add(sampleSizeGroup[0]);
        audioSelections.add(endianGroup[0]);
        audioSelections.add(signGroup[0]);
        audioSelections.add(channelGroup[0]);
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
            System.out.println(audioSelections);
        } else if (sampleRates == source) {
            setSampleRateGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        } else if (sampleSizes == source) {
            setSampleSizeGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        } else if (endians == source) {
            setEndianGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        } else if (signs == source) { // Signs
            setSignGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        } else {
            setChannelGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
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

    public Vector getAudioSelections() {
        return audioSelections;
    }

}
