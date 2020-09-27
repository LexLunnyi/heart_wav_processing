package org.ll.heart.sound.recognition;

import java.io.File;
import org.ll.heart.sound.recognition.fdomain.FrequencyDomainFFT;
import org.ll.heart.sound.recognition.fdomain.FrequencyDomainService;

/**
 *
 * @author aberdnikov
 */
public class PCGWrapper {
    private final Options options;
    
    public PCGWrapper(File wav, Options options) {
        this.options = options;
    }
    
    public boolean process(File out) {
        configure();
        try {
            process();
            save(out);
        } catch (Exception ex) {
            System.err.println("ERROR: PCGWrapper.process -> " + ex);
            return false;
        }
        return true;
    }
        
    //Configure services for transorm to frequency domain, filtration and segmentation
    private void configure() {
        setFrequencyService(new FrequencyDomainFFT());
    }
    
    private void setFrequencyService(FrequencyDomainService fservie) {
        
    }
    
    private void process() {
        
    }
    
    private void save(File out) {
        
    }
}
