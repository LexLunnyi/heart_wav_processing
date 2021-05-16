package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.GetValueFromPortion;
import org.ll.heart.sound.recognition.utils.RecordStatistic;
import org.ll.heart.sound.recognition.utils.SetValueToPortion;

/**
 *
 * @author aberdnikov
 */
public class MinMaxSegmentation extends StatisticSegmentation {
    public MinMaxSegmentation(GetValueFromPortion gvfp, SetValueToPortion svtp, RecordStatistic rs, int i) throws IllegalArgumentException {
        super(gvfp, svtp, rs, i);
    }
    
    @Override
    protected boolean markProcess(SignalPortion portion) {
        double diffLocal = localStat.getMax() - localStat.getMin();
        double diffGlobal = getter.get(globalStat.getMax()) - getter.get(globalStat.getMin()); 
        double val = diffLocal / diffGlobal;
        return val > threshold;
    }
}
