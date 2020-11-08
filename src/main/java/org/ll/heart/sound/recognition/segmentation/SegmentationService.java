package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public interface SegmentationService {
    void process(SignalPortion portion);
    void finish();
}