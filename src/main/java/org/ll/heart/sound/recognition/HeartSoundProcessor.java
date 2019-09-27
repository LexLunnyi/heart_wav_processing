package org.ll.heart.sound.recognition;

/**
 *
 * @author aberdnikov
 */
public class HeartSoundProcessor {
    private final Options options;

    public HeartSoundProcessor(String optionsPath) {
        this.options = new Options(optionsPath);
    }
    
    public void processFiles() {
        //Search for WAV files;
    }
}
