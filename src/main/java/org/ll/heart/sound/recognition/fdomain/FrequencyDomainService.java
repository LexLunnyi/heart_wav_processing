package org.ll.heart.sound.recognition.fdomain;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public interface FrequencyDomainService {
    SignalPortion process(SignalPortion portion);
    SignalPortion process(SignalPortion prev, SignalPortion portion);
    
}