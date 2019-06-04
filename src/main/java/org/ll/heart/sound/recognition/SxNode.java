/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ll.heart.sound.recognition;

/**
 *
 * @author aberdnikov
 */
public class SxNode {
    private long indexBegin = 0;
    private long indexSx = 0;
    private long indexEnd = 0;
    private double sumMagnitude = 0.0;
    private double avgMagnitude = 0.0;
    private long duration = 0;
    private boolean s1 = false;
    private boolean s2 = false;
    
    public SxNode(long startIndex) {
        indexBegin = startIndex;
    }
    
    public void processMagnitude(double magnitude) {
        sumMagnitude += magnitude;
    }
    
    public void SxDone(long index) {
        indexSx = index;
        avgMagnitude = sumMagnitude / (double)(indexSx - indexBegin);
    }
    
    public void finalize(long index) {
        indexEnd = index;
        duration = indexEnd - indexBegin;
    }

    public double getAvgMagnitude() {
        return avgMagnitude;
    }

    public long getDuration() {
        return duration;
    }

    public boolean isS1() {
        return s1;
    }

    public boolean isS2() {
        return s2;
    }
    
    
}
