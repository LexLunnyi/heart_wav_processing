package org.ll.heart.sound.recognition.wav;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.ll.heart.sound.recognition.HeartSoundPortion;

/**
 *
 * @author aberdnikov
 */
public class WavContainer {

    private final List<HeartSoundPortion> data = new ArrayList<>();
    private final String fileName;
    private final SimpleDateFormat tsFormat = new SimpleDateFormat("mm:ss.S");
    private Double maxValue = Double.MIN_VALUE;
    private Double minValue = Double.MAX_VALUE;

    public WavContainer(String fileName) throws IOException, Exception {
        this.fileName = fileName;

        long index = 0;
        // Open the wav file specified as the first argument
        WavFile wavFile = WavFile.openWavFile(new File(fileName));

        // Display information about the wav file
        wavFile.display();

        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();
        if (numChannels > 1) {
            throw new Exception("Stereo records still not supported");
        }

        long freq = wavFile.getSampleRate();

        // Create a buffer of 100 frames
        double[] buffer = new double[100 * numChannels];

        int framesRead;
        do {
            // Read frames into buffer
            framesRead = wavFile.readFrames(buffer, 100);

            // Loop through frames and look for minimum and maximum value
            for (int s = 0; s < framesRead * numChannels; s++) {
                index++;
                if (buffer[s] > maxValue) {
                    maxValue = buffer[s];
                }
                if (buffer[s] < minValue) {
                    minValue = buffer[s];
                }
                Date ts = new Date((index * 1000) / freq);
                data.add(new HeartSoundPortion(ts, buffer[s]));
            }
        } while (framesRead != 0);
        // Close the wavFile
        wavFile.close();
    }
    
    
    

    public Double getMaxValue() {
        return maxValue;
    }

    
    
    
    public Double getMinValue() {
        return minValue;
    }

    
    
    
    
    public void saveCSV() throws IOException {
        try (FileWriter fileWriter = new FileWriter("output.csv")) {
            for (HeartSoundPortion heartSoundPortion : data) {
                String row = tsFormat.format(heartSoundPortion.getTs()) + String.format(";%.5f;\n", heartSoundPortion.getIn());
                fileWriter.write(row);
            }
        }
    }
}
