package org.ll.heart.sound.recognition.windowing;

import org.apache.commons.math3.complex.Complex;

/**
 * @author aberdnikov
 */
public abstract class WindowingProcessor {
    protected double N;
    
    abstract double getW(double i);
    
    public Complex[] process(Complex[] in) {
        N = in.length;
                
        Complex[] res = new Complex[in.length];
        for (int i = 0; i < in.length; i++) {
            Complex updated = in[i].divide(getW(i));
            res[i] = new Complex(updated.getReal(), updated.getImaginary());
        }
        
        return res;
    }
}