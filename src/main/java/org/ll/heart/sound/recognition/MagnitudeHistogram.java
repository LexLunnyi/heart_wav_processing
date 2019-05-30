/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ll.heart.sound.recognition;

/**
 * !!!! FOR NORMALIZED VALUES ONLY !!!!
 */
public class MagnitudeHistogram {
    private final int size;
    private final int values[];
    private int count = 0;
    
    public MagnitudeHistogram(int size) {
        this.size = size;
        this.values = new int[size];
    }
    
    public void push(double magnitude) {
        count++;
        for (int i = size-1; i >= 0; i--) {
            double threshold = (double)i/(double)size;

            if (magnitude >= threshold) {
                values[i]++;
                break;
            }
        }
    }

    @Override
    public String toString() {
        String res = "MagnitudeHistogram{" + "size=" + size + ", values: \n";
        for (int i = 0; i < size; i++) {
            res += i + ";" + values[i] + ";\n";
        }
        res += "}\n";
        return res;
    }
    
    public double getThreshold() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += values[i];
            if ((double)sum/(double)count >= 0.8D) {
                return (double)(i+1)/(double)size;
            }
        }
        return 0.0D;
    }
}
