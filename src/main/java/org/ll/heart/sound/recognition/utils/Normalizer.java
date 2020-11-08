package org.ll.heart.sound.recognition.utils;

import java.util.Date;
import org.ll.heart.sound.recognition.SignalPortion;



/**
 *
 * @author aberdnikov
 */
public class Normalizer {
    final int HIST_SIZE = 2048;
    final double HIST_THRES = 0.001;
    
    final SignalPortion max = new SignalPortion(0, new Date(0), 0, null);
    final NormalizedHistogram mHist = new NormalizedHistogram(HIST_SIZE, HIST_THRES);
    final NormalizedHistogram fHist = new NormalizedHistogram(HIST_SIZE, HIST_THRES);
    
    public void calc(SignalPortion cur) {
        max.setSource(Math.max(max.getSource(), cur.getSource()));
        max.setFiltered(Math.max(max.getFiltered(), cur.getFiltered()));
        max.setMagnitude(Math.max(max.getMagnitude(), cur.getMagnitude()));
        max.setMfreq(Math.max(max.getMfreq(), cur.getMfreq()));
        
        mHist.push(cur.getMagnitude());
        fHist.push(cur.getMfreq());
    }
    
    public void norm(SignalPortion cur) {
        cur.setSource(cur.getSource() / max.getSource());
        cur.setFiltered(cur.getFiltered() / max.getFiltered());
        cur.setMagnitude(cur.getMagnitude() / max.getMagnitude());
        cur.setMfreq(cur.getMfreq() / max.getMfreq());
    }
    
    public double getMagnitudeThreshold() {
        return mHist.getThreshold();
    }
    
    public double getMfreqThreshold() {
        return fHist.getThreshold();
    }
}