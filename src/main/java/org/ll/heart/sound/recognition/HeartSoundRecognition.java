/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ll.heart.sound.recognition;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.ll.heart.sound.recognition.wav.WavFile;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundRecognition {

    public static void main(String[] args) {
        int index = 0;
        SimpleDateFormat ft = new SimpleDateFormat ("mm:ss.S");
        try {
            FileWriter fileWriter = new FileWriter("output.csv");
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(args[0]));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();
            long freq = wavFile.getSampleRate();

            // Create a buffer of 100 frames
            double[] buffer = new double[100 * numChannels];

            int framesRead;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            do {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, 100);

                // Loop through frames and look for minimum and maximum value
                for (int s = 0; s < framesRead * numChannels; s++) {
                    index++;
                    if (buffer[s] > max) {
                        max = buffer[s];
                    }
                    if (buffer[s] < min) {
                        min = buffer[s];
                    }
                    Date ts = new Date((index*1000)/freq);
                    String row = ft.format(ts) + String.format(";%.5f;\n", buffer[s]);
                    fileWriter.write(row);
                }
            } while (framesRead != 0);

            // Close the wavFile
            wavFile.close();
            fileWriter.close();

            // Output the minimum and maximum value
            System.out.printf("Min: %f, Max: %f\n", min, max);
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}