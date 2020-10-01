package org.ll.heart.sound.recognition.filter;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public interface FilterService {
    void filter(SignalPortion portion);
}
