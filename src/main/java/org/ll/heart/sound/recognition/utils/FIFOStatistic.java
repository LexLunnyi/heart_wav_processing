package org.ll.heart.sound.recognition.utils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class provides the statistic collection for values in FIFO containers. 
 * It calculates such parameters as mean, variance, standard deviation, minimum, 
 * and maximum values. It is essential that the class provides the getting of 
 * all parameters with constant time complexity O(1).
 * 
 * See https://en.wikipedia.org/wiki/Algorithms_for_calculating_variance
 * 
 * @author aberdnikov
 */
public class FIFOStatistic {
    int counter = 0;
    double first = 0.0;
    double Ex = 0.0;
    double Ex2 = 0.0;
    //Two stacks for keeping minimum and maximum elements
    Deque<MinMaxNode> inD = new LinkedList<>();
    Deque<MinMaxNode> outD = new LinkedList<>();
    
    /**
     * Adds a new value for participating in statistical parameters calculation
     * 
     * @param value to be added
     */
    public void add(double value) {
        //Update mean and variance
        if (0 == counter) {
            first = value;
        }
        counter++;
        double diff = value - first;
        Ex += diff;
        Ex2 += Math.pow(diff, 2);
        //Push element to the first stack with updating of the min and max values
        MinMaxNode l = inD.peekLast();
        MinMaxNode n = (null == l) ? new MinMaxNode(value) : new MinMaxNode(value, l);
        inD.addLast(n);
    }
    
    /**
     * Removes an old value from participating in statistical parameters calculation
     * 
     * @param value to be removed
     * @throws NoSuchElementException if the object doesn't have the element 
     *         with the specified value to be removed
     */
    public void subtract(double value) throws NoSuchElementException {
        //If out stack is empty then we should rewind inD stack here
        if (outD.isEmpty()) rewindStack();
        //Check that the element exists
        double peek = outD.peekLast().getValue();
        if (peek != value) {
            throw new NoSuchElementException("Trying to subtract " + Double.toString(value) + " but found " + Double.toString(peek));
        }
        //Remove from min-max-queue
        outD.removeLast();
        //Update mean and variance
        counter--;
        double diff = value - first;
        Ex -= diff;
        Ex2 -= Math.pow(diff, 2);
    }
    
    /**
     * Rewinds the elements from the input stack to the output 
     * stack for further finding minimum and maximum values
     * 
     * @throws NoSuchElementException if the input stack is empty
     */
    private void rewindStack() throws NoSuchElementException {
        if (inD.isEmpty()) {
            throw new NoSuchElementException("There is no elments for subscrtuction");
        }
        MinMaxNode cur = new MinMaxNode(inD.removeLast().getValue());
        outD.add(cur);

        while (!inD.isEmpty()) {
            MinMaxNode elem = inD.removeLast();
            outD.add(new MinMaxNode(elem.getValue(), cur));
        }
    }
    
    /**
     * Calculates the mean value
     * 
     * @return the mean of the values in the container
     */
    public double getMean() throws NoSuchElementException {
        if (counter < 1) return 0.0;
        return first + Ex / counter;
    }
    
    /**
     * Calculates the variance value
     * 
     * @return the variance of the values in the container
     */
    public double getVariance() {
        if (counter < 2) return 0.0;
        return (Ex2 - (Ex * Ex) / counter) / counter;
    }
    
    /**
     * Calculates the standard deviation
     * 
     * @return the variance of the values in the container
     */
    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }
    
    /**
     * Finds the minimal element
     * 
     * @return the minimal element of the values in the container
     * @throws NoSuchElementException if the container is empty
     */
    public double getMin() throws NoSuchElementException {
        if (inD.isEmpty()) {
            if (outD.isEmpty()) {
                throw new NoSuchElementException("It is impossible to take MIN");
            } else {
                return outD.peekLast().getMin();
            }
        } else {
            if (outD.isEmpty()) {
                return inD.peekLast().getMin();
            } else {
                return Math.min(inD.peekLast().getMin(), outD.peekLast().getMin());
            }            
        }
    }
    
    /**
     * Finds the maximal element
     * 
     * @return the maximal element of the values in the container
     * @throws NoSuchElementException if the container is empty
     */
    public double getMax() throws NoSuchElementException {
        if (inD.isEmpty()) {
            if (outD.isEmpty()) {
                throw new NoSuchElementException("It is impossible to take MAX");
            } else {
                return outD.peekLast().getMax();
            }
        } else {
            if (outD.isEmpty()) {
                return inD.peekLast().getMax();
            } else {
                return Math.min(inD.peekLast().getMax(), outD.peekLast().getMax());
            }            
        }
    }
}
