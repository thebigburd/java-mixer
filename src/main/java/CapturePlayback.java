import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;


/**
 *  Class to read data to an Input Channel
 *  and write data to an Output Channel
 */

public class CapturePlayback implements Runnable {

    AudioSettings audioSettings;
    File file;
    AudioInputStream audioInputStream;

    String errorString;

    Playback playback = new Playback();




    public void start(){
    }

    public void stop(){
    }

    public void run() {
    }

//    class Capture implements Runnable {
//
//        TargetDataLine line;  // Line can be a SourceDataLine or a Clip line
//        Thread thread;
//
//        public void start(){
//            errorString = null;
//            thread = new Thread(this);
//            thread.setName("Capture");
//            thread.start();
//
//        }
//
//        public void stop(){
//            thread = null;
//        }
//
//        public void run() {
//
//            audioInputStream = null;
//
//
//        }
//    }


    private class Playback implements Runnable {

        SourceDataLine line;
        Thread thread;


        public void start() {
            errorString = null;
            thread = new Thread(this);
            thread.setName("Playback");
            thread.start();
        }

        @Override
        public void run() {

        }
    }
}
