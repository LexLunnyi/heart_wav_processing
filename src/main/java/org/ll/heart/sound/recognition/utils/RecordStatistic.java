package org.ll.heart.sound.recognition.utils;

import java.util.Date;
import org.ll.heart.sound.recognition.SignalPortion;



/**
 *
 * @author aberdnikov
 */
public class RecordStatistic {
    FIFOStatistic frequencyStat = new FIFOStatistic();
    FIFOStatistic magnitudeStat = new FIFOStatistic();
    
    SignalPortion min = new SignalPortion(0, new Date(), 0, null);
    SignalPortion max = new SignalPortion(0, new Date(), 0, null);
    SignalPortion mean = new SignalPortion(0, new Date(), 0, null);
    
    public void add(SignalPortion portion) {
        frequencyStat.add(portion.getMfreq());
        magnitudeStat.add(portion.getMagnitude());
        
        min.setMfreq(frequencyStat.getMin());
        min.setMagnitude(magnitudeStat.getMin());
        
        max.setMfreq(frequencyStat.getMax());
        max.setMagnitude(magnitudeStat.getMax());
        
        mean.setMfreq(frequencyStat.getMean());
        mean.setMagnitude(magnitudeStat.getMean());
    }
            
    public SignalPortion getMax() {
        return max;
    }
    
    public SignalPortion getMin() {
        return min;
    }
    
    public SignalPortion getMean() {
        return mean;
    }
}