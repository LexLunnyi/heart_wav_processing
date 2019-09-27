package org.ll.heart.sound.recognition;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author aberdnikov
 */


public class Options {
    int windowSize;
    int windowStep;
    int bandpassLow;
    int bandpassHight;
    String inputDir;
    String outputDir;
    List<HeartSoundCategory> categories = new ArrayList();
    
    public Options(String path) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(path);
            // load a properties file
            prop.load(input);
            // get the property value and print it out
            windowSize = Integer.parseInt(prop.getProperty("window.size"));
            windowStep = Integer.parseInt(prop.getProperty("window.step"));
            bandpassLow = Integer.parseInt(prop.getProperty("bandpass.low"));
            bandpassHight = Integer.parseInt(prop.getProperty("bandpass.hight"));
            inputDir = prop.getProperty("data.input.dir");
            outputDir = prop.getProperty("data.output.dir");
            
            int categoriesCount = Integer.parseInt(prop.getProperty("categories.count"));
            for(int i = 1; i <= categoriesCount; i++) {
                String catName = prop.getProperty("categories[" + i + "].name");
                String catPath = prop.getProperty("categories[" + i + "].path");
                String catTag = prop.getProperty("categories[" + i + "].tag");
                categories.add(new HeartSoundCategory(catName, catPath, catTag));
            }

            printProperties();
        } catch (IOException ex) {
            System.out.print("Error read options: " + ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.print("Error close config-file: " + e);
                }
            }
        }
    }



    private void printProperties() {
        System.out.print("-------------------------------------------------------\n");
        System.out.print("OPTIONS: \n");
        System.out.print("  windowSize -> " + Integer.toString(windowSize) + "\n");
        System.out.print("  windowStep -> " + Integer.toString(windowStep) + "\n");
        System.out.print("  \n");
        System.out.print("  bandpassLow -> " + Integer.toString(bandpassLow) + "\n");
        System.out.print("  bandpassHight -> " + Integer.toString(bandpassHight) + "\n");
        System.out.print("  \n");
        System.out.print("  inputDir -> " + inputDir + "\n");
        System.out.print("  outputDir -> " + outputDir + "\n");
        System.out.print("  \n");
        int index = 1;
        for(HeartSoundCategory cat: categories) {
            System.out.print("  name " + index + " -> " + cat.getName() + "\n");
            System.out.print("  path " + index + " -> " + cat.getPath() + "\n");
            System.out.print("  tag " + index + " -> " + cat.getTag() + "\n");
            System.out.print("  \n");
            index++;
        }
        System.out.print("-------------------------------------------------------\n");
    }

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public int getWindowStep() {
        return windowStep;
    }

    public void setWindowStep(int windowStep) {
        this.windowStep = windowStep;
    }

    public int getBandpassLow() {
        return bandpassLow;
    }

    public void setBandpassLow(int bandpassLow) {
        this.bandpassLow = bandpassLow;
    }

    public int getBandpassHight() {
        return bandpassHight;
    }

    public void setBandpassHight(int bandpassHight) {
        this.bandpassHight = bandpassHight;
    }

    public String getInputDir() {
        return inputDir;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
}