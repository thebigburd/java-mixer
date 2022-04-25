import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Vector;

/*
        Class for selecting the settings data related to AudioFormat
        Used in Reading and Writing Audio Files

        Bitrate = Sample Rate * Sample Size
        16 Bit sample size, big/little -endian relevant
 */

public class AudioSettingUI extends JPanel {

    MigLayout layout = new MigLayout();

    String[] audioEncodingGroup = { "Linear", "ulaw", "alaw"};
    String[] sampleRateGroup = { "8,000Hz","11,025Hz","16,000Hz","22,050Hz",
            "44,100Hz", "48,000Hz"};
    String[] sampleSizeGroup = {"8 Bit", "16 Bit"};
    String[] signGroup = {"Signed", "Unsigned"};

    Vector settings = new Vector();

    public AudioSettingUI(){
        JPanel settingsPanel = new JPanel(layout);
        settingsPanel.add(new JLabel("Encoding:"));
        settingsPanel.add(new JComboBox(audioEncodingGroup), "wrap");
        settingsPanel.add(new JLabel("Sample Rate:"));
        settingsPanel.add(new JComboBox(sampleRateGroup), "wrap");
        settingsPanel.add(new JLabel("Sample Size"));
        settingsPanel.add(new JComboBox(sampleSizeGroup), "wrap");
        settingsPanel.add(new JLabel("Signed?"));
        settingsPanel.add(new JComboBox(signGroup), "wrap");

    }




}
