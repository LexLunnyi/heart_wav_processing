package org.ll.heart.sound.recognition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.ll.heart.sound.recognition.wav.WavContainer;

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
        //Search for WAV files;
        for(HeartSoundCategory hsc: options.getCategories()) {
            String inputPath = options.getInputDir() + "/" + hsc.getPath();
            String outputPath = options.getOutputDir() + "/" + hsc.getPath();
            //Get list of files
            List<String> files = searchFiles(inputPath);
            for (String file: files) {
                System.out.print("FILE: " + file + "\n");
                WavContainer wav = new WavContainer(inputPath + "/" + file, this.options);
                wav.makeOutput();
                String outFileName = outputPath + "/" + file + ".csv";
                wav.saveCSV(outFileName);
            }
        }
    }
}
