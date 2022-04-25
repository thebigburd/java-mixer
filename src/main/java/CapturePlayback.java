//import javax.sound.sampled.*;
//import javax.swing.*;
//import java.awt.event.ActionListener;
//import java.io.File;
//
//
///**
// *  Class to read data to an Input Channel
// *  and write data to an Output Channel
// */
//
//public class CapturePlayback implements Runnable {
//
//    Capture capture = new Capture();
//    Playback playback = new Playback();
//
//    File file;
//    AudioInputStream audioInputStream;
//
//    String errorString;
//
//
//    public void start(){
//    }
//
//    public void stop(){
//    }
//
//    public void run() {
//    }
//
////    class Capture implements Runnable {
////
////        TargetDataLine line;  // Line can be a SourceDataLine or a Clip line
////        Thread thread;
////
////        public void start(){
////            errorString = null;
////            thread = new Thread(this);
////            thread.setName("Capture");
////            thread.start();
////
////        }
////
////        public void stop(){
////            thread = null;
////        }
////
////        public void run() {
////
////            audioInputStream = null;
////
////
////        }
////    }
//
//
//    private class Playback implements Runnable {
//
//
//
//        @Override
//        public void run() {
//
//        }
//    }
//}
