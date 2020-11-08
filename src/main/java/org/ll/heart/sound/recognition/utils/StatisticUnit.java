package org.ll.heart.sound.recognition.utils;

/**
 * @author aberdnikov
 */
public class StatisticUnit {
    int counter = 0;
    double meanSum = 0.0;
    double varianceSum = 0.0;
    
    public void add(double value) {
        counter++;
        meanSum += value;
        varianceSum += Math.pow(value - getMean(), 2);
    }
    
    public void subsctract(double value) {
        counter--;
        meanSum -= value;
        varianceSum -= Math.pow(value - getMean(), 2);
        
    }
    
    public double getMean() {
        return meanSum / counter;
    }
    
    public double getVariance() {
        return varianceSum / counter;
    }
    
    public double getStandartDeviation() {
        return Math.sqrt(getVariance());
    }
}
