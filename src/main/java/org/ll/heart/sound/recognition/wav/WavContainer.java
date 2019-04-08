package org.ll.heart.sound.recognition.wav;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
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
    private Double freqStep = 0.0;
    FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    
    private final Date LIMIT_TS = new Date(500);
    private static final int WINDOW_SIZE = 512;
    private static final int WINDOW_STEP = 64;
    private static final double SPECTRUM_LOW = 1.0;
    private static final double SPECTRUM_HIGH = 11000.0;

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
        freqStep = (double)freq / (double)WINDOW_SIZE;

        // Create a buffer of 100 frames
        double[] buffer = new double[WINDOW_SIZE * numChannels];

        int framesRead;
        do {
            // Read frames into buffer
            framesRead = wavFile.readFrames(buffer, WINDOW_SIZE);

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
                if (ts.compareTo(LIMIT_TS) > 0) {
                    framesRead = 0;
                    break;
                }
                data.add(new HeartSoundPortion(ts, buffer[s]));
            }
            
            //
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
                String row = tsFormat.format(heartSoundPortion.getTs()) + String.format(";%.5f;%.5f;\n", heartSoundPortion.getIn(), heartSoundPortion.getOut());
                fileWriter.write(row);
            }
        }
    }
    
    
    public Complex[] FourierProcessing(double[] input) {
        Complex[] res = transformer.transform(input, TransformType.FORWARD);
        long size = res.length;

       
        for (int i = 0; i < size; i++) {
            double curFreq = i * freqStep;

            if ((curFreq <= SPECTRUM_LOW) || (curFreq >= SPECTRUM_HIGH)) {
                //Empty noise
                res[i] = new Complex(0, 0);
            } else {
                //Calc magnitude
            }
        }
      
        
        return transformer.transform(res, TransformType.INVERSE);
    }
    
    
    
    
    public void makeOutput() {
        long size = data.size();
        double[] input = new double[WINDOW_SIZE];
        
        for(int index = 0; index < size; index += WINDOW_STEP) {
            if (index + WINDOW_SIZE >= size) {
                break;
            }
            for(int j = 0; j < WINDOW_SIZE; j++) {
                input[j] = data.get(index + j).getIn();
            }
            Complex[] output = FourierProcessing(input);
            for(int j = 0; j < WINDOW_STEP; j++) {
                if (output[j].getImaginary() < 0) {
                    data.get(index + j).setOut(output[j].abs()*(-1.0));
                } else {
                    data.get(index + j).setOut(output[j].abs());
                }
            }
        }
    }
}
