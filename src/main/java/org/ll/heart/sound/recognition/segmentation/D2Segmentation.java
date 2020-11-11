package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.StatisticUnit;

/**
 *
 * @author aberdnikov
 */
public class D2Segmentation extends WindowSegmentation {
    final StatisticUnit magnitudeStat = new StatisticUnit();
    final StatisticUnit frequencyStat = new StatisticUnit();

    public D2Segmentation(int windowSize) throws IllegalArgumentException {
        super(windowSize);
    }

    @Override
    protected void markProcess(SignalPortion portion) {
        boolean fPart = (portion.getMfreq() < (frequencyStat.getMean() + frequencyStat.getStandartDeviation()));
        boolean mPart = (portion.getMagnitude() < (magnitudeStat.getMean() + magnitudeStat.getStandartDeviation()));
        portion.setSx(fPart && mPart);
    }

    @Override
    protected void addProcess(SignalPortion portion) {
        magnitudeStat.add(portion.getMagnitude());
        frequencyStat.add(portion.getMfreq());
    }

    @Override
    protected void removeProcess(SignalPortion portion) {
        magnitudeStat.subsctract(portion.getMagnitude());
        frequencyStat.subsctract(portion.getMfreq());
    }

}
