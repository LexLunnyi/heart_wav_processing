package org.ll.heart.sound.recognition;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.ll.heart.sound.recognition.wav.WavFileException;

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
    
    
    private PCGWrapper processFile(String path, String file) throws IOException, WavFileException {
        String inputPath = options.getInputDir() + "/" + path;
        String outputPath = options.getOutputDir() + "/" + path;
        
        String outFileName = outputPath + "/" + file;
        File dir = new File(outputPath);
        dir.mkdir();
        
        PCGWrapper wrapper = new PCGWrapper(new File(inputPath + "/" + file), options);
        wrapper.process(outFileName);
        return wrapper;
    }
    
    
    public void processFiles() throws Exception {
        try (FileWriter fileWriter = new FileWriter(options.getOutputDir() + "/output.csv")) {
            String resRow = "INDEX;GROUP;PATH;FILE;SAMPLE_RATE;WINDOW_SIZE\n";
            fileWriter.write(resRow);
            int index = 1;

            //Search for WAV files;
            for (HeartSoundCategory hsc : options.getCategories()) {
                //Get list of files
                List<String> files = searchFiles(options.getInputDir() + "/" + hsc.getPath());
                for (String file : files) {
                    System.out.print("FILE: " + file + "\n");
                    PCGWrapper wrapper = processFile(hsc.getPath(), file);
                    resRow = Integer.toString(index) + ";" + Integer.toString(hsc.getIndex()) + ";" + hsc.getPath() + ";" + file + ".csv;" + 
                             Double.toString(wrapper.getSampleRate()) + ";" + Integer.toString(wrapper.getWindowSize()) + "\n";
                    fileWriter.write(resRow);
                    index++;
                }
            }
        }
    }
}
 