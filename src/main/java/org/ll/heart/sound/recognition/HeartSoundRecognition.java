package org.ll.heart.sound.recognition;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundRecognition {
    public static void main(String[] args) {
        try {
            HeartSoundProcessor processor = new HeartSoundProcessor(args[0]);
            processor.processFiles();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}