import net.miginfocom.swing.MigLayout;

import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


/*
        Class for selecting the settings data related to AudioFormat
        AudioFormat is used in Reading and Writing Audio Files

        A popup menu from clicking Settings.

        Bitrate = Sample Rate * Sample Size
        16 Bit sample size, big/little -endian relevant
 */

/**
 *
 */
public class AudioSettingUI extends JDialog implements ActionListener{

    private JPanel settingWindow;
    final String[] audioEncodingGroup = {"Linear", "ulaw", "alaw"};
    final String[] sampleRateGroup = {"8,000Hz", "11,025Hz", "16,000Hz", "22,050Hz",
            "44,100Hz", "48,000Hz"};
    final String[] sampleSizeGroup = {"8 Bit", "16 Bit"};
    final String[] endianGroup = {"Little Endian", "Big Endian"};
    final String[] signGroup = {"Signed", "Unsigned"};
    // Global ComboBoxes for Action Listeners
    JComboBox audioEncodings = new JComboBox(audioEncodingGroup);
    JComboBox sampleRates = new JComboBox(sampleRateGroup);
    JComboBox sampleSizes = new JComboBox(sampleSizeGroup);
    JComboBox endians = new JComboBox((endianGroup));
    JComboBox signs = new JComboBox(signGroup);


    // Unsaved Selections
    Vector audioSelections = new Vector<String>(5);

    public AudioSettingUI(JFrame parent) {
        // Call JDialog Constructor
        super(parent, "Settings", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Load User's Saved Settings
        loadSettings();
        // Initialise UI
        init();
    }

    public void init(){
        MigLayout layout = new MigLayout("wrap 2");
        settingWindow = new JPanel(layout);


        // Add Listeners to Combo Boxes
        audioEncodings.addActionListener(this);
        sampleRates.addActionListener(this);
        sampleSizes.addActionListener(this);
        endians.addActionListener(this);
        signs.addActionListener(this);

        // Add Dropdown Boxes to Panel
        settingWindow.add(new JLabel("Encoding:"));
        settingWindow.add(audioEncodings);
        settingWindow.add(new JLabel("Sample Rate:"));
        settingWindow.add(sampleRates);
        settingWindow.add(new JLabel("Sample Size:"));
        settingWindow.add(sampleSizes);
        settingWindow.add(new JLabel("Endian:"));
        settingWindow.add(endians);
        settingWindow.add(new JLabel("Signed?:"));
        settingWindow.add(signs);

        // Add Buttons
        JButton okB = new JButton("OK");
        okB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSelections();
                dispose();
            }
        });

        JButton cancelB = new JButton("Cancel");
        cancelB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JButton resetToDefaultB = new JButton("Reset to Default");
        resetToDefaultB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetToDefault();
            }
        });
        settingWindow.add(okB, "align left");
        settingWindow.add(cancelB, "tag cancel");
        settingWindow.add(resetToDefaultB,"tag right");

        // Pack and Show Panel
        getContentPane().add(settingWindow);
        pack();
        setVisible(true);
    }


    private void resetToDefault(){
        setAudioEncodingGroup(audioEncodingGroup[1]);
        setSampleRateGroup(sampleRateGroup[4]);
        setSampleSizeGroup(sampleSizeGroup[1]);
        setEndianGroup(endianGroup[1]);
        setSignGroup(signGroup[0]);
    }

    /**
     * Method to prevent audioSelections array from being null
     *
     */
    private void loadSettings(){
        audioSelections.add(audioEncodingGroup[0]);
        audioSelections.add(sampleRateGroup[0]);
        audioSelections.add(sampleSizeGroup[0]);
        audioSelections.add(endianGroup[0]);
        audioSelections.add(signGroup[0]);
    }

    private void saveSelections(){
        Object audioSettings = audioSelections.clone();
        System.out.println(audioSettings);
    }

    public void actionPerformed(ActionEvent e) {
        final JComboBox<String> source = (JComboBox<String>) e.getSource();
        if(audioEncodings == source){
            setAudioEncodingGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        }
        else if(sampleRates == source){
            setSampleRateGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        }
        else if(sampleSizes == source){
            setSampleSizeGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        }
        else if(endians == source){
            setEndianGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        }
        else{ // Signs
            setSignGroup((String) source.getSelectedItem());
            System.out.println(audioSelections);
        }

    }

    public void setAudioEncodingGroup(String selection){
        audioSelections.set(0, selection);
    }

    public void setSampleRateGroup(String selection){
        audioSelections.set(1, selection);
    }

    public void setSampleSizeGroup(String selection){
        audioSelections.set(2, selection);
    }

    public void setEndianGroup(String selection){
        audioSelections.set(3, selection);
    }

    public void setSignGroup(String selection){
        audioSelections.set(4, selection);
    }

    public Vector getAudioSelections() {
        return audioSelections;
    }

    public void setAudioSelections(Vector audioSelections) {
        this.audioSelections = audioSelections;
    }

}




