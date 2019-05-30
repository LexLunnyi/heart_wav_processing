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
import org.ll.heart.sound.recognition.MagnitudeHistogram;

/**
 *
 * @author aberdnikov
 * 
 * 
 * 1) Рефакторинг кода.
 * 2) Нормализовать энергию окна и магнитуду сигнала.
 * 3) Определиться с критерием завершения сигнала.
 */
public class WavContainer {

    private final List<HeartSoundPortion> data = new ArrayList<>();
    private final List<String> spectrogram = new ArrayList<>();
    
    private final String fileName;
    private final SimpleDateFormat tsFormat = new SimpleDateFormat("mm:ss.S");
    private Double maxValue = Double.MIN_VALUE;
    private Double minValue = Double.MAX_VALUE;
    private Double freqStep = 0.0D;
    private long sampleRate = 0;
    FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    
    private final Date LIMIT_TS_BEGIN = new Date(0);
    private final Date LIMIT_TS_END = new Date(1000);
    //private final Date LIMIT_TS_BEGIN = new Date(276);
    //private final Date LIMIT_TS_END = new Date(1276);
    private static final int WINDOW_SIZE = 64;  //DOI: 10.1109/ISETC.2012.6408110
    private static final int WINDOW_STEP = 2;   //DOI: 10.1109/ISETC.2012.6408110
    private static final double SPECTRUM_LOW = 55.0;
    private static final double SPECTRUM_HIGH = 165.0;
    MagnitudeHistogram mHisto = new MagnitudeHistogram(WINDOW_SIZE);
    
    private Double maxWindowEnergy = Double.MIN_VALUE;
    private Double maxMagnitude = Double.MIN_VALUE;

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

        sampleRate = wavFile.getSampleRate();
        freqStep = (double)sampleRate / (double)WINDOW_SIZE;

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
                Date ts = new Date((index * 1000) / sampleRate);
                if (ts.compareTo(LIMIT_TS_BEGIN) < 0) {
                    continue;
                }
                if (ts.compareTo(LIMIT_TS_END) > 0) {
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
                String row = tsFormat.format(heartSoundPortion.getTs()) + String.format(";%.5f;%.5f;%.5f;%.5f;\n", 
                        heartSoundPortion.getIn(), heartSoundPortion.getOut(), heartSoundPortion.getMagnitude(), heartSoundPortion.getWindowEnergy());
                fileWriter.write(row);
            }
        }
    }
    
    public void saveSpectrogramCSV() throws IOException {
        try (FileWriter fileWriter = new FileWriter("spectrogram.csv")) {
            for (String row : spectrogram) {
                fileWriter.write(row);
            }
        }
    }
    
    
    private Complex[] FourierProcessing(HeartSoundPortion curPortion, double[] input, Complex[] prev) {
        Complex[] res = transformer.transform(input, TransformType.FORWARD);
        int size = res.length;
        double curMagnitudeDiff = 0.0D;
        double curPhase = 0.0D;
        double curPhaseDiff = 0.0D;
        
        //String row = tsFormat.format(curPortion.getTs()) + ";";
        for (int i = 1; i < size/2; i++) {
            double curFreq = i * freqStep;
            //Prepare CSV-row for debugging;
            
            //Make bandpass filtration
            if ((curFreq <= SPECTRUM_LOW) || (curFreq >= SPECTRUM_HIGH)) {
                //Empty noise
                res[i] = new Complex(0);
                res[size-i] = new Complex(0);
            } else if (null != prev) {
                double diffAngle = calcPhaseSubtraction(prev[i], res[i], curFreq);
                double diffMagnitude = getMagnitudeSubtraction(prev[i], res[i], diffAngle);
                curPhaseDiff += diffAngle;
                curMagnitudeDiff += diffMagnitude;
                
                if (0.0D == curPhase) {
                    curPhase = calcPhase(res[i]);
                }
            }
            //Prepare CSV-row for debugging;
            //row += String.format("%.5f;", res[i].abs());
        }
        //row += "\n";
        //spectrogram.add(row);
        curPortion.setMagnitude(curMagnitudeDiff);
        curPortion.setPhase(curPhase);
        curPortion.setPhaseDiff(curPhaseDiff);
         
        return res;
    }
    
    
    
    
    public void makeOutput() {
        long size = data.size();
        double[] input = new double[WINDOW_SIZE];
        int FIRST = WINDOW_SIZE/2 - WINDOW_STEP/2;
        double freqStep = (double)sampleRate / (double)WINDOW_SIZE;
        
        Complex[] output = null;  
        Complex[] fftData = null;
        double phase = 0.0D;
        double windowEnergy = 0.0D;
        double magnitude = 0.0D;
        double diffPhase = 0.0D;
        
        for(int index = 0; index < size; index += WINDOW_STEP) {
            System.out.print("index: " + Integer.toString(index) + "\n");
            if (index + WINDOW_SIZE >= size) {
                break;
            }
            for(int j = 0; j < WINDOW_SIZE; j++) {
                input[j] = data.get(index + j).getIn();
            }
            
            HeartSoundPortion curPortion = data.get(index + FIRST);
            fftData = FourierProcessing(curPortion, input, fftData);
                    
            magnitude = curPortion.getMagnitude();
            phase = curPortion.getPhase();
            diffPhase = curPortion.getPhaseDiff();
            windowEnergy = 0;
            
            output = transformer.transform(fftData, TransformType.INVERSE);
            for(int j = FIRST; j < (FIRST + WINDOW_STEP); j++) {
                HeartSoundPortion cur = data.get(index + j);
                if (output[j].getReal() < 0) {
                    cur.setOut(output[j].abs()*(-1.0));
                } else {
                    cur.setOut(output[j].abs());
                }
                windowEnergy += Math.pow(cur.getOut(), 2);
            }
            
            //Set subband parameters
            for(int j = FIRST; j < (FIRST + WINDOW_STEP); j++) {
                HeartSoundPortion cur = data.get(index + j);
                cur.setWindowEnergy(windowEnergy);
                cur.setMagnitude(magnitude);
                cur.setPhase(phase);
                cur.setPhaseDiff(diffPhase);
            }
            updateExtremums(curPortion);
        }
        normalize();
        s1s2Detection();
        System.out.println(mHisto.toString());
        System.out.println("Threshold: " + mHisto.getThreshold() + "\n");
    }
    
    
    private double getMagnitudeSubtraction(Complex prev, Complex cur, double angle) {
        double fEdge = prev.abs();
        double sEdge = cur.abs();
        return Math.sqrt(Math.pow(fEdge, 2) + Math.pow(sEdge, 2) - 2*fEdge*sEdge*Math.cos(angle*Math.PI));
    }
    
    
    
    private double calcPhase(Complex cur) {
        double arg = (Math.atan2(cur.getImaginary(), cur.getReal()) / Math.PI)*180.0;
        if (cur.getImaginary() < 0) {
            arg += 360.0;
        }
        return arg/360.0;
    }
    
    
    private double calcPhaseSubtraction(Complex prev, Complex cur, double curFreq) {
        double phaseOffset = ((double)WINDOW_STEP / (double)sampleRate)*curFreq;
        double res = calcPhase(cur) - calcPhase(prev) + phaseOffset;
        if (res < -0.5) {
            res += 1.0;
        } else if (res > 0.5) {
            res -= 1.0;
        }
        return res;
    }
    
    
    private void updateExtremums(HeartSoundPortion curPortions) {
        if (curPortions.getMagnitude() > maxMagnitude) {
            maxMagnitude = curPortions.getMagnitude();
        }
        if (curPortions.getWindowEnergy() > maxWindowEnergy) {
            maxWindowEnergy = curPortions.getWindowEnergy();
        }
    }
    
    
    public void normalize() {
        for (HeartSoundPortion heartSoundPortion : data) {
            heartSoundPortion.setMagnitude(heartSoundPortion.getMagnitude() / maxMagnitude);
            heartSoundPortion.setWindowEnergy(heartSoundPortion.getWindowEnergy() / maxWindowEnergy);
            mHisto.push(heartSoundPortion.getMagnitude());
        }
    }
    
    
    public void s1s2Detection() {
        double threshold = mHisto.getThreshold();
        for (HeartSoundPortion heartSoundPortion : data) {
            double magnitude = heartSoundPortion.getMagnitude();
            if (magnitude > threshold) {
                heartSoundPortion.setWindowEnergy(1.0D);
            } else {
                heartSoundPortion.setWindowEnergy(0.0D);
            }
        }
    }
}
