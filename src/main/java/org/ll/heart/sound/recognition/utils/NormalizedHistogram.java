package org.ll.heart.sound.recognition.utils;

/**
 * !!!! FOR NORMALIZED VALUES ONLY !!!!
 */
public class NormalizedHistogram {
    final double divider;
    final int size;
    final int counters[];
    int count = 0;
    
    public NormalizedHistogram(int size, double divider) {
        this.divider = divider;
        this.size = size;
        this.counters = new int[size];
    }
    
    public void push(double magnitude) {
        count++;
        for (int i = size-1; i >= 0; i--) {
            double threshold = (double)i/(double)size;
            if (magnitude >= threshold) {
                counters[i]++;
                break;
            }
        }
    }

    public double getThreshold() {
        int sum = 0;
        for (int i = 0; i < size; i++) {
            sum += counters[i];
            if ((double)sum/(double)count >= divider) {
                return (double)(i)/(double)size;
            }
        }
        return 0.0D;
    }
    

    public String toString(String filter) {
        String res = "NormalizedHistogram{" + "size=" + size + ", counters: \n";
        for (int i = 0; i < size; i++) {
            res += filter + "=>" + i + ";" + counters[i] + ";\n";
        }
        res += "}\n";
        return res;
    }
}