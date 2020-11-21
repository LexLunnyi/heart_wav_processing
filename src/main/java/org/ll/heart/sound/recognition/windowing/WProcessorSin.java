package org.ll.heart.sound.recognition.windowing;

/**
 * @author aberdnikov
 */
public class WProcessorSin extends WindowingProcessor {
    @Override
    double getW(double i) {
        return Math.sin((Math.PI*i)/(N - 1.0));
    }
}