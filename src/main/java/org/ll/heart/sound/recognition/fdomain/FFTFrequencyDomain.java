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
public class FFTFrequencyDomain implements FrequencyDomainService {
    final FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
    final double sampleRate;
    final double freqStep;

    public FFTFrequencyDomain(double sampleRate, double windowSize) {
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
    public void features(SignalPortion portion) {
        Complex[] sCur = portion.getSpectrum();
        int size = sCur.length;
        
        Complex m = new Complex(0);
        double Mfreq = 0.0;

        for (int i = 1; i < size/2; i++) {
            m = m.add(sCur[i]);
            Mfreq += (i + 1) * freqStep * sCur[i].abs();
        }
        portion.setMagnitude(m.abs());
        portion.setMfreq(Mfreq);
    }
    
    @Override
    public void features(SignalPortion prev, SignalPortion portion) {
        if (null == prev) {
            portion.setMagnitude(0.0);
            portion.setMfreq(0.0);
            return;
        }
        
        Complex[] sCur = portion.getSpectrum();
        Complex[] sPrev = prev.getSpectrum();
        int size = sCur.length;
        
        Complex m = new Complex(0);
        double Mfreq = 0.0;
        
        //note detection algorithm
        for (int i = 1; i < size/2; i++) {
            Complex diff = sPrev[i].subtract(sCur[i]); 
            m = m.add(diff);
            Mfreq += (i + 1) * freqStep * sCur[i].abs();
        }

        portion.setMagnitude(m.abs());
        portion.setMfreq(Mfreq);
    }
}