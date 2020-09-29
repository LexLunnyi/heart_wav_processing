package org.ll.heart.sound.recognition.fdomain;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public interface FrequencyDomainService {
    void process(SignalPortion portion);
    void process(SignalPortion prev, SignalPortion portion);
}