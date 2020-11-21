package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.FIFOStatistic;
import org.ll.heart.sound.recognition.utils.GetValueFromPortion;

/**
 *
 * @author aberdnikov
 */
public class LocalMinMaxSegmentation extends WindowSegmentation {
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    final double threshold;
    
    final FIFOStatistic stat = new FIFOStatistic();
    final GetValueFromPortion getter;

    public LocalMinMaxSegmentation(GetValueFromPortion getter, int windowSize, double threshold) throws IllegalArgumentException {
        super(windowSize);
        this.threshold = threshold;
        this.getter = getter;
    }

    @Override
    protected void markProcess(SignalPortion portion) {
        double extVal = max - min;
        double intVal = stat.getMax() - stat.getMin();
        portion.setSx(intVal >= extVal * threshold);
    }

    @Override
    protected void addProcess(SignalPortion portion) {
        double value = getter.get(portion);
        min = Math.min(min, value);
        max = Math.max(max, value);
        stat.add(getter.get(portion));
    }

    @Override
    protected void removeProcess(SignalPortion portion) {
        stat.subtract(getter.get(portion));
    }
}
