package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.GetValueForStat;
import org.ll.heart.sound.recognition.utils.FIFOStatistic;

/**
 * @author aberdnikov
 */
final public class D1Segmentation extends WindowSegmentation {
    final GetValueForStat forStat;
    final FIFOStatistic stat = new FIFOStatistic();
    
    public D1Segmentation(GetValueForStat func, int windowSize) {
        super(windowSize);
        this.forStat = func;
    }
    
    @Override
    protected void addProcess(SignalPortion portion) {
        stat.add(forStat.get(portion));
    }
    
    @Override
    protected void removeProcess(SignalPortion portion) {
        stat.subtract(forStat.get(portion));
    }

    @Override
    protected void markProcess(SignalPortion portion) {
        portion.setSx(forStat.get(portion) > (stat.getMean() + stat.getStandardDeviation()));
        //portion.setStatMagnitudeMean(stat.getMean());
        //portion.setStatMagnitudeSD(stat.getStandartDeviation());
    }
}
