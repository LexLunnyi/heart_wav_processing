package org.ll.heart.sound.recognition.fdomain;

import java.util.Arrays;
import org.ll.heart.sound.recognition.SignalPortion;
import smile.wavelet.DaubechiesWavelet;

/**
 *
 * @author aberdnikov
 */
public class WaveletFrequencyDomain  implements FrequencyDomainService {
    private final DaubechiesWavelet w = new DaubechiesWavelet(6);
    final double sampleRate;
    final double freqStep;

    public WaveletFrequencyDomain(double sampleRate, double windowSize) {
        this.sampleRate = sampleRate;
        this.freqStep = sampleRate / windowSize;
    }

    @Override
    public void forward(SignalPortion portion) {
        portion.setCoeffs(Arrays.copyOf(portion.getIn(), portion.getIn().length));
        w.transform(portion.getCoeffs());
    }

    @Override
    public void inverse(SignalPortion portion) {
        portion.setOut(Arrays.copyOf(portion.getCoeffs(), portion.getCoeffs().length));
        w.inverse(portion.getOut());
        portion.setFiltered(portion.getOut()[0]);
    }

    @Override
    public void features(SignalPortion portion) {
        double[] coeffs = portion.getCoeffs();
        double Mfreq = 0.0;
        double max = 0.0;
        int size = coeffs.length;
        
        for(int i = 0; i < size; i++) {
            double cur = Math.abs(coeffs[i]);
            Mfreq += (i + 1) * freqStep * cur;
            max = Math.max(max, cur);
        }
        
        double e = 0.0;
        for(int i = 0; i < size; i++) {
            double cur = Math.abs(coeffs[i]) / max;
            double curS = Math.pow(cur, 2.0);
            e += curS * Math.log(curS);
        }

        portion.setMfreq(Mfreq);
        portion.setMagnitude((e*(-1.0))/size);
    }

    @Override
    public void features(SignalPortion prev, SignalPortion portion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}