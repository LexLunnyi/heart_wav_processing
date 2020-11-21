package org.ll.heart.sound.recognition.windowing;

/**
 * @author aberdnikov
 */
public class WProcessorBarlett extends WindowingProcessor {
    @Override
    double getW(double i) {
        double A = (N-1.0)/2.0;
        return 1.0 - Math.abs(i/A - 1.0);
    }
}
