package org.ll.heart.sound.recognition;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.math3.complex.Complex;
import org.ll.heart.sound.recognition.fdomain.FrequencyDomainFFT;
import org.ll.heart.sound.recognition.fdomain.FrequencyDomainService;
import org.ll.heart.sound.recognition.filter.FilterBandpass;
import org.ll.heart.sound.recognition.filter.FilterService;
import org.ll.heart.sound.recognition.spectrogram.PixelARGB;
import org.ll.heart.sound.recognition.wav.WavFile;
import org.ll.heart.sound.recognition.wav.WavFileException;

/**
 *
 * @author aberdnikov
 */
public class PCGWrapper {
    private final Options options;
    private final WavFile wavFile;
    private final int windowSize;
    private final Date WAV_LIMIT_BEGIN;
    private Integer PCG_LIMIT_BEGIN = null;
    private final Date WAV_LIMIT_END;
    private Integer PCG_LIMIT_END = null;
    
    private final double[] data;
    private final List<SignalPortion> PCG = new ArrayList<>();
    
    private FrequencyDomainService freqService;
    private FilterService filterService;
    
    public PCGWrapper(File in, Options options) throws IOException, WavFileException {
        this.options = options;
        WAV_LIMIT_BEGIN = new Date(options.getWavLengthMin());
        WAV_LIMIT_END = new Date(options.getWavLengthMax());
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
            windowSize = autoParams.getSize();
        } else {
            windowSize = options.getWindowSize();
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
        int buffSize = windowSize * wavFile.getNumChannels();
        // Create a buffer of N frames
        double[] buffer = new double[buffSize];
        int framesRead;
        long index = 0;
        do {
            // Read frames into buffer
            framesRead = wavFile.readFrames(buffer, windowSize);
            // Loop through frames and look for minimum and maximum value
            for (int s = 0; s < framesRead * wavFile.getNumChannels(); s++) {
                index++;
                int i = (int)index-1;
                Date ts = new Date((index * 1000) / wavFile.getSampleRate());
                if (options.isWavLengthLimited()) {
                    if (ts.compareTo(WAV_LIMIT_BEGIN) < 0) {
                        data[i] = 0.0;
                    } else if (null == PCG_LIMIT_BEGIN) {
                        PCG_LIMIT_BEGIN = i;
                    }
                    if (ts.compareTo(WAV_LIMIT_END) > 0) {
                        data[i] = 0.0;
                    } else {
                        PCG_LIMIT_END = i;
                    }
                }
                data[i] = buffer[s];
            }
        } while (framesRead != 0);      
    }
    
    
    public void process(String out) throws IOException {
        configure();
        process();
        if (options.isAppSpectrogramSave()) {
            saveWithSpectrogram(out);            
        } else {
            save(out);
        }
    }
        
    //Configure services for transorm to frequency domain, filtration and segmentation
    private void configure() {
        setFrequencyService(new FrequencyDomainFFT());
        setFilterService(new FilterBandpass());
    }
    
    private void setFrequencyService(FrequencyDomainService fservie) {
        this.freqService = fservie;
    }

    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }
    
    //main action
    private void process() throws IOException {
        PCG.clear();
        
        int start = (null == PCG_LIMIT_BEGIN) ? 0 : PCG_LIMIT_BEGIN;
        int end = (null == PCG_LIMIT_END) ? data.length - windowSize : PCG_LIMIT_END - windowSize;
        if (end <= start) {
            throw new IOException("The start point or end point is incorrect");
        }
        
        for(int i = start; i < end; i++) {
            //We take midle element of the window
            int index = i + windowSize / 2;
            //Calc time
            Date ts = new Date((index * 1000) / wavFile.getSampleRate());
            //Create signal portion
            SignalPortion portion = new SignalPortion(ts, data[index], Arrays.copyOfRange(data, i, i + windowSize));
            freqService.forward(portion);
            filterService.filter(portion);
            freqService.inverse(portion);
            PCG.add(portion);
        }
    }


    private void save(String out) throws IOException {
        try (FileWriter fileWriter = new FileWriter(out + ".csv")) {
            fileWriter.write(PCG.get(0).getCSVColumnsNames(false) + "\n");
            for (SignalPortion signal : PCG) {
                fileWriter.write(signal.toCSV());
            }
        }
    }    
    
    
    private void saveWithSpectrogram(String out) throws IOException {
        int w = PCG.size();
        int h = PCG.get(0).getSpectrum().length;
        BufferedImage dst = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        try (FileWriter fileWriter = new FileWriter(out + ".csv")) {
            fileWriter.write(PCG.get(0).getCSVColumnsNames(false) + "\n");
            int wIndex = 0;
            for (SignalPortion signal : PCG) {
                fileWriter.write(signal.toCSV());
                Complex[] s = signal.getSpectrum();
                int hIndex = 0;
                for (Complex g : s) {
                    int bright = (int)(255.0 * g.abs());
                    PixelARGB p = new PixelARGB(255, bright, bright, bright);
                    dst.setRGB(wIndex, hIndex, p.getPixel());
                    hIndex++;
                }
                wIndex++;
            }
        }
        
        ImageIO.write(dst, "png", new File(out + ".png"));
    }
    
    
    public int getWindowSize() {
        return windowSize;
    }

    public WavFile getWavFile() {
        return wavFile;
    }
}