import javax.swing.*;

/**
 *  Application Launcher
 *
 * @version 1.00
 * @author Richard Lam
 */
public class MainLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JavaMixer frame = new JavaMixer();
            }
        });
    }
}
