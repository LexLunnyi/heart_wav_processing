package org.ll.heart.sound.recognition.segmentation;

import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.RecordStatistic;

/**
 *
 * @author aberdnikov
 */
public class SegmentationServiceFactory {
    final RecordStatistic stat;
    final int windowSize;
    final double threshold;

    public SegmentationServiceFactory(final RecordStatistic stat, int windowSize, double threshold) {
        this.stat = stat;
        this.windowSize = windowSize;
        this.threshold = threshold;
    }
    
    public SegmentationService createMinMax(SegmentationLogicType logic) {
        MultiSegmentation res = new MultiSegmentation();
        switch(logic) {
            case FREQUENCY:
                res.setFrequencySegmenter(new MinMaxSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, stat, windowSize));
                res.setMagnitudeSegmenter(new MinMaxSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxStub, stat, windowSize));
                break;
            case MAGNITUDE:
                res.setFrequencySegmenter(new MinMaxSegmentation(SignalPortion::getMfreq, SignalPortion::setSxStub, stat, windowSize));
                res.setMagnitudeSegmenter(new MinMaxSegmentation(SignalPortion::getMagnitude, SignalPortion::setSx, stat, windowSize));
                break;
            case FREQUENCY_OR_MAGNITUDE:
                res.setFrequencySegmenter(new MinMaxSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, stat, windowSize));
                res.setMagnitudeSegmenter(new MinMaxSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxOrNew, stat, windowSize));
                break;
            case FREQUENCY_AND_MAGNITUDE:
                res.setFrequencySegmenter(new MinMaxSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, stat, windowSize));
                res.setMagnitudeSegmenter(new MinMaxSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxAndNew, stat, windowSize));
                break;
        }
        if (threshold > 0.0) {
            res.setThreshold(threshold);
        }
        return res;
    }
    
    public SegmentationService createMean(SegmentationLogicType logic) {
        MultiSegmentation res = new MultiSegmentation();
        switch(logic) {
            case FREQUENCY:
                res.setFrequencySegmenter(new MeanSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, stat, windowSize));
                res.setMagnitudeSegmenter(new MeanSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxStub, stat, windowSize));
                break;
            case MAGNITUDE:
                res.setFrequencySegmenter(new MeanSegmentation(SignalPortion::getMfreq, SignalPortion::setSxStub, stat, windowSize));
                res.setMagnitudeSegmenter(new MeanSegmentation(SignalPortion::getMagnitude, SignalPortion::setSx, stat, windowSize));
                break;
            case FREQUENCY_OR_MAGNITUDE:
                res.setFrequencySegmenter(new MeanSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, stat, windowSize));
                res.setMagnitudeSegmenter(new MeanSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxOrNew, stat, windowSize));
                break;
            case FREQUENCY_AND_MAGNITUDE:
                res.setFrequencySegmenter(new MeanSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, stat, windowSize));
                res.setMagnitudeSegmenter(new MeanSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxAndNew, stat, windowSize));
                break;
        }        
        return res;
    }
    
    public SegmentationService createHistogram(SegmentationLogicType logic) {
        MultiSegmentation res = new MultiSegmentation();
        switch(logic) {
            case FREQUENCY:
                res.setFrequencySegmenter(new HistogramSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, windowSize));
                res.setMagnitudeSegmenter(new HistogramSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxStub, windowSize));
                break;
            case MAGNITUDE:
                res.setFrequencySegmenter(new HistogramSegmentation(SignalPortion::getMfreq, SignalPortion::setSxStub, windowSize));
                res.setMagnitudeSegmenter(new HistogramSegmentation(SignalPortion::getMagnitude, SignalPortion::setSx, windowSize));
                break;
            case FREQUENCY_OR_MAGNITUDE:
                res.setFrequencySegmenter(new HistogramSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, windowSize));
                res.setMagnitudeSegmenter(new HistogramSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxOrNew, windowSize));
                break;
            case FREQUENCY_AND_MAGNITUDE:
                res.setFrequencySegmenter(new HistogramSegmentation(SignalPortion::getMfreq, SignalPortion::setSx, windowSize));
                res.setMagnitudeSegmenter(new HistogramSegmentation(SignalPortion::getMagnitude, SignalPortion::setSxAndNew, windowSize));
                break;
        }
        if (threshold > 0.0) {
            res.setThreshold(threshold);
        }
        return res;
    }
}
