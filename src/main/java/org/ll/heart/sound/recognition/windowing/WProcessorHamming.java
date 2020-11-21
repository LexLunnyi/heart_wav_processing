package org.ll.heart.sound.recognition.windowing;

/**
 * @author aberdnikov
 */
public class WProcessorHamming extends WindowingProcessor {
    @Override
    double getW(double i) {
        return 0.54-0.46*Math.cos((2.0*Math.PI*i)/(N-1.0));
    }
}
