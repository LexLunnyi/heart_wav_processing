package org.ll.heart.sound.recognition.utils;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public interface GetValueForStat {
    double get(SignalPortion portion);
}
