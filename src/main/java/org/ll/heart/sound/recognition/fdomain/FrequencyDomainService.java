package org.ll.heart.sound.recognition.fdomain;

/**
 *
 * @author aberdnikov
 */
public interface FrequencyDomainService {
    SignalPortion process(SignalPortion portion);
    SignalPortion process(SignalPortion prev, SignalPortion portion);
    
}