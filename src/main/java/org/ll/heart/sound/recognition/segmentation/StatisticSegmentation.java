package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.FIFOStatistic;
import org.ll.heart.sound.recognition.utils.GetValueFromPortion;
import org.ll.heart.sound.recognition.utils.RecordStatistic;
import org.ll.heart.sound.recognition.utils.SetValueToPortion;

/**
 *
 * @author aberdnikov
 */
public abstract class StatisticSegmentation extends WindowSegmentation {
    final GetValueFromPortion getter;
    final RecordStatistic globalStat;
    final FIFOStatistic localStat = new FIFOStatistic();

    public StatisticSegmentation(GetValueFromPortion getter, SetValueToPortion setter, RecordStatistic globalStat, int windowSize) throws IllegalArgumentException {
        super(setter, windowSize);
        this.getter = getter;
        this.globalStat = globalStat;
    }
    
    @Override
    protected abstract boolean markProcess(SignalPortion portion) throws IllegalStateException;

    @Override
    protected void addProcess(SignalPortion portion) {
        localStat.add(getter.get(portion));
    }

    @Override
    protected void removeProcess(SignalPortion portion) {
        localStat.subtract(getter.get(portion));
    }
}
