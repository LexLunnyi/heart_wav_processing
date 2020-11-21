package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.GetValueFromPortion;

/**
 *
 * @author aberdnikov
 */
public class MinMaxSegmentation extends WindowSegmentation {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    final double threshold;
    
    final GetValueFromPortion getter;

    public MinMaxSegmentation(GetValueFromPortion getter, int windowSize, double threshold) throws IllegalArgumentException {
        super(windowSize);
        this.threshold = threshold;
        this.getter = getter;
    }

    @Override
    protected void markProcess(SignalPortion portion) {
        double tVal = (max - min) * threshold + min;
        portion.setSx(getter.get(portion) >= tVal);
    }

    @Override
    protected void addProcess(SignalPortion portion) {
        double value = getter.get(portion);
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    @Override
    protected void removeProcess(SignalPortion portion) {
    }
}
