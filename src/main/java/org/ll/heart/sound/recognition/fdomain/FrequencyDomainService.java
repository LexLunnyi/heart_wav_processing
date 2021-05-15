package org.ll.heart.sound.recognition.fdomain;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 * Interface for classes that perform obtaining a signal spectrum
 * 
 * @author aberdnikov
 */
public interface FrequencyDomainService {
    /**
     * Forward conversion from time to frequency domain
     * @param portion object with source and destination data
     */
    void forward(SignalPortion portion);
    
    /**
     * Backward conversion from frequency to time domain
     * @param portion object with source and destination data
     */
    void inverse(SignalPortion portion);
    
    /**
     * Features extraction from signal spectrum
     * @param portion object with source and destination data
     */
    void features(SignalPortion portion);
    
    /**
     * Features extraction from signal spectrum in case we need diff between two
     * consequent points in time
     * @param prev previous object with source data
     * @param portion object with source and destination data
     */
    void features(SignalPortion prev, SignalPortion portion);
}