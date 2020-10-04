package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class SegmentationThreshold implements SegmentationService {
    final SegmentationType type;
    final double magnitudeThreshold;
    final double mfreqThreshold;
    
    
    public SegmentationThreshold(SegmentationType type, double threshold) {
        if (SegmentationType.MAGNITUDE_THRESHOLD == type) {
            magnitudeThreshold = threshold;
            mfreqThreshold = 0.0;
        } else {
            magnitudeThreshold = 0.0;
            mfreqThreshold = threshold;            
        }
        this.type = type;
    }

    
    public SegmentationThreshold(SegmentationType type, double magnitudeThreshold, double mfreqThreshold) {
        this.type = type;
        this.magnitudeThreshold = magnitudeThreshold;
        this.mfreqThreshold = mfreqThreshold;
    }
    
    
    @Override
    public void process(SignalPortion portion) {
        if (null != type) switch (type) {
            case MAGNITUDE_THRESHOLD:
                portion.setSx(portion.getMagnitude() > magnitudeThreshold);
                break;
            case MFREQ_THRESHOLD:
                portion.setSx(portion.getMfreq() > mfreqThreshold);
                break;
            case MAGNITUDE_MFREQ_THRESHOLD:
                portion.setSx((portion.getMagnitude() > mfreqThreshold)||(portion.getMfreq() > mfreqThreshold));
                break;
            default:
                break;
        }  
    }
}
