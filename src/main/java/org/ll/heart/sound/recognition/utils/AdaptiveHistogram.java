package org.ll.heart.sound.recognition.utils;

import java.util.List;
import java.util.Optional;

/**
 * This class implements the histogram for full record with an accumulation 
 * during the record. In other words, it doesn't need to iterate through 
 * the full record to collect beans and calculate the threshold. The values 
 * add to the histogram and the threshold being updated after each new portion 
 * of the signal. It allows using histogram "online", at the time of making 
 * auscultation.
 * 
 * See https://github.com/LexLunnyi/heart_wav_processing/wiki/Adaptive-Histogram
 * 
 * @author aberdnikov
 */
public class AdaptiveHistogram {
    final int beans[];
    final double threshold;
    
    double max;
    double step;
    int size;
    int counter = 0;
    
    int left = 0;
    int thresholdIndex = 0;
    
    /**
     * Construct an empty adaptive histogram
     * @param in The initial data to fill histogram
     * @param size The half of count of the constructed beans
     * @param threshold The fraction of the frames started from the first bean 
     *                   from count of the all frames in the histogram that 
     *                   gives the threshold value
     * @throws IllegalArgumentException If the initial data is empty
     */
    public AdaptiveHistogram(List<Double> in, int size, double threshold) throws IllegalArgumentException {
        Optional<Double> maxIn = in.stream().reduce(Double::max);
        if (!maxIn.isPresent()) {
            throw new IllegalArgumentException("Wrong init data");
        }
        
        step = maxIn.get() / size;
        beans = new int[size*2];
        max = maxIn.get()*2;
        this.size = size;
        this.threshold = threshold;
        
        in.forEach(elem -> {
            add(elem);
        });
    }
    
    /**
     * Adds the new frame to the histogram
     * @param value The frame value
     */
    public void add(double value) {
        counter++;
        if (value > max) {
            squeeze();
            max = max * 2;
            step = step * 2;
        }
        //Increment needed bean
        int index = (int)Math.floor(value / step);
        beans[index]++;
        if (index <= thresholdIndex) left++;
        updateThreshold();
    }
    
    /**
     * Tries to update old threshold index with a new index
     * 
     * @param part Sum of the beans values
     * @param newIndex The index of the part's last bean
     * @return The {@code true} if the threshold index was updated
     */
    private boolean reachedThreshold(int part, int newIndex) {
        if (((double)part / (double) counter) >= threshold) {
            left = part;
            thresholdIndex = newIndex;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates the threshold index that is stored to return threshold value 
     * with O(1) time complexity
     */
    private void updateThreshold() {
        // Split the bean values to thress blocks: 
        // | beans values sum before left bean(leftPack) | left bean value(leftBean) | right bean value(rightBean) |
        // The old threshold is located berween leftBean and rightBean
        int leftBean = beans[thresholdIndex];
        int leftPack = left - leftBean;
        int rightBean = beans[thresholdIndex+1];
        
        //Update threshold index
        if ((thresholdIndex > 0) && reachedThreshold(leftPack, thresholdIndex-1)) return;
        if (reachedThreshold(left, thresholdIndex)) return;
        reachedThreshold(left+rightBean, thresholdIndex+1);
    }
    
    /**
     * Calculates the threshold value of the adaptive histogram
     * @return The threshold value
     */
    public double getThreshold() {
        return (thresholdIndex + 1) * step;
    }
    
    /**
     * Squeezes the beans values making their values diapason twice bigger
     */
    private void squeeze() {
        int part = 0;
        boolean reached = false;
        
        for(int i = 0; i < size; i++) {
            int newVal = beans[i*2] + beans[i*2+1];
            beans[i] = newVal;
            part += newVal;
            if (!reached) reached = reachedThreshold(part, i);
        }
        for(int i = size; i < size*2; i++) {
            beans[i] = 0;
        }
    }
}