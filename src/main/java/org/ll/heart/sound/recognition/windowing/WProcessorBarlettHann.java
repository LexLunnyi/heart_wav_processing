package org.ll.heart.sound.recognition.windowing;

/**
 * @author aberdnikov
 */
public class WProcessorBarlettHann extends WindowingProcessor {
    final static double A_0 = 0.62;
    final static double A_1 = 0.48;
    final static double A_2 = 0.38;
    
    @Override
    double getW(double i) {
        return A_0 - A_1*Math.abs(i/(N-1.0)-0.5) - A_2*Math.cos((2.0*Math.PI)/(N-1.0));
    }
}
