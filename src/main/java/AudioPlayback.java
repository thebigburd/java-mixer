import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;


/**
 *  Class to write data to an Output Channel
 */

public class AudioPlayback implements Runnable {

    DataLine line;
    Thread thread;

    String errorString;

    public void start(){
        thread = new Thread(this);
        thread.setName("Playback");
        thread.start();
    }

    public void stop(){
        thread = null;
    }

    public void run() {

    }
}
