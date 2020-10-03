package org.ll.heart.sound.recognition.fdomain;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.ll.heart.sound.recognition.SignalPortion;

/**
 *
 * @author aberdnikov
 */
public class FrequencyDomainFFT implements FrequencyDomainService {
    final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    final double sampleRate;
    final double freqStep;

    public FrequencyDomainFFT(double sampleRate, double windowSize) {
        this.sampleRate = sampleRate;
        this.freqStep = sampleRate / windowSize;
    }

    @Override
    public void forward(SignalPortion portion) {
        portion.setSpectrum(transformer.transform(portion.getIn(), TransformType.FORWARD));
    }

    @Override
    public void inverse(SignalPortion portion) {
        Complex[] out = transformer.transform(portion.getSpectrum(), TransformType.INVERSE);
        Complex val = out[0];
        if (val.getReal() < 0) {
            portion.setFiltered(val.abs() * (-1.0));
        } else {
            portion.setFiltered(val.abs());
        }
    }

    @Override
    public void features(SignalPortion prev, SignalPortion portion) {
        Complex[] sCur = portion.getSpectrum();
        Complex[] sPrev = prev.getSpectrum();
        int size = sCur.length;
        
        Complex m = new Complex(0);
        double Mfreq = 0.0;
        
        //note detection algorithm
        //for (int i = 0; i < size/2; i++) {
        //    Complex diff = sPrev[i].subtract(sCur[i]); 
        //    m = m.add(diff);
        //}
        for (int i = 0; i < size/2; i++) {
            m = m.add(sCur[i]);
            Mfreq += (i + 1) * freqStep * sCur[i].abs();
        }
        portion.setMagnitude(m.abs());
        portion.setMfreq(Mfreq);
    }
}