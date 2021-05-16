package org.ll.heart.sound.recognition.segmentation;

import java.util.LinkedList;
import java.util.List;
import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.AdaptiveHistogram;
import org.ll.heart.sound.recognition.utils.GetValueFromPortion;
import org.ll.heart.sound.recognition.utils.SetValueToPortion;

/**
 *
 * @author aberdnikov
 */
public class HistogramSegmentation extends WindowSegmentation {
    AdaptiveHistogram hist = null;
    final GetValueFromPortion getter;
    final List<Double> initData = new LinkedList<>();

    public HistogramSegmentation(GetValueFromPortion getter, SetValueToPortion setter, int windowSize) throws IllegalArgumentException {
        super(setter, windowSize);
        this.getter = getter;
    }

    @Override
    protected boolean markProcess(SignalPortion portion) throws IllegalStateException {
        if (hist == null) {
            try {
                hist = new AdaptiveHistogram(initData, windowSize, threshold);
            } catch (IllegalArgumentException ex) {
                throw new IllegalStateException("Error to create histogram: " + ex.getMessage());
            }
        }
        return getter.get(portion) >= hist.getThreshold();
    }

    @Override
    protected void addProcess(SignalPortion portion) {
        double value = portion.getMagnitude();
        if (hist == null) {
            initData.add(value);
        } else {
            hist.add(portion.getMagnitude());
        }
    }

    @Override
    protected void removeProcess(SignalPortion portion) {
    }
}