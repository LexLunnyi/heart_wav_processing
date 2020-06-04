package org.ll.heart.sound.recognition.windowing;

/**
 * @author aberdnikov
 */
public class WProcessorLanczos extends WindowingProcessor {
    
    double sinc(double i) {
        return Math.sin(Math.PI*i) / (Math.PI*i);
    }
    
    @Override
    double getW(double i) {
        return sinc((2.0*i)/(N - 1.0) - 1.0);
    }
}
