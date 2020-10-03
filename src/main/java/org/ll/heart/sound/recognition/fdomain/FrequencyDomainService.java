package org.ll.heart.sound.recognition.fdomain;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public interface FrequencyDomainService {
    void forward(SignalPortion portion);
    void inverse(SignalPortion portion);
    void features(SignalPortion prev, SignalPortion portion);
}