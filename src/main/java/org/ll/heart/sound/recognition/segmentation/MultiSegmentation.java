package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class MultiSegmentation implements SegmentationService {
    protected WindowSegmentation wsFrequency;
    protected WindowSegmentation wsMagnitude;

    public void setFrequencySegmenter(WindowSegmentation wsFrequency) {
        this.wsFrequency = wsFrequency;
    }

    public void setMagnitudeSegmenter(WindowSegmentation wsMagnitude) {
        this.wsMagnitude = wsMagnitude;
    }

    @Override
    public void setThreshold(double threshold) {
        wsFrequency.setThreshold(threshold);
        wsMagnitude.setThreshold(threshold);
    }
    
    @Override
    public void process(SignalPortion portion) {
        wsFrequency.process(portion);
        wsMagnitude.process(portion);
    }

    @Override
    public void finish() {
        wsFrequency.finish();
        wsMagnitude.finish();
    }
    
}
