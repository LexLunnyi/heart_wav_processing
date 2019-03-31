package org.ll.heart.sound.recognition;

import org.ll.heart.sound.recognition.wav.WavContainer;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundRecognition {
    private static final int WINDOW_SIZE = 512;
    private static final int WINDOW_STEP = 64;

    public static void main(String[] args) {
        try {
            WavContainer wav = new WavContainer(args[0]);
            wav.saveCSV();
            // Output the minimum and maximum value
            System.out.printf("Min: %f, Max: %f\n", wav.getMinValue(), wav.getMaxValue());
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}