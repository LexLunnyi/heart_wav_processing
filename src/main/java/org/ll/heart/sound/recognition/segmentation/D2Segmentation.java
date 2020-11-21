package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.FIFOStatistic;

/**
 *
 * @author aberdnikov
 */
public class D2Segmentation extends WindowSegmentation {
    final FIFOStatistic magnitudeStat = new FIFOStatistic();
    final FIFOStatistic frequencyStat = new FIFOStatistic();

    public D2Segmentation(int windowSize) throws IllegalArgumentException {
        super(windowSize);
    }

    @Override
    protected void markProcess(SignalPortion portion) {
        boolean fPart = (portion.getMfreq() > (frequencyStat.getMean() + frequencyStat.getStandardDeviation()));
        boolean mPart = (portion.getMagnitude() > (magnitudeStat.getMean() + magnitudeStat.getStandardDeviation()));
        portion.setSx(fPart && mPart);
    }

    @Override
    protected void addProcess(SignalPortion portion) {
        magnitudeStat.add(portion.getMagnitude());
        frequencyStat.add(portion.getMfreq());
    }

    @Override
    protected void removeProcess(SignalPortion portion) {
        magnitudeStat.subtract(portion.getMagnitude());
        frequencyStat.subtract(portion.getMfreq());
    }

}
