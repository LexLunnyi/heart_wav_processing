package org.ll.heart.sound.recognition.segmentation;

import java.util.Iterator;
import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.ll.heart.sound.recognition.SignalPortion;
import org.ll.heart.sound.recognition.utils.SetValueToPortion;

/**
 * Abstract class for the window segmentation process. Before segmentation of 
 * any sample class collects the window of samples. Segmentation process uses 
 * all values in the window
 * @author aberdnikov
 */
public abstract class WindowSegmentation implements SegmentationService {
    final static double DEF_THRESHOLD = 0.5;
    final private Queue<SignalPortion> window;
    final private Iterator<SignalPortion> iter;
    final protected int windowSize;
    private boolean started = false;
    SetValueToPortion setter;
    double threshold;
    
    /**
     * Constructs the window for a class that implements this abstract class
     * and performs segmentation
     * @param setter
     * @param windowSize is size of the window
     * @throws IllegalArgumentException in case when window size is odd
     */
    public WindowSegmentation(SetValueToPortion setter, int windowSize) throws IllegalArgumentException {
        window = new CircularFifoQueue<>(windowSize+1);
        iter = window.iterator();
        this.setter = setter;
        this.windowSize = windowSize;
        int half = windowSize / 2;
        if (windowSize != half*2) {
            throw new IllegalArgumentException("The window size must be even, the wrong value: " + windowSize);
        }
    }

    @Override
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public final void process(SignalPortion portion) throws IllegalStateException {
        if (window.size() >= windowSize) {
            SignalPortion rpotion = window.remove();
            removeProcess(rpotion);
        }
        window.add(portion);
        addProcess(portion);
        
        //If window is ready we can start segmentation and mark first half of the window
        if ((!started) && (window.size() == windowSize)) {
            started = true;
            markBulk();
        }
        //Mark the right middle signal portion
        if (started) {
            if (iter.hasNext()) {
                mark(iter.next());
            } else {
                throw new IllegalStateException("Window doesn't have signal portion for mark");
            }           
        }
    }
    
    /**
     * Segments samples that left in the window when end of record was reached
     * @throws IllegalStateException 
     */
    private void markBulk() throws IllegalStateException {
        int cnt = 1;
        while ((iter.hasNext()) && (cnt < windowSize / 2)) {
            mark(iter.next());
            cnt++;
        }
        if (cnt != windowSize / 2) {
            throw new IllegalStateException("Window must have " + windowSize + " but has " + window.size());
        }
    }

    @Override
    public void finish() throws IllegalStateException {
        markBulk();
    }
    
    private void mark(SignalPortion portion) {
        boolean newVal = markProcess(portion);
        setter.set(portion, newVal);
    }
    
    /**
     * Marks sample as belonged to heart tones or not
     * @param portion sample that must be marked
     * @return <code>true</code> if sample belongs to heart tones
     * @throws IllegalStateException
     */
    protected abstract boolean markProcess(SignalPortion portion) throws IllegalStateException;

    /**
     * Called when a new sample is adding to the window
     * @param portion is sample data
     */
    protected abstract void addProcess(SignalPortion portion);
    
    /**
     * Called when an old sample is removing from the window
     * @param portion is sample data
     */
    protected abstract void removeProcess(SignalPortion portion);
}