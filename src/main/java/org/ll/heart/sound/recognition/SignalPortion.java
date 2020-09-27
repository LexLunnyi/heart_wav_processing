package org.ll.heart.sound.recognition;

import org.apache.commons.math3.complex.Complex;

/**
 *
 * @author aberdnikov
 */
public class SignalPortion {
    double[] in;
    double[] out;
    Complex[] spectrum;
    
    public SignalPortion(double[] in) {
        this.in = in;
    }
}
