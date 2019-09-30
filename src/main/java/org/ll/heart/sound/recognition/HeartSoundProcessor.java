package org.ll.heart.sound.recognition;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import org.ll.heart.sound.recognition.wav.WavContainer;
import org.ll.heart.sound.recognition.wav.WavFile;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundProcessor {
    private final Options options;

    public HeartSoundProcessor(String optionsPath) {
        this.options = new Options(optionsPath);
    }
    
    private List<String> searchFiles(String path) {
        List<String> res = new ArrayList();
        File f = new File(path);
        File[] matchingFiles = f.listFiles((File dir, String name) -> name.endsWith("wav") || name.endsWith("WAV"));
        for (File matchingFile : matchingFiles) {
            res.add(matchingFile.getName());
        }
        return res;
    }
    
    
    public void processFiles() throws Exception {
        try (FileWriter fileWriter = new FileWriter(options.getOutputDir() + "/output.csv")) {
            String resRow = "N;file;sampleRate;channels;windowSize;windowStep;\n";
            fileWriter.write(resRow);
            int index = 1;

            //Search for WAV files;
            for (HeartSoundCategory hsc : options.getCategories()) {
                String inputPath = options.getInputDir() + "/" + hsc.getPath();
                String outputPath = options.getOutputDir() + "/" + hsc.getPath();
                //Get list of files
                List<String> files = searchFiles(inputPath);
                for (String file : files) {
                    System.out.print("FILE: " + file + "\n");
                    WavContainer wav = new WavContainer(inputPath + "/" + file, this.options);
                    wav.makeOutput();
                    String outFileName = outputPath + "/" + file + ".csv";
                    wav.saveCSV(outFileName);
                    WavFile header = wav.getWavFile();
                    resRow = Integer.toString(index) + ";" + hsc.getPath() + "/" + file + ".csv;" + 
                             Long.toString(header.getSampleRate()) + ";" + Integer.toString(header.getNumChannels()) + ";" +
                             Integer.toString(wav.getWindowSize()) + ";" + Integer.toString(wav.getWindowStep()) + "\n";
                    fileWriter.write(resRow);
                    index++;
                }
            }
        }
    }
}
 