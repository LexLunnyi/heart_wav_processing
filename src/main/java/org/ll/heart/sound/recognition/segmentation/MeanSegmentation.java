package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.GetValueFromPortion;
import org.ll.heart.sound.recognition.utils.RecordStatistic;
import org.ll.heart.sound.recognition.utils.SetValueToPortion;

/**
 * @author aberdnikov
 */
public class MeanSegmentation extends StatisticSegmentation {
    public MeanSegmentation(GetValueFromPortion gvfp, SetValueToPortion svtp, RecordStatistic rs, int i) throws IllegalArgumentException {
        super(gvfp, svtp, rs, i);
    }
    
    @Override
    protected boolean markProcess(SignalPortion portion) {
        return localStat.getMean() > getter.get(globalStat.getMean());
    }
}