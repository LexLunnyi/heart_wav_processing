package org.ll.heart.sound.recognition;

import org.ll.heart.sound.recognition.utils.WindowParams;
import org.ll.heart.sound.recognition.utils.RecordStatistic;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import org.apache.commons.math3.complex.Complex;
import org.ll.heart.sound.recognition.fdomain.FFTFrequencyDomain;
import org.ll.heart.sound.recognition.fdomain.FrequencyDomainService;
import org.ll.heart.sound.recognition.fdomain.HHTFrequencyDomain;
import org.ll.heart.sound.recognition.fdomain.WaveletFrequencyDomain;
import org.ll.heart.sound.recognition.filter.BlankFilter;
import org.ll.heart.sound.recognition.filter.FilterService;
import org.ll.heart.sound.recognition.segmentation.SegmentationService;
import org.ll.heart.sound.recognition.segmentation.SegmentationServiceFactory;
import org.ll.heart.sound.recognition.spectrogram.PixelARGB;
import org.ll.heart.sound.recognition.wav.WavFile;
import org.ll.heart.sound.recognition.wav.WavFileException;

/**
 *
 * @author aberdnikov
 */
public class PCGWrapper {
    final static String KEY_NEED_NORMALIZATION = "KEY_NEED_NORMALIZATION";

    final Options options;
    final WavFile wavFile;
    final int windowSize;
    final Date WAV_LIMIT_BEGIN;
    Integer PCG_LIMIT_BEGIN = null;
    final Date WAV_LIMIT_END;
    Integer PCG_LIMIT_END = null;
    final double sampleRate;
    final int sampleRateDivider;

    final double[] data;
    final List<SignalPortion> PCG = new ArrayList<>();

    FrequencyDomainService freqService;
    FilterService filterService;
    SegmentationService segmentationService;
    final RecordStatistic globalStat = new RecordStatistic();

    public PCGWrapper(File in, Options options) throws IOException, WavFileException {
        this.options = options;
        WAV_LIMIT_BEGIN = new Date(options.getWavLengthMin());
        WAV_LIMIT_END = new Date(options.getWavLengthMax());
        // Open the wav file specified as the first argument
        wavFile = WavFile.openWavFile(in);
        if (options.isAppPcgNarrow()) {
            sampleRateDivider = (int) Math.floor(wavFile.getSampleRate() / options.getAppPcgHigh());
            sampleRate = wavFile.getSampleRate() / sampleRateDivider;
        } else {
            sampleRate = wavFile.getSampleRate();
            sampleRateDivider = 1;
        }
        // Display information about the wav file
        wavFile.display();
        // Get the number of audio channels in the wav file
        int numChannels = wavFile.getNumChannels();
        if (numChannels > 1) {
            throw new IOException("Stereo records still not supported");
        }
        //Get window size and step
        if (options.isWindowAuto()) {
            WindowParams autoParams = new WindowParams(options.getWindowFrequencyRate(), (int) sampleRate);
            windowSize = autoParams.getSize();
        } else {
            windowSize = options.getWindowSize();
        }
        //Check record size
        long frameSize = wavFile.getNumFrames();
        if ((frameSize < 1) || (frameSize > Integer.MAX_VALUE)) {
            throw new IOException("Length of record is incorrect");
        }
        data = new double[(int)(Math.floor(frameSize/sampleRateDivider))];
        readFile();
        // Close the wavFile
        wavFile.close();
        //Update borders
        PCG_LIMIT_BEGIN = (null == PCG_LIMIT_BEGIN) ? 0 : PCG_LIMIT_BEGIN;
        PCG_LIMIT_END = (null == PCG_LIMIT_END) ? data.length - windowSize : PCG_LIMIT_END - windowSize;
    }

    private void readFile() throws IOException, WavFileException {
        int buffSize = sampleRateDivider * wavFile.getNumChannels();
        // Create a buffer of N frames
        double[] buffer = new double[buffSize];
        int index = 0;
        
        // Read frames into buffer
        while (0 != wavFile.readFrames(buffer, sampleRateDivider)) {
            Date ts = new Date((long)((index * 1000) / getSampleRate()));
            if (options.isWavLengthLimited()) {
                if (ts.compareTo(WAV_LIMIT_BEGIN) < 0) {
                    data[index] = 0.0;
                } else if (null == PCG_LIMIT_BEGIN) {
                    PCG_LIMIT_BEGIN = index;
                }
                if (ts.compareTo(WAV_LIMIT_END) > 0) {
                    data[index] = 0.0;
                } else {
                    PCG_LIMIT_END = index;
                }
            }
            data[index] = buffer[0];//Only when mono-record
            index++;
        }
    }

    public void process(String out) throws IOException {
        Set<String> params = new HashSet<>();
        
        configure(params);
        getSignalPortions(!params.contains(KEY_NEED_NORMALIZATION));
        
        if (params.contains(KEY_NEED_NORMALIZATION)) {
            PCG.forEach(sp -> {
                segmentationService.process(sp);
            });
            segmentationService.finish();
        }
        
        if (options.isAppSpectrogramSave()) {
            saveWithSpectrogram(out);
        } else {
            save(out);
        }
    }

    //Configure services for transorm to frequency domain, filtration and segmentation
    private void configure(Set<String> params) throws IllegalStateException {
        //Configure getting spectrum way
        switch (options.getAppSpectrumWay()) {
            case FFT:
                setFrequencyService(new FFTFrequencyDomain(getSampleRate(), getWindowSize()));
                break;
            case DWT:
                setFrequencyService(new WaveletFrequencyDomain(getSampleRate(), getWindowSize()));
                break;
            case HHT:
                setFrequencyService(new HHTFrequencyDomain());
                break;
            default:
                throw new IllegalStateException("The getting spectrum way didn't set");
        }
        //Configure segmentation way
        SegmentationServiceFactory factory = new SegmentationServiceFactory(globalStat, windowSize, options.getAppSegmentationThreshold());
        switch (options.getAppSegmentationType()) {
            case MINMAX:
                setSegmentService(factory.createMinMax(options.getAppSegmentationLogicType()));
                params.add(KEY_NEED_NORMALIZATION);
                break;
            case MEAN:
                setSegmentService(factory.createMean(options.getAppSegmentationLogicType()));
                params.add(KEY_NEED_NORMALIZATION);
                break;
            case HISTOGRAM:
                setSegmentService(factory.createHistogram(options.getAppSegmentationLogicType()));
                break;
            default:
                throw new IllegalStateException("The segmentation way didn't set");
        }
        //setFilterService(new BandpassFilter(getSampleRate()/getWindowSize(), options.getBandpassLow(), options.getBandpassHight()));
        setFilterService(new BlankFilter());
    }

    private void setFrequencyService(FrequencyDomainService fservie) {
        this.freqService = fservie;
    }

    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }

    public void setSegmentService(SegmentationService segmentService) {
        this.segmentationService = segmentService;
    }

    //main action
    private void getSignalPortions(boolean withSegmentation) throws IOException {
        PCG.clear();
        if (PCG_LIMIT_END <= PCG_LIMIT_BEGIN) {
            throw new IOException("The start point or end point is incorrect");
        }
        //SignalPortion prev = null;
        for (int i = PCG_LIMIT_BEGIN; i < PCG_LIMIT_END; i++) {
            //Calc time
            Date ts = new Date((long) ((i * 1000) / getSampleRate()));
            //Create signal portion
            SignalPortion portion = new SignalPortion(i, ts, data[i], Arrays.copyOfRange(data, i, i + windowSize));
            freqService.forward(portion);
            //filterService.filter(portion);
            freqService.features(portion);
            
            if (withSegmentation) {
                segmentationService.process(portion);
            }
            globalStat.add(portion);
            PCG.add(portion);
            //prev = portion;
        }    
        if (withSegmentation) {
            segmentationService.finish();
        }
    }
    
    private boolean checkIndex(int index) {
        return ((index == 0) || (index == 200) || (index == 450) || 
                (index == 700) || (index == 2400) || (index == 7150) || 
                (index == 15800));
    }

    private void save(String out) throws IOException {
        try (FileWriter fileWriter = new FileWriter(out + ".csv")) {
            fileWriter.write(PCG.get(0).getCSVColumnsNames(false) + "\n");
            int index = 0;
            for (SignalPortion signal : PCG) {
                fileWriter.write(signal.toCSV());
//                if (signal.getFreqAdd() != null) {
//                    HHTPortion hp = (HHTPortion)signal.getFreqAdd();
//                    if (checkIndex(index)) hp.save(out + "_" + Integer.toString(index) + ".csv");
//                    index++;
//                }
            }
        }
    }
    
    private double getMax(Complex[] s) {
        double res = 0.0;
        for (Complex g : s) {
            res = Math.max(res, g.abs());
        }
        return res;
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
                double max = getMax(s);
                int hIndex = 0;
                for (Complex g : s) {
                    int bright = (int) Math.round(255.0 * (g.abs())/max);
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

    public double getSampleRate() {
        return sampleRate;
    }
}