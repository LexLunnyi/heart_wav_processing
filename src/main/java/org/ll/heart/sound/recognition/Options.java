package org.ll.heart.sound.recognition;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.ll.heart.sound.recognition.fdomain.FTransormType;
import org.ll.heart.sound.recognition.segmentation.SegmentationLogicType;
import org.ll.heart.sound.recognition.segmentation.SegmentationType;

/**
 *
 * @author aberdnikov
 */

public class Options {
    int windowSize;
    int windowStep;
    boolean windowAuto;
    int windowFrequencyRate;
    double bandpassLow;
    double bandpassHight;
    String inputDir;
    String outputDir;
    boolean wavLengthLimited;
    int wavLengthMin;
    int wavLengthMax;
    boolean appSpectrogramSave;
    int appPcgHigh;
    boolean appPcgNarrow;
    FTransormType appSpectrumWay;
    SegmentationType appSegmentationType;
    SegmentationLogicType appSegmentationLogicType;
    double appSegmentationThreshold;
    List<HeartSoundCategory> categories = new ArrayList();
    
    public Options(String path) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(path);
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            appSpectrogramSave = Boolean.parseBoolean(prop.getProperty("app.spectrogram.save"));
            appPcgNarrow = Boolean.parseBoolean(prop.getProperty("app.pcg.narrow"));
            appPcgHigh = Integer.parseInt(prop.getProperty("app.pcg.high"));
            appSpectrumWay = FTransormType.valueOf(prop.getProperty("app.spectrum.way", FTransormType.NONE.name()));
            appSegmentationType = SegmentationType.valueOf(prop.getProperty("app.segmentation.type", SegmentationType.NONE.name()));
            appSegmentationLogicType = SegmentationLogicType.valueOf(prop.getProperty("app.segmentation.logic.type", SegmentationLogicType.NONE.name()));
            appSegmentationThreshold = Double.parseDouble(prop.getProperty("app.segmentation.threshold", "0"));
            
            windowSize = Integer.parseInt(prop.getProperty("window.size"));
            windowStep = Integer.parseInt(prop.getProperty("window.step"));
            windowAuto = Boolean.parseBoolean(prop.getProperty("window.auto"));
            windowFrequencyRate = Integer.parseInt(prop.getProperty("window.frequency.rate"));
            
            bandpassLow = Double.parseDouble(prop.getProperty("bandpass.low"));
            bandpassHight = Double.parseDouble(prop.getProperty("bandpass.hight"));
            
            inputDir = prop.getProperty("data.input.dir");
            outputDir = prop.getProperty("data.output.dir");
            
            wavLengthLimited = Boolean.parseBoolean(prop.getProperty("wav.length.limited"));
            wavLengthMin = Integer.parseInt(prop.getProperty("wav.length.min"));
            wavLengthMax = Integer.parseInt(prop.getProperty("wav.length.max"));

            int categoriesCount = Integer.parseInt(prop.getProperty("categories.count"));
            for(int i = 1; i <= categoriesCount; i++) {
                String catName = prop.getProperty("categories[" + i + "].name");
                String catPath = prop.getProperty("categories[" + i + "].path");
                String catTag = prop.getProperty("categories[" + i + "].tag");
                categories.add(new HeartSoundCategory(i, catName, catPath, catTag));
            }

            printProperties();
        } catch (IOException ex) {
            System.out.println("Error read options: " + ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println("Error close config-file: " + e);
                }
            }
        }
    }



    private void printProperties() {
        System.out.println("-------------------------------------------------------");
        System.out.println("OPTIONS: ");
        System.out.println("  ");
        System.out.println("  appSpectrogramSave -> " + Boolean.toString(appSpectrogramSave));
        System.out.println("  appPcgNarrow -> " + Boolean.toString(appPcgNarrow));
        System.out.println("  appPcgHigh -> " + Integer.toString(appPcgHigh));
        System.out.println("  appSpectrumWay -> " + appSpectrumWay.name());
        System.out.println("  appSegmentationType -> " + appSegmentationType.name());
        System.out.println("  appSegmentationLogicType -> " + appSegmentationLogicType.name());
        System.out.println("  appSegmentationThreshold -> " + Double.toString(appSegmentationThreshold));
        System.out.println("  ");
        System.out.println("  windowSize -> " + Integer.toString(windowSize));
        System.out.println("  windowStep -> " + Integer.toString(windowStep));
        System.out.println("  windowAuto -> " + Boolean.toString(windowAuto));
        System.out.println("  windowFrequencyRate -> " + Integer.toString(windowFrequencyRate));
        System.out.println("  ");
        System.out.println("  bandpassLow -> " + Double.toString(bandpassLow));
        System.out.println("  bandpassHight -> " + Double.toString(bandpassHight));
        System.out.println("  ");
        System.out.println("  inputDir -> " + inputDir);
        System.out.println("  outputDir -> " + outputDir);
        System.out.println("  ");
        System.out.println("  wavLengthLimited -> " + Boolean.toString(wavLengthLimited));
        System.out.println("  wavLengthMin -> " + Integer.toString(wavLengthMin));
        System.out.println("  wavLengthMax -> " + Integer.toString(wavLengthMax));
        System.out.println("  ");
        int index = 1;
        for(HeartSoundCategory cat: categories) {
            System.out.println("  name " + index + " -> " + cat.getName());
            System.out.println("  path " + index + " -> " + cat.getPath());
            System.out.println("  tag " + index + " -> " + cat.getTag());
            System.out.println("  ");
            index++;
        }
        System.out.println("-------------------------------------------------------");
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getWindowStep() {
        return windowStep;
    }

    public double getBandpassLow() {
        return bandpassLow;
    }

    public double getBandpassHight() {
        return bandpassHight;
    }

    public String getInputDir() {
        return inputDir;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public boolean isWavLengthLimited() {
        return wavLengthLimited;
    }

    public int getWavLengthMin() {
        return wavLengthMin;
    }

    public int getWavLengthMax() {
        return wavLengthMax;
    }

    public List<HeartSoundCategory> getCategories() {
        return categories;
    }

    public boolean isWindowAuto() {
        return windowAuto;
    }

    public int getWindowFrequencyRate() {
        return windowFrequencyRate;
    }

    public boolean isAppSpectrogramSave() {
        return appSpectrogramSave;
    }

    public int getAppPcgHigh() {
        return appPcgHigh;
    }

    public boolean isAppPcgNarrow() {
        return appPcgNarrow;
    }

    public FTransormType getAppSpectrumWay() {
        return appSpectrumWay;
    }

    public void setAppSpectrumWay(FTransormType appSpectrumWay) {
        this.appSpectrumWay = appSpectrumWay;
    }

    public SegmentationType getAppSegmentationType() {
        return appSegmentationType;
    }

    public void setAppSegmentationType(SegmentationType appSegmentationType) {
        this.appSegmentationType = appSegmentationType;
    }

    public SegmentationLogicType getAppSegmentationLogicType() {
        return appSegmentationLogicType;
    }

    public void setAppSegmentationLogicType(SegmentationLogicType appSegmentationLogicType) {
        this.appSegmentationLogicType = appSegmentationLogicType;
    }

    public double getAppSegmentationThreshold() {
        return appSegmentationThreshold;
    }

    public void setAppSegmentationThreshold(double appSegmentationThreshold) {
        this.appSegmentationThreshold = appSegmentationThreshold;
    }
}