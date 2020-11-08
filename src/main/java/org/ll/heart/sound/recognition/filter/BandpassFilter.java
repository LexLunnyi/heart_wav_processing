package org.ll.heart.sound.recognition.filter;

import org.apache.commons.math3.complex.Complex;
import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class BandpassFilter implements FilterService {
    final double freqStep;
    final double low;
    final double hight;

    public BandpassFilter(double freqStep, double low, double hight) {
        this.freqStep = freqStep;
        this.low = low;
        this.hight = hight;
    }
    
    @Override
    public void filter(SignalPortion portion) {
        Complex[] s = portion.getSpectrum();
        int size = s.length;

        //String row = tsFormat.format(curPortion.getTs()) + ";";
        for (int i = 1; i < size / 2; i++) {
            double curFreq = i * freqStep;
            //Make bandpass filtration
            if ((curFreq <= low) || (curFreq >= hight)) {
                //Empty noise
                s[i] = new Complex(0);
                s[size-i-1] = new Complex(0);
            }
        }
    }
}
