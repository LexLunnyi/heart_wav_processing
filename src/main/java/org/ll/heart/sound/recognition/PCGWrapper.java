package org.ll.heart.sound.recognition;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import org.ll.heart.sound.recognition.fdomain.FrequencyDomainFFT;
import org.ll.heart.sound.recognition.fdomain.FrequencyDomainService;
import org.ll.heart.sound.recognition.wav.WavFile;
import org.ll.heart.sound.recognition.wav.WavFileException;

/**
 *
 * @author aberdnikov
 */
public class PCGWrapper {
    private final Options options;
    private final WavFile wavFile;
    private final int WINDOW_SIZE;
    private final Date WAV_LIMIT_BEGIN;
    private final Date WAV_LIMIT_END;
    private final double[] data;
    
    FrequencyDomainService fservie;
    
    public PCGWrapper(File in, Options options) throws IOException, WavFileException {
        this.options = options;
        this.WAV_LIMIT_BEGIN = new Date(options.getWavLengthMin());
        this.WAV_LIMIT_END = new Date(options.getWavLengthMax());
        // Open the wav file specified as the first argument
        wavFile = WavFile.openWavFile(in);
        // Display information about the wav file
        wavFile.display();
        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();
        if (numChannels > 1) {
            throw new IOException("Stereo records still not supported");
        }
        //Get window size and step
        if (options.isWindowAuto()) {
            WindowParams autoParams = new WindowParams(options.getWindowFrequencyRate(), (int)wavFile.getSampleRate());
            WINDOW_SIZE = autoParams.getSize();
        } else {
            WINDOW_SIZE = options.getWindowSize();
        }
        //Check record size
        long frameSize = wavFile.getNumFrames();
        if ((frameSize < 1) || (frameSize > Integer.MAX_VALUE)) {
            throw new IOException("Length of record is incorrect");
        }
        data = new double[(int)frameSize];
        readFile();
        // Close the wavFile
        wavFile.close();
    }
    
    
    private void readFile() throws IOException, WavFileException {
        int buffSize = WINDOW_SIZE * wavFile.getNumChannels();
        // Create a buffer of N frames
        double[] buffer = new double[buffSize];
        int framesRead;
        long index = 0;
        do {
            // Read frames into buffer
            framesRead = wavFile.readFrames(buffer, WINDOW_SIZE);
            // Loop through frames and look for minimum and maximum value
            
            for (int s = 0; s < framesRead * wavFile.getNumChannels(); s++) {
                index++;
                int i = (int)index-1;
                Date ts = new Date((index * 1000) / wavFile.getSampleRate());
                //FIXME do we need to limit reading here, maybe only processing?
                if (options.isWavLengthLimited()) {
                    if (ts.compareTo(WAV_LIMIT_BEGIN) < 0) {
                        data[i] = 0.0;
                    }
                    if (ts.compareTo(WAV_LIMIT_END) > 0) {
                        framesRead = 0;
                        data[i] = 0.0;
                    }
                }
                data[i] = buffer[s];
            }
        } while (framesRead != 0);      
    }
    
    
    public void process(File out) {
        configure();
        try {
            process();
            save(out);
        } catch (Exception ex) {
            System.err.println("ERROR: PCGWrapper.process -> " + ex);
        }
    }
        
    //Configure services for transorm to frequency domain, filtration and segmentation
    private void configure() {
        setFrequencyService(new FrequencyDomainFFT());
    }
    
    private void setFrequencyService(FrequencyDomainService fservie) {
        this.fservie = fservie;
    }
    
    //main action
    private void process() {
        int size = data.length;
        
        for(int i = 0; i < size - WINDOW_SIZE; i++) {
            SignalPortion portion = new SignalPortion(Arrays.copyOfRange(data, i, i + WINDOW_SIZE));
            fservie.process(portion);
        }
    }
    
    
    private void save(File out) {
        
    }
}
