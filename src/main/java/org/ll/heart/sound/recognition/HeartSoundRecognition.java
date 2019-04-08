package org.ll.heart.sound.recognition;

import org.ll.heart.sound.recognition.wav.WavContainer;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundRecognition {

    public static void main(String[] args) {
        try {
            WavContainer wav = new WavContainer(args[0]);
            wav.makeOutput();
            wav.saveCSV();
            // Output the minimum and maximum value
            System.out.printf("Min: %f, Max: %f\n", wav.getMinValue(), wav.getMaxValue());
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}