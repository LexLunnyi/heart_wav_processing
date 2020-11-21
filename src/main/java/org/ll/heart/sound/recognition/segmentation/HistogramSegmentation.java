package org.ll.heart.sound.recognition.segmentation;

import java.util.LinkedList;
import java.util.List;
import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.AdaptiveHistogram;

/**
 *
 * @author aberdnikov
 */
public class HistogramSegmentation extends WindowSegmentation {
    AdaptiveHistogram hist = null;
    final int windowSize;
    final List<Double> initData = new LinkedList<>();
    final double threshold;

    public HistogramSegmentation(int windowSize, double threshold) throws IllegalArgumentException {
        super(windowSize);
        this.windowSize = windowSize;
        this.threshold = threshold;
    }

    @Override
    protected void markProcess(SignalPortion portion) throws IllegalStateException {
        if (hist == null) {
            try {
                hist = new AdaptiveHistogram(initData, windowSize, threshold);
            } catch (IllegalArgumentException ex) {
                throw new IllegalStateException("Error to create histogram: " + ex.getMessage());
            }
        }
        portion.setSx(portion.getMagnitude() >= hist.getThreshold());
        portion.setThresholdHistogram(hist.getThreshold());
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