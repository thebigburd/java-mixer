import javax.swing.*;

public class MixerLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MixerGUI frame = new MixerGUI();
            }
        });
    }
}
