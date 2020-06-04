package org.ll.heart.sound.recognition.windowing;

/**
 * @author aberdnikov
 */
public class WProcessorHann extends WindowingProcessor {
    @Override
    double getW(double i) {
        return 0.5-0.5*Math.cos((2.0*Math.PI*i)/(N-1.0));
    }
}