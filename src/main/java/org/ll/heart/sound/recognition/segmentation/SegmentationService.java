package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 * Interface for classes that perform segmentation of sample 
 * (belonging to S1 or S2 heart tones)
 * 
 * @author aberdnikov
 */
public interface SegmentationService {
    /**
     * Sets a new threshold value instead of default
     * @param threshold is new value
     */
    void setThreshold(double threshold);
    
    /**
     * Performs segmentation for passed sample
     * @param portion sample of the record that must be segmented
     */
    void process(SignalPortion portion);
    
    /**
     * Finishes segmentation process for record
     */
    void finish();
}