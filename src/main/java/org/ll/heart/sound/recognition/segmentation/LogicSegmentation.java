package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public abstract class LogicSegmentation implements SegmentationService {
    protected WindowSegmentation wsFrequency;
    protected WindowSegmentation wsMagnitude;
    
    public LogicSegmentation(int windowSize) {
        wsFrequency = new MinMaxSegmentation(SignalPortion::getMfreq, windowSize, windowSize);
    }

    @Override
    public abstract void process(SignalPortion portion);

    @Override
    public void finish() {
        wsFrequency.finish();
        wsMagnitude.finish();
    }
    
}
