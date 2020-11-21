package org.ll.heart.sound.recognition.segmentation;

import java.util.Iterator;
import java.util.Queue;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public abstract class WindowSegmentation implements SegmentationService {
    final private Queue<SignalPortion> window;
    final private Iterator<SignalPortion> iter;
    final private int windowSize;
    private boolean started = false;
    
    public WindowSegmentation(int windowSize) throws IllegalArgumentException {
        window = new CircularFifoQueue<>(windowSize+1);
        iter = window.iterator();
        this.windowSize = windowSize;
        int half = windowSize / 2;
        if (windowSize != half*2) {
            throw new IllegalArgumentException("The window size must be even, the wrong value: " + windowSize);
        }
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
                markProcess(iter.next());
            } else {
                throw new IllegalStateException("Window doesn't have signal portion for mark");
            }           
        }
    }
    
    private void markBulk() throws IllegalStateException {
        int cnt = 1;
        while ((iter.hasNext()) && (cnt < windowSize / 2)) {
            markProcess(iter.next());
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
    
    protected abstract void markProcess(SignalPortion portion) throws IllegalStateException;

    protected abstract void addProcess(SignalPortion portion);
    
    protected abstract void removeProcess(SignalPortion portion);
}