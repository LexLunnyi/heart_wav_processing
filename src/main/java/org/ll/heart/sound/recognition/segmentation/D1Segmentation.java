package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.FIFOStatistic;
import org.ll.heart.sound.recognition.utils.GetValueFromPortion;

/**
 * @author aberdnikov
 */
final public class D1Segmentation extends WindowSegmentation {
    final GetValueFromPortion getter;
    final FIFOStatistic stat = new FIFOStatistic();
    
    public D1Segmentation(GetValueFromPortion getter, int windowSize) {
        super(windowSize);
        this.getter = getter;
    }
    
    @Override
    protected void addProcess(SignalPortion portion) {
        stat.add(getter.get(portion));
    }
    
    @Override
    protected void removeProcess(SignalPortion portion) {
        stat.subtract(getter.get(portion));
    }

    @Override
    protected void markProcess(SignalPortion portion) {
        portion.setSx(getter.get(portion) > (stat.getMean() + stat.getStandardDeviation()));
        portion.setStatMagnitudeMean(stat.getMean());
        portion.setStatMagnitudeSD(stat.getStandardDeviation());
    }
}