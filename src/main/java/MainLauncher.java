import javax.swing.*;

public class MainLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JavaMixer frame = new JavaMixer();
            }
        });
    }
}
